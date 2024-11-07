package com.laptrinhjavaweb.utils;

import com.laptrinhjavaweb.constant.SystemConstant;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class MessageUtils {

	public Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		switch (message){
			case SystemConstant.UPDATE_SUCCESS:
				result.put(SystemConstant.MESSAGE, "Cập nhật tòa nhà thành công!");
				result.put(SystemConstant.ALERT, SystemConstant.SUCCESS);
				break;
			case SystemConstant.INSERT_SUCCESS:
				result.put(SystemConstant.MESSAGE, "Thêm tòa nhà thành công!");
				result.put(SystemConstant.ALERT, SystemConstant.SUCCESS);
				break;
			case SystemConstant.DELETE_SUCCESS:
				result.put(SystemConstant.MESSAGE, "Xóa tòa nhà thành công!");
				result.put(SystemConstant.ALERT, SystemConstant.SUCCESS);
				break;
			case SystemConstant.ASSIGN_SUCCESS:
				result.put(SystemConstant.MESSAGE, "Giao tòa tòa nhà cho nhân viên quản lý thành công!");
				result.put(SystemConstant.ALERT, SystemConstant.SUCCESS);
				break;
			case SystemConstant.ERROR_SYSTEM:
				result.put(SystemConstant.MESSAGE, "Đã xảy ra lỗi hệ thống, vui lòng thử lại sau!");
				result.put(SystemConstant.ALERT, SystemConstant.DANGER);
				break;
		}
		return result;
	}
}
