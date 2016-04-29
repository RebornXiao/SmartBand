package com.xiao.smartband.client;


import com.xiao.smartband.http.AsyncHttpClient;
import com.xiao.smartband.http.AsyncHttpResponseHandler;
import com.xiao.smartband.http.RequestParams;

public class PatientRestClient {
//	public static final String BASE_URL = "http://192.168.123.100/device/index.php/Index/";//113.10.131.16
	public static final String BASE_URL = "http://szrg168.com/index.php/Index/";//113.10.131.16
//	public static final String BASE_URL = "http://192.168.1.100/device/index.php/Index/";

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params,AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	private static String getAbsoluteUrl(String relativeUrl) {
			return BASE_URL + relativeUrl; 		
	}
}