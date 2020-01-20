package com.secuve.tester.service;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.secuve.tester.util.RestClient;

public class SecuSignTestService {
	
	public static void callAuthSign(RestClient restClient, String userkey, ArrayList reg_sign_ids,
			String auth_sign_name, String authSignData, String authThreshold, String orientation,
			String logLevel) {
		
		JSONObject sendData = new JSONObject();
		
		String authSignId = System.currentTimeMillis()+userkey;
		
		sendData.put("USER_KEY", userkey);
		sendData.put("REG_SIGN_IDS", reg_sign_ids);
		sendData.put("AUTH_SIGN_ID", authSignId);
		sendData.put("AUTH_SIGN_NAME", auth_sign_name);
		sendData.put("AUTH_SIGN_DATA", authSignData);
		sendData.put("AUTH_THRESHOLD", authThreshold);
		sendData.put("ORIENTATION", orientation);
		sendData.put("LOG_LEVEL", logLevel);
		
		String urlAdd = "/ssas/signaudit/authsign";
		String jsonResult = restClient.post(urlAdd, sendData.toJSONString());
		
		System.out.println(jsonResult);
	}



	public static void callRegSign(RestClient restClient, JSONObject sendData, String userkey,
			String signName, String orientation, String signData) {
		
		String regSignId = System.currentTimeMillis()+userkey;
		sendData.put("USER_KEY", userkey);
		sendData.put("SIGN_ID", regSignId);
		sendData.put("SIGN_NAME", signName);
		sendData.put("ORIENTATION", orientation);
		sendData.put("SIGN_DATA", signData);

		String urlAdd = "/ssas/signaudit/regsign";
		String jsonResult = restClient.post(urlAdd, sendData.toJSONString());
		
		System.out.println(jsonResult);
	}
	
	

	public static void callRegDevice(RestClient restClient, JSONObject sendData, String userkey, String deviceId,
			String deviceInfo, String deviceType) {
		sendData.put("USER_KEY", userkey);
		sendData.put("DEVICE_ID", deviceId);
		sendData.put("DEVICE_INFO", deviceInfo);
		sendData.put("DEVICE_TYPE", deviceType);

		String urlAdd = "/ssas/signaudit/regdevice";
		String jsonResult = restClient.post(urlAdd, sendData.toJSONString());
		int count=1;
		
		System.out.println((count++)+jsonResult);
	}

}
