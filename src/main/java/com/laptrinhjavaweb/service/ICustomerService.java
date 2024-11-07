package com.laptrinhjavaweb.service;

import com.laptrinhjavaweb.dto.AssignmentCustomerDTO;
import com.laptrinhjavaweb.dto.CustomerDTO;
import com.laptrinhjavaweb.dto.request.CustomerSearchRequest;
import com.laptrinhjavaweb.dto.request.TransactionRequest;
import com.laptrinhjavaweb.dto.response.CustomerSearchResponse;
import com.laptrinhjavaweb.dto.response.StaffResposeDTO;
import com.laptrinhjavaweb.dto.response.TransactionReponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ICustomerService {
    List<CustomerSearchResponse> findByCondition(CustomerSearchRequest request, Pageable pageable);
    int countByCondition(CustomerSearchRequest model);
    CustomerDTO save(CustomerDTO customerDTO);
    CustomerDTO findById(Long id);
    List<StaffResposeDTO> loadStaffByCustomerId(Long customerId);
    void delete(List<Long> customerIds);
    void assignCustomerToStaffs(AssignmentCustomerDTO assignmentCustomerDTO);
    Map<String, String> getAllTransaction();
    void saveTransaction(TransactionRequest transactionRequest);
    List<TransactionReponse> findTransactionByCustomerId(Long customerId);

}
