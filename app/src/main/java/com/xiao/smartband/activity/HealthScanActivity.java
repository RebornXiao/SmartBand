package com.xiao.smartband.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.xiao.smartband.MyApplication;
import com.xiao.smartband.R;
import com.xiao.smartband.bluetooth.EBbraceletcom;
import com.xiao.smartband.client.PatientRestClient;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.HealthScan;
import com.xiao.smartband.entity.HealthState;
import com.xiao.smartband.entity.ScandateFlag;
import com.xiao.smartband.entity.UId;
import com.xiao.smartband.http.RequestParams;
import com.xiao.smartband.http.XmlHttpResponseHandler;
import com.xiao.smartband.tools.CommentWays;
import com.xiao.smartband.view.ScanView;
import com.xiao.smartband.view.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HealthScanActivity extends Activity {
	private TitleView titleView;
	private BluetoothDevice device = null;
 //   static private BtDeviceReceiver mReceiver;
 //   private BluetoothAdapter mBtAdapter = 
  //  		BluetoothAdapter.getDefaultAdapter();
	private ModelDao model;
	private int weight = 0 ;
	private Calendar ca;
	private int year,month,day;
	private ScanView view1;
	ArrayList<HealthScan> scans;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_health_scan);
//		setContentView(R.layout.activity_main);
		init();
		//取昨天的数据，要是昨天没运动就全为0,每天只能取一次
		/*boolean flag = model.queryScanFlag(year*10000 + (month+1)*100 +  (day- 1));
		if(flag == true){
			return;
		}*/
	//	json("2014-11-06");
		/*if(MyApplication.CONNETET == true){
			sendCmdHealthScan();	
		}else {
			*//**
			 *重新连接 
			 *//*
			if(MyApplication.CONNETET == false){
				connetBtDeice();
			}
		}*/
	}
	private void init(){
		if(MyApplication.eBbraceletcom == null)
		   MyApplication.eBbraceletcom = new EBbraceletcom();
		model = new ModelDao(getApplicationContext());
		ca = Calendar.getInstance();
		year = ca.get(Calendar.YEAR);
		month=ca.get(Calendar.MONTH);//+1
		day=ca.get(Calendar.DATE);
		titleView = (TitleView)findViewById(R.id.titleview);
		view1 = (ScanView)findViewById(R.id.view1);
		scans = model.HealthScanSearth();
		if(scans.size() >= 10){
			for(int i = 0 ; i < scans.size() - 10; i++ ) {
					 model.deleteHealthScanId(scans.get(i).getId());
			    }
			scans = model.HealthScanSearth();
			}
		if(scans.size() > 0){
		   view1.dealData(scans.size() - 1);
		}
		titleView.setTitle(R.string.health_scan);
		titleView.setbg(R.drawable.health_santitleimg);
		titleView.titleImg(R.drawable.health_scanimg);
		
		
		/**健康扫描应该保存未提交成功日的日期，下次一次提交，最多提交10天的数据.
		 *判断健康扫描的信息是否成功提交至服务器,最好再做个网络判断，没有网络直接不执行 
		 *假如健康扫描存了多次，我们就取最后一次
		 */
		if(CommentWays.isWifi(getApplicationContext()) == true
	               || CommentWays.is3G(getApplicationContext()) == true){
			ArrayList<ScandateFlag> flags = model.scanDateSearth();
			if(flags.size() > 0){
			   for(int i = 0; i < flags.size(); i++){
				   json(flags.get(i).getDate(),flags.get(i).getId());
			   }
			}
		}
		
		titleView.setBack(R.drawable.steps_back, new TitleView.onBackLister() {
			
			@Override
			public void onClick(View button) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		titleView.right(R.string.history, new TitleView.onSetLister() {
			
			@Override
			public void onClick(View button) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), HealthScanHistory.class);
			//	MyApplication.activityStuts = 6;
				startActivity(intent);
			}
		});
	}
	private void json(String date,final int id){
		/**
		 *判断有几条信息,然后new 几个JSONObect,每个JSONObect需要被添加到JSONArray 数组里 搞定. 
		 */
		ArrayList<UId> uids = model.uIdSearth();
		if(uids.size() == 0) return;
		String op = "10002";
		String info = "";
		String uId = uids.get(0).getuId();
		JSONArray array = new JSONArray();
		JSONObject object2 = new JSONObject();
		HealthScan healthScan = model.queryHealthyScan(date);//year+"-"+(month+1)+"-"+(day-1)
		if(healthScan == null) return;
		try {
			object2.put("steps", healthScan.getHSsteps());
			object2.put("sportTime", healthScan.getHSstepTime());
			object2.put("calories", healthScan.getHSkaloria());
			//object2.put("sleepTime", healthScan.get);
			object2.put("deepTime", healthScan.getHSsleepDeep());
			object2.put("heartRate", healthScan.getHSheart());
			

			object2.put("stepsPer", healthScan.getHSstepsPer());
			object2.put("deepTimePer", healthScan.getHSsleepDeeper());
			object2.put("heartRatePer", healthScan.getHSheartRatePer());
			if(healthScan.getHShealthScore().equals(R.string.poor))
				object2.put("score", "0");
		     else object2.put("score", healthScan.getHShealthScore());
			
			object2.put("time", healthScan.getHShealthDate());
			
			
			List<HealthState> states = model.healthStateSearth();
			if(states.size() > 0){
			   for(int i = 0 ;i<states.size(); i++){
				   if(date != null && states.get(i).getDate().equals(date)){
					   object2.put("higPressure", states.get(i).getBloodPressure());
					   object2.put("lowPressure", states.get(i).getBloodPressureLow());
					   object2.put("befmeaBlood", states.get(i).getBloodSugar());
					   object2.put("afterBlood", states.get(i).getBloodSugarAfter());
					   break;
				   }
			   }
			}
			
			

			info = object2.toString();
			array.put(object2);
		    
		  /*JSONObject object3 = new JSONObject();
		   object3.put("steps", "10000");
		   object3.put("sportTime", "60");
		   object3.put("calories", "121.2");
		   object3.put("sleepTime", "8.1");
		   object3.put("deepTime", "6.1");
		   object3.put("heartRate", "80");
		   object3.put("stepsPer", "80");
		   object3.put("deepTimePer", "70");
		   object3.put("heartRatePer", "100");
		   object3.put("score", "80");
		   object3.put("time", "2014-11-06");
		   info = object3.toString();
		   array.put(object3);*/
		
		   
		   
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("op", op);
		map2.put("info", array.toString());
		map2.put("uId", uId);
		RequestParams params = new RequestParams(map2);
		
		PatientRestClient.post("synData", params, new XmlHttpResponseHandler(){
			@Override
			public void onFinish() {
				super.onFinish();
				Log.i("info", "onFinish");
			}
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				try {
				String ack = response.getString("ack");
				String op = response.getString("op");
				if(ack.equals("0") && op.equals("10002")){
					MyApplication.ways.ToastShow(getApplicationContext(), "提交成功");
					String err = response.getString("err");
					String info = response.getString("info");
					String uId = response.getString("uId");

					//上传完数据，删除表
					model.deleteScanDateFlagId(id);
				}else if(ack.equals("1")){
					//MyApplication.ways.ToastShow(getApplicationContext(), "提交失败");
					
				}else if(ack.equals("6")){
					//MyApplication.ways.ToastShow(getApplicationContext(), "用户不存在");	
				}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.i("info", "sucess");
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				//没有网络，会跳到此处.
				
				Log.i("info", "false");
			}
		});
		
	}
/*	public class BtDeviceReceiver extends BroadcastReceiver { 
//	    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String action = intent.getAction();
	           // status = 1;
	            // When discovery finds a device
	            //Log.i("info", "扫描注册广播="+status);
	            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            	BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	                String strName = dev.getName();
	     //           MyApplication.address = dev.getAddress();
	               // if(dev.getName().equals("SH11") && device == null){//"AET-R121""BTSPP""Dp"
	                if(dev.getAddress().replace(":", "").equals(MyApplication.mac) && device == null){//"AET-R121""BTSPP""Dp"
	                	device = dev;

	                	MyApplication.eBbraceletcom.PairDevice(dev);//设备配对
	                }       
	                
	            } 
	           if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
	        	   int state = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1);
	        	   int reason = intent.getIntExtra( "android.bluetooth.device.extra.REASON", -1);

	        	   MyApplication.eBbraceletcom.ConnectDevice(true);//调用连接方法
	            }
	            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action) && device == null) {
	            	MyApplication.ways.ToastShow(getApplicationContext(), "没有搜索到任何设备");
	            	if(mReceiver != null) HealthScanActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播
	            	mReceiver = null;
	           }
	        }
	    }*/
}
