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

import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class XmlHttpResponseHandler extends AsyncHttpResponseHandler {
	protected static final int SUCCESS_XML_MESSAGE = 100;

	public void onSuccess(JSONObject response) {
		Log.i("info", "xml sucess=");
	}
	
	
	public void onSuccess(JSONArray response) {
		Log.i("info", "xml sucess=");
	}

	public void onFinish() {
	}
	
	public void onSuccess(int statusCode, JSONObject response) {
		onSuccess(response);
	}
	
	
	public void onSuccess(int statusCode, JSONArray response) {
		onSuccess(response);
	}

	public void onFailure(Throwable e, JSONObject errorResponse) {
		Log.i("info", "xml onFailure=");
	}

	public void onFailure(Throwable e, JSONArray errorResponse) {
		Log.i("info", "xml onFailure=");
	}
	@Override
	protected void sendSuccessMessage(int statusCode, String responseBody) {
		try {
			Object xmlResponse = parseResponse(responseBody);
			sendMessage(obtainMessage(SUCCESS_XML_MESSAGE, new Object[] {statusCode, xmlResponse }));
		} catch (XmlPullParserException e) {
			sendFailureMessage(e, responseBody);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case SUCCESS_XML_MESSAGE:
			Object[] response = (Object[]) msg.obj;
			handleSuccessJsonMessage(((Integer) response[0]).intValue(),
					response[1]);
			break;
		default:
			super.handleMessage(msg);
		}
	}

	protected void handleSuccessJsonMessage(int statusCode, Object xmlResponse) {
		boolean flag = xmlResponse instanceof JSONObject;
		Log.i("info", "xmlResponse instanceof JSONObject="+flag);
		if (xmlResponse instanceof JSONObject) {
			onSuccess(statusCode, (JSONObject) xmlResponse);
		} else if (xmlResponse instanceof JSONArray) {
			onSuccess(statusCode, (JSONArray) xmlResponse);
		} else {
			onFailure(new XmlPullParserException("Unexpected type "
					+ xmlResponse.getClass().getName()), (JSONObject) null);
			Log.i("info", "failure="+new XmlPullParserException("Unexpected type "
					+ xmlResponse.getClass().getName()));
		}
	}

	/*protected Object parseResponse(String responseBody) throws XmlPullParserException, JSONException, SAXException, IOException, ParserConfigurationException {
		Object result = null;
		responseBody = responseBody.trim();
		if (responseBody.startsWith("<") ) {
			//result = new JSONTokener(responseBody).nextValue();
			//result = new XMLTokener(responseBody).nextValue();
			//result = new HttpXml(responseBody);
			result=new HttpXml().parse(responseBody);
			
		}
		if (result == null) {
			result = responseBody;
		}
		return result;
	}*/
	protected Object parseResponse(String responseBody) throws XmlPullParserException, JSONException, SAXException, IOException, ParserConfigurationException {
		Object result = null;
		responseBody = responseBody.trim();
		if (responseBody.startsWith("{") ) {
			//result = new JSONTokener(responseBody).nextValue();
			//result = new XMLTokener(responseBody).nextValue();
			//result = new HttpXml(responseBody);
			//result=new HttpXml().parse(responseBody);
			result = new JSONTokener(responseBody).nextValue();
		}
		if (responseBody.startsWith("{", 1)) {
			result = new JSONTokener(responseBody).nextValue();
		}
		if (result == null) {
			result = responseBody;
		}
		Log.i("info", "result="+result.toString());
		return result;
	}
	@Override
	protected void handleFailureMessage(Throwable e, String responseBody) {
		try {
			if (responseBody != null) {
				Object xmlResponse = parseResponse(responseBody);
				if (xmlResponse instanceof JSONObject) {
					onFailure(e, (JSONObject) xmlResponse);
				} else if (xmlResponse instanceof JSONArray) {
					onFailure(e, (JSONArray) xmlResponse);
				} else {
					onFailure(e, responseBody);
				}
			} else {
				onFailure(e, "");
			}
		} catch (XmlPullParserException ex) {
			onFailure(e, responseBody);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e2) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e4) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
