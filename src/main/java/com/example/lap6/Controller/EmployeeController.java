package com.example.lap6.Controller;

import com.example.lap6.Api.ApiResponse;
import com.example.lap6.Model.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/employee")

public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ArrayList<Employee> getEmployee() {
        return employees;
    }

    @PostMapping("/post")
    public ResponseEntity postEmployee(@Valid @RequestBody Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        employees.add(employee);
        return ResponseEntity.status(201).body(new ApiResponse("Employee added successfully"));
    }

    @PutMapping("/update/{index}")
    public ResponseEntity updateEmployee(@PathVariable int index, @Valid @RequestBody Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        employees.set(index, employee);
        return ResponseEntity.status(201).body(new ApiResponse("Employee updated successfully"));
    }

    @DeleteMapping("/delete/{index}")
    public ResponseEntity deleteEmployee(@PathVariable int index) {
        if (index >= employees.size()) {
            String message = "Employee not found";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        employees.remove(index);
        return ResponseEntity.status(201).body(new ApiResponse("Employee deleted successfully"));
    }

    @GetMapping("/search/{position}")
    public ResponseEntity searchEmployee(@PathVariable String position) {
        if (! position.equalsIgnoreCase("supervisor") && ! position.equalsIgnoreCase("coordinator")) {
            String message = "invalid position";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }

        ArrayList<Employee> employees1 = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getPosition().equalsIgnoreCase(position)) {
                employees1.add(employee);
            }
        }
        return ResponseEntity.status(200).body(new ApiResponse(employees1.toString()));
    }

    @GetMapping("getbyage/{min}/{max}")
    public ResponseEntity getByAge(@PathVariable int min, @PathVariable int max) {
        if (min < 26 || max > 100) {
            String message = "Invalid age";
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        ArrayList<Employee> employees2 = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getAge() >= min && employee.getAge() <= max) {
                employees2.add(employee);
            }
        }
        return ResponseEntity.status(200).body(new ApiResponse(employees2.toString()));
    }

    @PutMapping("/apply/{index}")
    public ResponseEntity applyLeave(@PathVariable int index) {
        if (index>= employees.size()) {
            String message = "Employee not found";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        } else if (employees.get(index).isOnLeave()) {
            String message = "Employee already on leave";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        } else if (employees.get(index).getAnnualLeave() <= 0) {
            String message = "Employee don't have an annual leave ";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }

        employees.get(index).setOnLeave(true);
        employees.get(index).setAnnualLeave(employees.get(index).getAnnualLeave() - 1);
        return ResponseEntity.status(201).body(new ApiResponse("Employee Leave accepted successfully"));
    }


    @GetMapping("/noannualleave")
    public ResponseEntity noAnnualLeave() {
        ArrayList<Employee> employees3 = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getAnnualLeave() == 0) {
                employees3.add(employee);
            }
        }
        return ResponseEntity.status(200).body(new ApiResponse(employees3.toString()));
    }

    @PutMapping("/promote/{index}/{id}")
    public ResponseEntity promoteEmployee(@PathVariable int index, @PathVariable String id) {
        boolean found = false;
        Employee employee = null ;
        if (index>=employees.size()) {
            String message = "Employee Requester not found";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        if (employees.get(index).getPosition().equalsIgnoreCase("coordinator")) {
            String message = "Requester are coordinator, cannot promote employee";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }
        for (Employee e : employees) {
            if (e.getId().equalsIgnoreCase(id)) {
                found = true;
                employee = e;
            }
        }
        if(found) {
            if (employee.getAge() >= 30) {

                if (!employee.isOnLeave()) {
                    if (employee.getPosition().equalsIgnoreCase("supervisor")) {
                        String message = "Employee already supervisor !";
                        return ResponseEntity.status(400).body(new ApiResponse(message));
                    } else {
                        employee.setPosition("supervisor");
                        String message = "Employee promoted successfully !";
                        return ResponseEntity.status(201).body(new ApiResponse(message));
                    }
                } else {
                    String message = "Employee currently on leave !";
                    return ResponseEntity.status(400).body(new ApiResponse(message));
                }

            } else {
                String message = "Age should be less than 30 !";
                return ResponseEntity.status(400).body(new ApiResponse(message));
            }
        }

        else{
            String message = "Employee to Promote not found !";
            return ResponseEntity.status(404).body(new ApiResponse(message));
        }


    }



}
