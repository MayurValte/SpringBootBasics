package com.mvctest.controllers;

import com.mvctest.dto.EmployeeDTO;
import com.mvctest.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/getEmployeeById/{empId}")
    public ResponseEntity<EmployeeDTO> getEmpById(@PathVariable(name = "empId") Long id){
        Optional<EmployeeDTO> employeeByIdOptional = service.getEmployeeById(id);
       return employeeByIdOptional.map(employeeDTO -> ResponseEntity.ok(employeeDTO))
                .orElseThrow(()-> new NoSuchElementException("Employee not found"));
    }

    @GetMapping(path = "/getAllEmployee")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployee(){
        return ResponseEntity.ok(service.getAllEmployee());
    }

    @PostMapping("/saveEmpoyee")
    public ResponseEntity<EmployeeDTO> saveEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        EmployeeDTO savedEmp = service.saveEmployee(employeeDTO);
        return new ResponseEntity<>(savedEmp, HttpStatus.CREATED);
    }

    @PutMapping("/updateEmpById/{empId}")
    public ResponseEntity<EmployeeDTO> updateEmpById(@RequestBody EmployeeDTO employeeDTO,@PathVariable(name = "empId") Long empId){
        EmployeeDTO updatedEmp = service.updateEmpById(empId, employeeDTO);
        if(updatedEmp==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedEmp);
    }

    @DeleteMapping("/deleteEmpById/{empId}")
    public ResponseEntity<Boolean> deleteEmpById(@PathVariable(name = "empId") Long empId){
        boolean deleted = service.deleteEmpById(empId);
        if(deleted) return ResponseEntity.ok(true);
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/updatePartialEmpById/{empId}")
    public ResponseEntity<EmployeeDTO> updatePartialEmpById(@RequestBody Map<String,Object> updates,@PathVariable(name = "empId") Long empId){
        EmployeeDTO patchedEmp = service.updatePartialByEmpId(empId, updates);
        if(patchedEmp==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(patchedEmp);
    }

}
