package com.company.employees.service.impl;

import com.company.employees.dto.EmployeeRequestDTO;
import com.company.employees.dto.EmployeeResponseDTO;
import com.company.employees.entity.Employee;
import com.company.employees.exception.DuplicateResourceException;
import com.company.employees.exception.ResourceNotFoundException;
import com.company.employees.mapper.EmployeeMapper;
import com.company.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void findAll_returnsMappedEmployees() {
        Employee employee1 = buildEmployee(1L, "John", "Doe", "john@example.com");
        Employee employee2 = buildEmployee(2L, "Jane", "Smith", "jane@example.com");
        EmployeeResponseDTO response1 = buildResponse(1L, "John", "Doe", "john@example.com");
        EmployeeResponseDTO response2 = buildResponse(2L, "Jane", "Smith", "jane@example.com");

        when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));
        when(employeeMapper.toResponse(employee1)).thenReturn(response1);
        when(employeeMapper.toResponse(employee2)).thenReturn(response2);

        List<EmployeeResponseDTO> result = employeeService.findAll();

        assertEquals(2, result.size());
        assertSame(response1, result.get(0));
        assertSame(response2, result.get(1));
        verify(employeeRepository).findAll();
    }

    @Test
    void findById_returnsEmployeeWhenFound() {
        Employee employee = buildEmployee(5L, "Alice", "Green", "alice@example.com");
        EmployeeResponseDTO response = buildResponse(5L, "Alice", "Green", "alice@example.com");
        when(employeeRepository.findById(5L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponse(employee)).thenReturn(response);

        EmployeeResponseDTO result = employeeService.findById(5L);

        assertSame(response, result);
        verify(employeeRepository).findById(5L);
        verify(employeeMapper).toResponse(employee);
    }

    @Test
    void findById_throwsResourceNotFoundExceptionWhenMissing() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.findById(99L));
        verify(employeeRepository).findById(99L);
        verifyNoInteractions(employeeMapper);
    }

    @Test
    void create_savesEmployeeWhenEmailIsUnique() {
        EmployeeRequestDTO request = EmployeeRequestDTO.builder()
                .firstName("Bob")
                .lastName("Brown")
                .email("bob@example.com")
                .position("Engineer")
                .department("R&D")
                .salary(BigDecimal.valueOf(75000))
                .build();
        Employee toSave = buildEmployee(null, "Bob", "Brown", "bob@example.com");
        Employee saved = buildEmployee(10L, "Bob", "Brown", "bob@example.com");
        EmployeeResponseDTO response = buildResponse(10L, "Bob", "Brown", "bob@example.com");

        when(employeeRepository.existsByEmail("bob@example.com")).thenReturn(false);
        when(employeeMapper.toEntity(request)).thenReturn(toSave);
        when(employeeRepository.save(toSave)).thenReturn(saved);
        when(employeeMapper.toResponse(saved)).thenReturn(response);

        EmployeeResponseDTO result = employeeService.create(request);

        assertSame(response, result);
        verify(employeeRepository).existsByEmail("bob@example.com");
        verify(employeeRepository).save(toSave);
        verify(employeeMapper).toEntity(request);
        verify(employeeMapper).toResponse(saved);
    }

    @Test
    void create_throwsDuplicateResourceExceptionWhenEmailExists() {
        EmployeeRequestDTO request = EmployeeRequestDTO.builder()
                .email("duplicate@example.com")
                .build();
        when(employeeRepository.existsByEmail("duplicate@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> employeeService.create(request));
        verify(employeeRepository).existsByEmail("duplicate@example.com");
        verify(employeeMapper, never()).toEntity(any());
        verify(employeeRepository, never()).save(any());
    }

    private Employee buildEmployee(Long id, String firstName, String lastName, String email) {
        return Employee.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .position("Position")
                .department("Department")
                .salary(BigDecimal.valueOf(50000))
                .build();
    }

    private EmployeeResponseDTO buildResponse(Long id, String firstName, String lastName, String email) {
        return EmployeeResponseDTO.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .position("Position")
                .department("Department")
                .salary(BigDecimal.valueOf(50000))
                .build();
    }
}
