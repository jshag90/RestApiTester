package com.secuve.tester.main;

import org.json.simple.JSONObject;

import com.secuve.tester.util.RestClient;

public class MainController implements Runnable {

	String url = "http://39.115.210.51:8080";

	private int[] temp;
	private RestClient restClient;

	public MainController() {
		
		restClient = new RestClient(url);
		
		temp = new int[100];

		for (int start = 0; start < temp.length; start++) {
			temp[start] = start;
		}
	}

	@Override
	public void run() {
		
		for (int start : temp) {
			long startTime = System.currentTimeMillis();

			try {
				
				//api 정의
				JSONObject sendData = new JSONObject();
				sendData.put("USER_KEY", "11");
				
				String urlAdd = "/SMA/reqmultimodalauth";
				String jsonResult = restClient.post(urlAdd, sendData.toJSONString());
				System.out.println(jsonResult);
			} catch (Exception ie) {
				ie.printStackTrace();
			}
			
			long endTime = System.currentTimeMillis();
			
			System.out.println( "실행 시간 : " + ( endTime - startTime )/1000.0 );
		}
		
		System.out.println("end");
	}

	public static void main(String[] args) {

		MainController ct = new MainController();
		Thread t = new Thread(ct, "thread");

		t.start();
		
		//multi-thread
//		Thread t2 = new Thread(ct, "thread");
//		t2.start();
		
	}

}
