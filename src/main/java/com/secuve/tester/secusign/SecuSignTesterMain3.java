package com.secuve.tester.secusign;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.secuve.tester.service.SecuSignTestService;
import com.secuve.tester.util.RestClient;

public class SecuSignTesterMain3 implements Runnable {

	String url = "https://www.secuvecert1.com:8443";
//	String url = "http://172.16.0.215:8080";
//	String url = "https://awsus.quicksign.co.kr:8443";

	private int[] temp;
	private RestClient restClient;

	public SecuSignTesterMain3() {
		
		restClient = new RestClient(url);
		
		temp = new int[1000];

		for (int start = 0; start < temp.length; start++) {
			temp[start] = start;
		}
	}

	@Override
	public void run() {
		for (int start : temp) {
			
			try {
				SecuSignTestService tc = new SecuSignTestService();

				JSONObject sendData = new JSONObject();
				String userkey = "5273";
				String orientation = "1";
				////////////////////////////
				
//				String deviceId = "12345";
//				String deviceInfo = "deviceInfo";
//				String deviceType = "ios";
//
//				tc.callRegDevice(restClient, sendData, userkey, deviceId, deviceInfo, deviceType);
				////////////////////
//				String signName = "signName";
//				String signData = "[(304,634,9)(320,629,26)(376,621,43)(456,611,60)(544,600,78)(622,590,95)(681,582,112)(726,574,130)(749,569,147)][(522,435,18)(520,454,35)(518,502,53)(519,563,70)(523,623,87)(527,674,104)(533,714,122)(539,746,140)]";
//				
//				tc.callRegSign(restClient, sendData, userkey, signName, orientation, signData);
				////////////////////
				ArrayList reg_sign_ids = new ArrayList<String>();
				reg_sign_ids.add("15628270782305273");
//				reg_sign_ids.add("156282578970401050655273");
//				reg_sign_ids.add("156282580833801050655273");
		
				String auth_sign_name = "authSignName";
				String authSignData = "[(376,621,43)(456,611,60)(544,600,78)(622,590,95)(681,582,112)(726,574,130)(749,569,147)][(522,435,18)(520,454,35)(518,502,53)(519,563,70)(523,623,87)(527,674,104)(533,714,122)(539,746,140)]";
				String authThreshold = "90";
				String logLevel = "debug";
				 System.out.print(start+" "); tc.callAuthSign(restClient, userkey, reg_sign_ids, auth_sign_name, authSignData, authThreshold, orientation, logLevel);

			} catch (Exception ie) {
				ie.printStackTrace();
			}

		}
	}

	public static void main(String[] args) {

		SecuSignTesterMain3 ct = new SecuSignTesterMain3();
		Thread t = new Thread(ct, "thread");

		t.start();
	}

}
