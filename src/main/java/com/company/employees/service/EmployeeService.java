package com.company.employees.service;

import com.company.employees.dto.EmployeeRequestDTO;
import com.company.employees.dto.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeResponseDTO> findAll();

    EmployeeResponseDTO findById(Long id);

    EmployeeResponseDTO create(EmployeeRequestDTO request);

    EmployeeResponseDTO update(Long id, EmployeeRequestDTO request);

    void delete(Long id);
}
