package com.laptrinhjavaweb.controller.admin;

import com.laptrinhjavaweb.constant.SystemConstant;
import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.dto.request.BuildingSearchRequest;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.service.IBuildingService;
import com.laptrinhjavaweb.service.IUserService;
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

@Controller(value = "buildingControllerOfAdmin")
public class BuildingController {

	private final IBuildingService buildingService;
	private final IUserService userService;
	private final MessageUtils messageUtils;

	@Autowired
	public BuildingController(IBuildingService buildingService, IUserService userService, MessageUtils messageUtils){
		this.buildingService = buildingService;
		this.userService = userService;
		this.messageUtils = messageUtils;
	}

	@RequestMapping(value = "/admin/building/list", method = RequestMethod.GET)
	public ModelAndView buildingList(@ModelAttribute("modelSearch") BuildingSearchRequest model, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("admin/building/list");//view tra ve lay theo pakage views
		List<BuildingSearchResponse> buildings = buildingService.findByCondition(model ,PageRequest.of(model.getPage() - 1, model.getMaxPageItems()));

		model.setListResult(buildings);
		model.setTotalItems(buildingService.countByCondition(model));
		model.setTotalPage((int) Math.ceil((double) model.getTotalItems()/model.getMaxPageItems()));

		mav.addObject(SystemConstant.MODEL, model);
		mav.addObject(SystemConstant.STAFF_MAP, userService.getStaffMaps());
		mav.addObject(SystemConstant.DISTRICT_MAP, buildingService.getAllDistricts());
		mav.addObject(SystemConstant.BUILDING_TYPE_MAP, buildingService.getAllRentTypes());

		return mav;
	}

	@RequestMapping(value = "/admin/building/edit", method = RequestMethod.GET)
	public ModelAndView createBuilding(@RequestParam(name = "id", required = false) Long buildingId) {
		ModelAndView mav = new ModelAndView("admin/building/edit");//view tra ve lay theo pakage views
		BuildingDTO buildingDTO = new BuildingDTO();

		mav.addObject(SystemConstant.MODEL, buildingDTO);
		mav.addObject(SystemConstant.DISTRICT_MAP, buildingService.getAllDistricts());
		mav.addObject(SystemConstant.BUILDING_TYPE_MAP, buildingService.getAllRentTypes());

		return mav;
	}

	@RequestMapping(value = "/admin/building/edit/{id}", method = RequestMethod.GET)
	public ModelAndView buildingEdit(@PathVariable(value = "id", required = false) Long buildingId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("admin/building/edit");//view tra ve lay theo pakage views

		BuildingDTO buildingDTO = buildingService.findById(buildingId);
		buildingDTO.setId(buildingId);
		mav.addObject(SystemConstant.BUILDING_ID, buildingId);
		mav.addObject(SystemConstant.MODEL, buildingDTO);
		mav.addObject(SystemConstant.DISTRICT_MAP, buildingService.getAllDistricts());
		mav.addObject(SystemConstant.BUILDING_TYPE_MAP, buildingService.getAllRentTypes());

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
