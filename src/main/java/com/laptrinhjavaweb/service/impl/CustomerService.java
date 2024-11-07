package com.laptrinhjavaweb.service.impl;

import com.laptrinhjavaweb.constant.SystemConstant;
import com.laptrinhjavaweb.converter.CustomerConverter;
import com.laptrinhjavaweb.converter.TransactionConverter;
import com.laptrinhjavaweb.converter.UserConverter;
import com.laptrinhjavaweb.dto.AssignmentCustomerDTO;
import com.laptrinhjavaweb.dto.CustomerDTO;
import com.laptrinhjavaweb.dto.request.CustomerSearchRequest;
import com.laptrinhjavaweb.dto.request.TransactionRequest;
import com.laptrinhjavaweb.dto.response.CustomerSearchResponse;
import com.laptrinhjavaweb.dto.response.StaffResposeDTO;
import com.laptrinhjavaweb.dto.response.TransactionReponse;
import com.laptrinhjavaweb.entity.CustomerEntity;
import com.laptrinhjavaweb.entity.TransactionEntity;
import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.enums.TransactionsEnum;
import com.laptrinhjavaweb.repository.CustomerRepository;
import com.laptrinhjavaweb.repository.TransactionRepository;
import com.laptrinhjavaweb.repository.UserRepository;
import com.laptrinhjavaweb.security.utils.SecurityUtils;
import com.laptrinhjavaweb.service.ICustomerService;
import com.laptrinhjavaweb.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerConverter customerConverter;
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final TransactionConverter transactionConverter;
    private final TransactionRepository transactionRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerConverter customerConverter, UserRepository userRepository, UserConverter userConverter, TransactionConverter transactionConverter, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.customerConverter = customerConverter;
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.transactionConverter = transactionConverter;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<CustomerSearchResponse> findByCondition(CustomerSearchRequest request, Pageable pageable) {
        if(SecurityUtils.getAuthorities().contains("ROLE_STAFF")){
            Long staffId = SecurityUtils.getPrincipal().getId();
            request.setStaffId(staffId);
        }

        List<CustomerEntity> customers = customerRepository.findByCondition(customerConverter.toBuilder(request), pageable);

        return customers.stream()
                .map(customerConverter::toSearchResponse)
                .collect(Collectors.toList());
    }

    @Override
    public int countByCondition(CustomerSearchRequest request) {
        if(SecurityUtils.getAuthorities().contains("ROLE_STAFF")){
            Long staffId = SecurityUtils.getPrincipal().getId();
            request.setStaffId(staffId);
        }
        return customerRepository.countByCondition(customerConverter.toBuilder(request));
    }

    @Override
    @Transactional
    public CustomerDTO save(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = customerConverter.toEntity(customerDTO);
        return customerConverter.toDTO(customerRepository.save(customerEntity));
    }

    @Override
    public CustomerDTO findById(Long id) {
        return customerConverter.toDTO(customerRepository.findById(id).get());
    }

    @Override
    public List<StaffResposeDTO> loadStaffByCustomerId(Long customerId) {
        List<UserEntity> allStaffs = userRepository.findByStatusAndRoles_Code(SystemConstant.ACTIVE_STATUS, "STAFF");
        return allStaffs.stream().map(staff ->
        {
            StaffResposeDTO staffRespose = userConverter.toAssignmentStaffResponseDTO(staff);

            for(CustomerEntity customerEntity : staff.getCustomers())
            {
                if(customerId.equals(customerEntity.getId())){
                    staffRespose.setChecked("checked");
                    break;
                }
            }
            return staffRespose;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(List<Long> customerIds) {
        if(customerIds != null && !customerIds.isEmpty()){
            Long count = customerRepository.countByIdIn(customerIds);
            if(count != customerIds.size()){
                throw new NotFoundException("Customer not found!");
            }
            transactionRepository.deleteByCustomerIdIn(customerIds);
            customerRepository.deleteByIdIn(customerIds);
        }
    }

    @Override
    public void assignCustomerToStaffs(AssignmentCustomerDTO assignmentCustomerDTO) {
        Long customerId = assignmentCustomerDTO.getCustomerId();
        List<Long> staffIdsRequest = assignmentCustomerDTO.getStaffIds();

        CustomerEntity foundCustomer = Optional.of(customerRepository.findById(customerId)).get()
                .orElseThrow(() -> new NotFoundException("Building not found"));
        List<UserEntity> foundUsers = userRepository.findByIdIn(staffIdsRequest);

        foundCustomer.setUsers(foundUsers);

        customerRepository.save(foundCustomer);
    }

    @Override
    public Map<String, String> getAllTransaction() {
        return Arrays.stream(TransactionsEnum.values()).collect(Collectors.toMap(Enum::toString, TransactionsEnum::getTransactionValue));
    }

    @Override
    @Transactional
    public void saveTransaction(TransactionRequest transactionRequest) {
        TransactionEntity transactionEntity = transactionConverter.toEntity(transactionRequest);
        if(!transactionEntity.getNote().isEmpty()){
            transactionRepository.save(transactionEntity);
        }
    }

    @Override
    public List<TransactionReponse> findTransactionByCustomerId(Long customerId) {
        CustomerEntity customerEntity = customerRepository.findById(customerId).get();
        List<TransactionEntity> transactions = customerEntity.getTransactions();
        return transactions.stream().map(transaction -> {
            TransactionReponse transactionReponse = new TransactionReponse();
            transactionReponse.setCode(transaction.getCode());
            transactionReponse.setCreatedDate(transaction.getCreatedDate());
            transactionReponse.setCustomerId(transaction.getCustomer().getId());
            transactionReponse.setNote(transaction.getNote());
            return transactionReponse;
        }).collect(Collectors.toList());
    }
}
