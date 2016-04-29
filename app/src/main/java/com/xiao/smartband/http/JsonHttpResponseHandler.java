/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package com.xiao.smartband.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Message;


public class JsonHttpResponseHandler extends AsyncHttpResponseHandler {
	protected static final int SUCCESS_JSON_MESSAGE = 100;

	public void onSuccess(JSONObject response) {
	}

	
	public void onSuccess(JSONArray response) {
	}

	
	public void onSuccess(int statusCode, JSONObject response) {
		onSuccess(response);
	}

	
	public void onSuccess(int statusCode, JSONArray response) {
		onSuccess(response);
	}

	public void onFailure(Throwable e, JSONObject errorResponse) {
	}

	public void onFailure(Throwable e, JSONArray errorResponse) {
	}

	@Override
	protected void sendSuccessMessage(int statusCode, String responseBody) {
		try {
			Object jsonResponse = parseResponse(responseBody);
			sendMessage(obtainMessage(SUCCESS_JSON_MESSAGE, new Object[] {
					statusCode, jsonResponse }));
		} catch (JSONException e) {
			sendFailureMessage(e, responseBody);
		}
	}

	@Override
	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case SUCCESS_JSON_MESSAGE:
			Object[] response = (Object[]) msg.obj;
			handleSuccessJsonMessage(((Integer) response[0]).intValue(),
					response[1]);
			break;
		default:
			super.handleMessage(msg);
		}
	}

	protected void handleSuccessJsonMessage(int statusCode, Object jsonResponse) {
		if (jsonResponse instanceof JSONObject) {
			onSuccess(statusCode, (JSONObject) jsonResponse);
		} else if (jsonResponse instanceof JSONArray) {
			onSuccess(statusCode, (JSONArray) jsonResponse);
		} else {
			onFailure(new JSONException("Unexpected type "
					+ jsonResponse.getClass().getName()), (JSONObject) null);
		}
	}

	protected Object parseResponse(String responseBody) throws JSONException {
		Object result = null;
		responseBody = responseBody.trim();
		if (responseBody.startsWith("{", 1) || responseBody.startsWith("[")) {
		//if (responseBody.startsWith("{") || responseBody.startsWith("[")) {
			result = new JSONTokener(responseBody).nextValue();
		}
		if (result == null) {
			result = responseBody;
		}
		return result;
	}

	@Override
	protected void handleFailureMessage(Throwable e, String responseBody) {
		try {
			if (responseBody != null) {
				Object jsonResponse = parseResponse(responseBody);
				if (jsonResponse instanceof JSONObject) {
					onFailure(e, (JSONObject) jsonResponse);
				} else if (jsonResponse instanceof JSONArray) {
					onFailure(e, (JSONArray) jsonResponse);
				} else {
					onFailure(e, responseBody);
				}
			} else {
				onFailure(e, "");
			}
		} catch (JSONException ex) {
			onFailure(e, responseBody);
		}
	}
	
}
