package com.xiao.smartband.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.xiao.smartband.MyApplication;
import com.xiao.smartband.R;
import com.xiao.smartband.bluetooth.EBEventHander;
import com.xiao.smartband.bluetooth.EBbraceletcom;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.HeartRate;
import com.xiao.smartband.protocal.CmdProtocol;
import com.xiao.smartband.tools.CommentWays;
import com.xiao.smartband.view.TitleView;

import java.util.ArrayList;

public class HeartRateActivity extends Activity {
	private TitleView titleView;
	private TextView tvheartRate,tvheartRateTime;
	private BluetoothDevice device = null;
    static private BtDeviceReceiver mReceiver;
    private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	private int Times = 0;
//	private int status;
	private ModelDao model;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_heart_rate);
		init();
		
		
		/*if(MyApplication.CONNETET == true){
			sendCmd();
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
		titleView = (TitleView)findViewById(R.id.titleview);
		titleView.setTitle(R.string.heart_rate);
		titleView.setbg(R.drawable.heart_titlebg);
		titleView.titleImg(R.drawable.heart_titleimg);
		tvheartRate = (TextView)findViewById(R.id.tvheartRate);
		tvheartRateTime = (TextView)findViewById(R.id.tvheartRateTime);
		//显示最近一次的心率
		ArrayList<HeartRate> rates = model.HeartRateSearth();
		if(rates.size() > 0){
			String nheartRate = rates.get(rates.size() - 1).getHeartRate();
			String nheartRateData = rates.get(rates.size() - 1).getHeartDate();
			tvheartRateTime.setText(nheartRateData);
			tvheartRate.setText(nheartRate);
		}
		titleView.setBack(R.drawable.steps_back, new TitleView.onBackLister() {
			
			@Override
			public void onClick(View button) {
				if(mReceiver != null)
					HeartRateActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播   && status == 1
					mReceiver = null;
				finish();
			}
		});
		titleView.right(R.string.history, new TitleView.onSetLister() {
			
			@Override
			public void onClick(View button) {
				Intent intent = new Intent(getApplicationContext(), SportHistoryActivitySec.class);
				MyApplication.activityStuts = 3;
				startActivity(intent);
			}
		});
	}

	private void sendCmdHeartRate(){
		MyApplication.eBbraceletcom.Start(new EBEventHander(){
			@Override
			public void OnRecv(byte[] buffer, int lenght) {
				super.OnRecv(buffer, lenght);
				

				byte [] cmd = new byte[ 1000 ];
		        int nLen = CmdProtocol.DecodeCmdData(buffer, lenght, cmd);
		        if(nLen > 0 ){
				//if(lenght < 13) return;
				//if(buffer != null && lenght > 0){
					byte[] nCmdPara = new byte[1];
					int offset = 4;//命令码的下一位
					if(CmdProtocol.AckExam(cmd, nLen, nCmdPara) == 1){
					  if(nCmdPara[0] == 0x03){
						  if(MyApplication.ways == null) MyApplication.ways = new CommentWays();
							MyApplication.ways.ToastShow(getApplicationContext(), "心率接收数据成功");
							for(int i = 0 ; i<(nLen - 6) / 7 ; i++){
								byte[] date = new byte[6];
								byte[] heartRate = new byte[1];
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, date, 6);
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, heartRate, 1);
								if(heartRate[0] == 0) {
									break;//假如心率为零 跳出当次循环
								}
								String date2 = String.format("%02d", date[2]);
								String date3 = String.format("%02d", date[3]);
								String date4 = String.format("%02d", date[4]);
								String date5 = String.format("%02d", date[5]);
								String time = date[0]*100+date[1]+"-"+date2+"-"+date3+" "+date4+":"+date5;
								tvheartRateTime.setText(time);
								tvheartRate.setText(heartRate[0] + "");
								HeartRate rate = new HeartRate();
								rate.setHeartDate(time);
								rate.setHeartRate(heartRate[0]+"");
								model.insertHeartRate(rate);
							}
					    }
					}
				}
			}
			@Override
			public void OnBtEvent(int nEvent) {
				super.OnBtEvent(nEvent);
				if(nEvent == EBbraceletcom.BT_EVENT_DEVICE_DISCONNET){
					MyApplication.CONNETET = false;
					MyApplication.ways.ToastShow(getApplicationContext(), "蓝牙已断开");
					MyApplication.eBbraceletcom.Stop();
				}
			}
		});

		byte[] msgBody = new byte[1];
		byte[] newcmd = CmdProtocol.newCmd(msgBody, 1);
		byte[] Cmd = CmdProtocol.packageCmd((byte)0x03, newcmd, 1);
		MyApplication.eBbraceletcom.Write(Cmd, Cmd.length);
	}
	
	
	private void connetBtDeice(){
  	    	 	if (!mBtAdapter.isEnabled()) {
//	                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//	                startActivityForResult(enableIntent,1);
	                mBtAdapter.enable();//直接打开
	            } 
	    	 	if(mReceiver == null) mReceiver = new BtDeviceReceiver();
	    	 	
	    	 	if(MyApplication.eBbraceletcom != null) MyApplication.eBbraceletcom.Stop();
				MyApplication.eBbraceletcom = null;
				
				
				if(MyApplication.eBbraceletcom == null) MyApplication.eBbraceletcom = new EBbraceletcom();
				MyApplication.eBbraceletcom.Start(new EBEventHander(){
					public void OnBtEvent(int nEventType){
						
						if(nEventType == EBbraceletcom.BT_EVENT_DEVICE_SCANNING){
					        //  注册广播 发现设备
					        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
					        HeartRateActivity.this.registerReceiver(mReceiver, filter);
					       
					        //  扫描完成
					        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
					        HeartRateActivity.this.registerReceiver(mReceiver, filter);
							java.util.Set<BluetoothDevice> deviceList = MyApplication.eBbraceletcom.GetPairedDevices();
							for (BluetoothDevice dev : deviceList) {
									 if(dev.getAddress().replace(":", "").equals(MyApplication.mac) && device == null){//"38:25:10:1A:62:61"
					                	device = dev;
					                	MyApplication.eBbraceletcom.ConnectDevice(dev, true);//调用连接方法	
						            	MyApplication.ways.ToastShow(getApplicationContext(), "正在连接蓝牙设备中....");
					                }       
					                
					            } 
						}
 						if(nEventType == EBbraceletcom.BT_EVENT_DEVICE_PAIRING){
 							
							IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
							HeartRateActivity.this.registerReceiver(mReceiver, filter);
							//MyApplication.datacomm.SetCommState(3);
						}
 						if(nEventType == EBbraceletcom.BT_EVENT_DEVICE_CONNECTING){
 						  }
						if(nEventType == EBbraceletcom.BT_EVENT_DEVICE_CONNECTED){
							if(mReceiver != null) HeartRateActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播   && status == 1
							   mReceiver = null;
							   device = null;
							  // status = 0 ;
				            	MyApplication.ways.ToastShow(getApplicationContext(), "蓝牙设备连接成功");
 							
 							String str = "MTKSPPForMMI";
 							byte[] msgBody = str.getBytes();
 							MyApplication.eBbraceletcom.Write(msgBody, msgBody.length);
 							MyApplication.eBbraceletcom.Start(new EBEventHander(){
 								public void OnRecv(byte[] sample, int lenght) {
 									if(sample != null){
 										
 									}
 								};
 							});
 							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
 							
							MyApplication.CONNETET = true;
							sendCmdHeartRate();
 						}
						if(nEventType == EBbraceletcom.FAILED){
							/*if(Times == 0 && Times < 3){
							   Toast.makeText(getApplicationContext(), "蓝牙设备连接失败", 1500).show();
							   connetBtDeice();
							   Times += 1 ;
							}*/
			            	MyApplication.ways.ToastShow(getApplicationContext(), "蓝牙设备连接失败");
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
	         //   status = 1;
	            // When discovery finds a device
	            
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
	            	if(mReceiver != null) HeartRateActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播
	            	mReceiver = null;
	           }
	        }
	    }
	
}
