package com.laptrinhjavaweb.controller.admin;

import com.laptrinhjavaweb.constant.SystemConstant;
import com.laptrinhjavaweb.dto.CustomerDTO;
import com.laptrinhjavaweb.dto.request.CustomerSearchRequest;
import com.laptrinhjavaweb.dto.response.CustomerSearchResponse;
import com.laptrinhjavaweb.dto.response.TransactionReponse;
import com.laptrinhjavaweb.service.ICustomerService;
import com.laptrinhjavaweb.service.IUserService;
import com.laptrinhjavaweb.service.impl.CustomerService;
import com.laptrinhjavaweb.service.impl.UserService;
import com.laptrinhjavaweb.utils.MessageUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Controller(value = "customerControllerOfAdmin")
public class CustomerController {

    private final IUserService userService;

    private final ICustomerService customerService;

    private final MessageUtils messageUtils;

    @Autowired
    public CustomerController(UserService userService, CustomerService customerService, MessageUtils messageUtils) {
        this.userService = userService;
        this.customerService = customerService;
        this.messageUtils = messageUtils;
    }

    @RequestMapping(value = "/admin/customer/list", method = RequestMethod.GET)
    public ModelAndView customerList(@ModelAttribute("modelSearch")CustomerSearchRequest model, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("admin/customer/list");//view tra ve lay theo pakage views
        model.setTableId("customerList");
        List<CustomerSearchResponse> customers = customerService.findByCondition(model, PageRequest.of(model.getPage() - 1, model.getMaxPageItems()));
        model.setListResult(customers);
        model.setTotalItems(customerService.countByCondition(model));
        model.setTotalPage((int) Math.ceil((double) model.getTotalItems()/model.getMaxPageItems()));
        mav.addObject(SystemConstant.STAFF_MAP, userService.getStaffMaps());
        mav.addObject(SystemConstant.MODEL, model);
        return mav;
    }

    @RequestMapping(value = "/admin/customer/edit", method = RequestMethod.GET)
    public ModelAndView createCustomer(@RequestParam(name = "id", required = false) Long buildingId) {
        ModelAndView mav = new ModelAndView("admin/customer/edit");//view tra ve lay theo pakage views
        CustomerDTO customerDTO = new CustomerDTO();
        mav.addObject(SystemConstant.MODEL, customerDTO);
        return mav;
    }

    @RequestMapping(value = "/admin/customer/edit/{id}", method = RequestMethod.GET)
    public ModelAndView buildingEdit(@PathVariable(value = "id", required = false) Long customerId, HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("admin/customer/edit");//view tra ve lay theo pakage views
        CustomerDTO customerDTO = customerService.findById(customerId);
        List<TransactionReponse> transactions = customerService.findTransactionByCustomerId(customerId);
        mav.addObject(SystemConstant.TRANSACTIONS, transactions);
        mav.addObject(SystemConstant.TRANSACTION_MAP, customerService.getAllTransaction());
        mav.addObject(SystemConstant.MODEL, customerDTO);

        initMessageResponse(mav, request);
        return mav;
    }
    private void initMessageResponse(ModelAndView mav, HttpServletRequest request){
        String message = request.getParameter(SystemConstant.MESSAGE);
        if(StringUtils.isNotEmpty(message)){
            Map<String, String> messageMap = messageUtils.getMessage(message);
            mav.addObject(SystemConstant.ALERT, messageMap.get(SystemConstant.ALERT));
            mav.addObject(SystemConstant.MESSAGE_RESPONSE, messageMap.get(SystemConstant.MESSAGE));
        }
    }
}
