package com.company.employees.service.impl;

import com.company.employees.dto.EmployeeRequestDTO;
import com.company.employees.dto.EmployeeResponseDTO;
import com.company.employees.entity.Employee;
import com.company.employees.exception.DuplicateResourceException;
import com.company.employees.exception.ResourceNotFoundException;
import com.company.employees.mapper.EmployeeMapper;
import com.company.employees.repository.EmployeeRepository;
import com.company.employees.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<EmployeeResponseDTO> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    public EmployeeResponseDTO findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id)
                );
        return employeeMapper.toResponse(employee);
    }

    @Override
    public EmployeeResponseDTO create(EmployeeRequestDTO request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Employee already exists with email: " + request.getEmail()
            );
        }

        Employee employee = employeeMapper.toEntity(request);
        Employee saved = employeeRepository.save(employee);

        return employeeMapper.toResponse(saved);
    }

    @Override
    public EmployeeResponseDTO update(Long id, EmployeeRequestDTO request) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id)
                );

        // Update fields manually (clean & explicit)
        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setEmail(request.getEmail());
        existing.setPosition(request.getPosition());
        existing.setDepartment(request.getDepartment());
        existing.setSalary(request.getSalary());

        Employee updated = employeeRepository.save(existing);
        return employeeMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id)
                );
        employeeRepository.delete(employee);
    }
}
