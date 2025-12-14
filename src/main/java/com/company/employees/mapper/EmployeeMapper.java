package com.company.employees.mapper;

import com.company.employees.dto.EmployeeRequestDTO;
import com.company.employees.dto.EmployeeResponseDTO;
import com.company.employees.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeResponseDTO toResponse(Employee entity);

    Employee toEntity(EmployeeRequestDTO dto);

    void updateEntityFromDto(EmployeeRequestDTO dto, @MappingTarget Employee entity);
}
