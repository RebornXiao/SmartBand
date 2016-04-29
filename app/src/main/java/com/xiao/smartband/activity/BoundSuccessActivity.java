package com.xiao.smartband.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.xiao.smartband.MyApplication;
import com.xiao.smartband.R;
import com.xiao.smartband.bluetooth.EBEventHander;
import com.xiao.smartband.bluetooth.EBbraceletcom;
import com.xiao.smartband.client.PatientRestClient;
import com.xiao.smartband.http.RequestParams;
import com.xiao.smartband.http.XmlHttpResponseHandler;
import com.xiao.smartband.protocal.CmdProtocol;
import com.xiao.smartband.tools.CommentWays;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BoundSuccessActivity extends Activity {
	private TextView tvexpirence;
	private BluetoothDevice device = null;
    static private BtDeviceReceiver mReceiver;
    private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
   // private EBbraceletcom eBbracom;
	private int Times = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_bound);
		MyApplication.getInstance().addActivity(this);
		tvexpirence = (TextView)findViewById(R.id.tvImmediExperi);
		//eBbracom = new EBbraceletcom();
//		connetBtDeice();
		tvexpirence.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			//	json();
			//	MyApplication.getInstance().exit();//需放在返回成功里面
//				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//				startActivity(intent);
				MyApplication.getInstance().exit();
				System.exit(0);
			}
		});
	}
	private void json(){
		String op = "10001";
		String info = "";
		String chk = null;
		JSONObject object2 = new JSONObject();
		try {
			object2.put("name", "zhangsan");
			object2.put("sex", "1");
			object2.put("age", "55");
			object2.put("phone", "13923760416");
			String chkPara = op+ "zhangsan"+"1"+"55"+"13923760416"+"6544dfsd";
			chk = CmdProtocol.md5str(chkPara);
			object2.put("mac", "a3:2d:5d:4g:8e:6t");
			info = object2.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("op", op);
		map2.put("info", info);
		map2.put("chk", chk);
		RequestParams params = new RequestParams(map2);
		
		PatientRestClient.post("register", params, new XmlHttpResponseHandler(){
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
				if(ack.equals("0") && op.equals("10001")){
					MyApplication.ways.ToastShow(getApplicationContext(), "提交成功");
					String err = response.getString("err");
					String info = response.getString("info");
					String uId = response.getString("uId");
					
				}else if(ack.equals("1")){
					MyApplication.ways.ToastShow(getApplicationContext(), "电话号码已存在");
					
				}else if(ack.equals("2")){
					MyApplication.ways.ToastShow(getApplicationContext(), "提交失败");
					
				}else if(ack.equals("3")){
					MyApplication.ways.ToastShow(getApplicationContext(), "电话号码不能为空");
					
				}else if(ack.equals("4")){
					MyApplication.ways.ToastShow(getApplicationContext(), "op配对失败");
					
				}else if(ack.equals("5")){
					MyApplication.ways.ToastShow(getApplicationContext(), "chk配对");
					
				}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.i("info", "sucess");
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.i("info", "fale");
			}
		});
		
	}
	private void connetBtDeice(){
		/*if(MyApplication.datacomm != null) 
			MyApplication.datacomm.StopListen();
		    MyApplication.datacomm = null;//后加的
*/	   //  if(type.equals("2")){
  	    	 	if (!mBtAdapter.isEnabled()) {
//	                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//	                startActivityForResult(enableIntent,1);
	                mBtAdapter.enable();//直接打开
	            } 
	    	 	if(mReceiver == null) mReceiver = new BtDeviceReceiver();
				if(MyApplication.eBbraceletcom == null) MyApplication.eBbraceletcom = new EBbraceletcom();
				MyApplication.eBbraceletcom.Start(new EBEventHander(){
					public void OnBtEvent(int nEventType){
						
						if(nEventType == EBbraceletcom.BT_EVENT_DEVICE_SCANNING){
					        //  注册广播 发现设备
					        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
					        BoundSuccessActivity.this.registerReceiver(mReceiver, filter);
					       
					        //  扫描完成
					        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
					        BoundSuccessActivity.this.registerReceiver(mReceiver, filter);
							java.util.Set<BluetoothDevice> deviceList = MyApplication.eBbraceletcom.GetPairedDevices();
							for (BluetoothDevice dev : deviceList) {
								  String strName = dev.getName();
								// if(dev.getName().equals("SH11") && device == null){//"AET-R121""BTSPP""Dp""GISBLE""UeUa-DM""SH11"
									 if(dev.getAddress().equals(MyApplication.mac) && device == null){//"AET-R121""BTSPP""Dp""GISBLE""UeUa-DM""SH11"
					                	device = dev;
					                	MyApplication.eBbraceletcom.ConnectDevice(dev, true);//调用连接方法	
						            	Toast.makeText(getApplicationContext(), "正在连接蓝牙设备中....", Toast.LENGTH_LONG).show();
					                }       
					                
					            } 
						}
 						if(nEventType == EBbraceletcom.BT_EVENT_DEVICE_PAIRING){
 							
							IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
							BoundSuccessActivity.this.registerReceiver(mReceiver, filter);
							//MyApplication.datacomm.SetCommState(3);
						}
 						if(nEventType == EBbraceletcom.BT_EVENT_DEVICE_CONNECTING){
 						  }
						if(nEventType == EBbraceletcom.BT_EVENT_DEVICE_CONNECTED){
							if(mReceiver != null) BoundSuccessActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播
							mReceiver = null;
 							Toast.makeText(getApplicationContext(), "蓝牙设备连接成功", Toast.LENGTH_LONG).show();
 							String str = "MTKSPPForMMI";
 							byte[] msgBody = str.getBytes();
 							MyApplication.eBbraceletcom.Write(msgBody, msgBody.length);
 							if(MyApplication.ways == null) MyApplication.ways = new CommentWays();
 							MyApplication.ways.ToastShow(getApplicationContext(), "发送第一包数据");
 						}
						if(nEventType == EBbraceletcom.FAILED){
							if(Times == 0){
								Toast.makeText(getApplicationContext(), "蓝牙设备连接失败", Toast.LENGTH_LONG).show();
								Times += 1 ;
							}
						}
					} 
					public void OnError(byte[] data){
						
					}
				});
		//	}
    }
	public class BtDeviceReceiver extends BroadcastReceiver { 
//	    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String action = intent.getAction();

	            // When discovery finds a device
	            
	            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            	BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	                String strName = dev.getName();
	     //           MyApplication.address = dev.getAddress();
	               // if(dev.getName().equals("SH11") && device == null){//"AET-R121""BTSPP""Dp"
	                if(dev.getAddress().equals(MyApplication.mac) && device == null){//"AET-R121""BTSPP""Dp"
	                	device = dev;

	                //	MyApplication.datacomm.btRWriter.PairDevice(dev);//设备配对
	                }       
	                
	            } 
	           if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
	        	   int state = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1);
	        	   int reason = intent.getIntExtra( "android.bluetooth.device.extra.REASON", -1);

	        	   MyApplication.eBbraceletcom.ConnectDevice(true);//调用连接方法
	            }
	            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action) && device == null) {
	            	Toast.makeText(getApplicationContext(), "没有搜索到任何设备", Toast.LENGTH_LONG).show();
	            	if(mReceiver != null) BoundSuccessActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播
	            	mReceiver = null;
	           }
	        }
	    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		else return super.onKeyDown(keyCode, event);
	}
}
