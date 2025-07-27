package com.mvctest.services;

import com.mvctest.dto.EmployeeDTO;
import com.mvctest.entityies.EmployeeEntity;
import com.mvctest.repositoryies.EmployeeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepo empRepo;
    private final ModelMapper mapper;

    public EmployeeService(EmployeeRepo empRepo, ModelMapper mapper) {
        this.empRepo = empRepo;
        this.mapper = mapper;
    }

    public Optional<EmployeeDTO> getEmployeeById(Long id){
        /*EmployeeEntity foundEmp = empRepo.findById(id).orElse(null);
        return mapper.map(foundEmp, EmployeeDTO.class);*/
        Optional<EmployeeEntity> empEntityOptional = empRepo.findById(id);
        return empEntityOptional.map(empDTO -> mapper.map(empDTO, EmployeeDTO.class));
    }

    public List<EmployeeDTO> getAllEmployee() {
        List<EmployeeEntity> allEmployee = empRepo.findAll();
        List<EmployeeDTO> toReturnEmployees = allEmployee.stream()
                .map(emp -> mapper.map(emp, EmployeeDTO.class))
                .collect(Collectors.toList());
        return toReturnEmployees;
    }

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        EmployeeEntity toSaveEntity = mapper.map(employeeDTO, EmployeeEntity.class);
        EmployeeEntity savedEmployee = empRepo.save(toSaveEntity);
        EmployeeDTO toReturnEmp = mapper.map(savedEmployee, EmployeeDTO.class);
        return toReturnEmp;
    }

    public EmployeeDTO updateEmpById(Long empId, EmployeeDTO employeeDTO) {
        EmployeeEntity toUpdateEmp = mapper.map(employeeDTO, EmployeeEntity.class);
        boolean isEmpPresent = checkIfEmpExistById(empId);
        if(!isEmpPresent) return null;
        toUpdateEmp.setId(empId);
        EmployeeEntity savedEmp = empRepo.save(toUpdateEmp);
        return mapper.map(savedEmp, EmployeeDTO.class);
    }
    public boolean checkIfEmpExistById(Long empId){
        return empRepo.existsById(empId);
    }

    public boolean deleteEmpById(Long empId) {
        boolean exist =checkIfEmpExistById(empId);
        if(!exist) return false;
        empRepo.deleteById(empId);
        return true;
    }

    public EmployeeDTO updatePartialByEmpId(Long empId, Map<String, Object> updates) {
        boolean exists = checkIfEmpExistById(empId);
        if(!exists) return null;
        EmployeeEntity employeeEntity = empRepo.findById(empId).get();
        updates.forEach((field,value)->{
            Field fieldToUpdate = ReflectionUtils.findField(EmployeeEntity.class, field);
            fieldToUpdate.setAccessible(true);
            ReflectionUtils.setField(fieldToUpdate,employeeEntity,value);
        });

        EmployeeEntity updatedEmployee = empRepo.save(employeeEntity);
        return mapper.map(updatedEmployee, EmployeeDTO.class);

    }
}
