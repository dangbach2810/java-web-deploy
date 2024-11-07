package com.laptrinhjavaweb.api.admin;

import com.laptrinhjavaweb.dto.AssignmentCustomerDTO;
import com.laptrinhjavaweb.dto.CustomerDTO;
import com.laptrinhjavaweb.dto.request.TransactionRequest;
import com.laptrinhjavaweb.dto.response.StaffResposeDTO;
import com.laptrinhjavaweb.service.ICustomerService;
import com.laptrinhjavaweb.service.impl.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/customer")
public class CustomerAPI {

    @Autowired
    private ICustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> saveCustomer(@RequestBody CustomerDTO customerDTO){
        return ResponseEntity.ok(customerService.save(customerDTO));
    }

    @GetMapping("/{customerId}/staffs")
    public ResponseEntity<List<StaffResposeDTO>> loadStaff(@PathVariable Long customerId){
        return ResponseEntity.ok(customerService.loadStaffByCustomerId(customerId));
    }

    @PostMapping("/assignment/customer")
    public ResponseEntity<Void> assignmentCustomer(@RequestBody AssignmentCustomerDTO assignmentCustomerDTO){
        customerService.assignCustomerToStaffs(assignmentCustomerDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCustomer(@RequestBody List<Long> customerIds){
        customerService.delete(customerIds);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transaction")
    public ResponseEntity<Void> transactionCustomer(@RequestBody TransactionRequest transactionRequest){
        customerService.saveTransaction(transactionRequest);
        return ResponseEntity.noContent().build();
    }
}
