package org.spring.zuul.server.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;

/**
 * 安全验证失败、接口调用异常后统一调用此接口返回
 * 异常或错误信息(JSON)
 * @author oyyl
 * @since 2018/11/28
 *
 */
@RestController
@RequestMapping("/api/app")
public class ZuulErrorHandlerController {

	@RequestMapping(path="/error", produces="application/json; charset=utf-8")
	public Object error(HttpServletRequest request){
		
		JSONObject obj = new JSONObject();
		// 异常编码
		obj.put("status_code", request.getAttribute("javax.servlet.error.status_code"));
		// 异常详细信息
		//obj.put("exception", request.getAttribute("javax.servlet.error.exception"));
		// 异常信息
		obj.put("message", request.getAttribute("javax.servlet.error.message"));
		// 获取请求参数返回
		Map<String, String[]> ps = request.getParameterMap();
		for(Map.Entry<String,String[]> entry: ps.entrySet()){
			obj.put(entry.getKey(), entry.getValue());
		}
		return obj;
	}
}
