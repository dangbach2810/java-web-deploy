package com.laptrinhjavaweb.converter;

import com.laptrinhjavaweb.dto.request.TransactionRequest;
import com.laptrinhjavaweb.entity.CustomerEntity;
import com.laptrinhjavaweb.entity.TransactionEntity;
import com.laptrinhjavaweb.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TransactionConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomerRepository customerRepository;

    public TransactionEntity toEntity(TransactionRequest transactionRequest){
        CustomerEntity customerEntity = customerRepository.findById(transactionRequest.getCustomerId()).get();
        TransactionEntity transactionEntity = modelMapper.map(transactionRequest, TransactionEntity.class);
        transactionEntity.setCustomer(customerEntity);
        transactionEntity.setCreatedDate(new Date());
        return transactionEntity;
    }
}
