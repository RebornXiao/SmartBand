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
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.xiao.smartband.MyApplication;
import com.xiao.smartband.R;
import com.xiao.smartband.bluetooth.EBEventHander;
import com.xiao.smartband.bluetooth.EBbraceletcom;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.Sleep;
import com.xiao.smartband.protocal.CmdProtocol;
import com.xiao.smartband.tools.CommentWays;
import com.xiao.smartband.view.TitleView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SleepActivity extends Activity {
	private TitleView titleView;
	private TextView tvsleep,tvsleepDeep,tvsleepPercent;
	private BluetoothDevice device = null;
    static private BtDeviceReceiver mReceiver;
    private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	private int Times = 0;
	//private int status;
	private Calendar ca;
	private int year,month,day ;
	private ModelDao model;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_sleep);
		init();
	//	ddd();
		/*if(MyApplication.CONNETET == true){
			sendCmdSleep();
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
		ca = Calendar.getInstance();
		year = ca.get(Calendar.YEAR);
		month=ca.get(Calendar.MONTH);//+1
		day=ca.get(Calendar.DATE);
		titleView.setTitle(R.string.sleep);
		titleView.setbg(R.drawable.sleeptitlebg);
		titleView.titleImg(R.drawable.sleeptitleimg);
		tvsleep = (TextView)findViewById(R.id.tvsleep);
		tvsleepDeep = (TextView)findViewById(R.id.tvsleepDeep);
		tvsleepPercent = (TextView)findViewById(R.id.tvsleepPercent);
		ArrayList<Sleep> sleeps = model.sleepSearth();
		if(sleeps.size() > 0){
			tvsleep.setText(sleeps.get(sleeps.size() - 1).getSleepTime());
			tvsleepDeep.setText(sleeps.get(sleeps.size() - 1).getSleepDeep());
			tvsleepPercent.setText(sleeps.get(sleeps.size() - 1).getSleepPercent());
		}
		titleView.setBack(R.drawable.steps_back, new TitleView.onBackLister() {
			
			@Override
			public void onClick(View button) {
				if(mReceiver != null) SleepActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播  && status == 1
				   mReceiver = null;
				finish();
			}
		});
		titleView.right(R.string.history, new TitleView.onSetLister() {
			
			@Override
			public void onClick(View button) {
				Intent intent = new Intent(getApplicationContext(), SportHistoryActivity.class);
				MyApplication.activityStuts = 4;
				startActivity(intent);
			}
		});
	}
	private void sendCmdSleep(){
		MyApplication.eBbraceletcom.Start(new EBEventHander(){
			@Override
			public void OnRecv(byte[] buffer, int lenght) {
				super.OnRecv(buffer, lenght);
				

				byte [] cmd = new byte[ 1000 ];
		        int nLen = CmdProtocol.DecodeCmdData(buffer, lenght, cmd);
		        
		        if(nLen > 0 ){
		        	
		        	for(int i = 0; i < nLen; i++){
		        		Log.i("info", "bfs="+cmd[i]);
		        	}
		        	
		        	//byte [] cmd = {-114, 0, 11, 4, 20, 14, 10, 25, 14, 50, 6, 25, 1, 24, 15, -114};
					int offset = 4;//命令码的下一位
					byte[] nCmdPara = new byte[1];
					if(CmdProtocol.AckExam(cmd, nLen, nCmdPara) == 1){
						if(nCmdPara[0] == 0x04){
							if(MyApplication.ways == null) MyApplication.ways = new CommentWays();
							MyApplication.ways.ToastShow(getApplicationContext(), "接收数据成功");
							for(int i = 0 ; i< (nLen - 6)/10; i++){
								byte[] time = new byte[6];
								byte[] sleepTime = new byte[3];
								 
								byte[] deepTime = new byte[2];
								//byte[] kaloria = new byte[4];
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, time, 6);
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, sleepTime, 2);
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, deepTime, 2);
								/*//保存标志
								int flagTime = (year)*10000+(month+1)*100+day;
								SleepFlag sleepFlag = new SleepFlag();
								sleepFlag.setDate(flagTime);
								model.insertSleepFlag(sleepFlag);*/
								DecimalFormat df = new DecimalFormat("###.0"); 
								if(sleepTime[0] == 0 && sleepTime[1] == 0) break;
								int lowDeep = (sleepTime[0]*60+sleepTime[1] ) - (deepTime[0]*60+deepTime[1]);
								String nlowDeep = "";
								int nDoubleLow ;
								if(lowDeep > 60){
									nDoubleLow = (lowDeep%60*10/60);
									nlowDeep = lowDeep/60 + "."+nDoubleLow;
									//nlowDeep = lowDeep/60 + "."+lowDeep%60;
								}else {
									nDoubleLow = (lowDeep*10/60);
									nlowDeep = "0."+nDoubleLow;
									//nlowDeep = "0."+lowDeep/60;
								}
								String ntime = time[0]*100+time[1]+"-"+time[2]+"-"+time[3];
								Log.i("info", "ggg="+sleepTime[0] + " "+sleepTime[1]);
								if(sleepTime[0] > 0 || sleepTime[1] > 0)
								tvsleep.setText(nlowDeep);
								int nDoubleDeep = 0 ; 
								if(deepTime[0] > 0 || deepTime[1] > 0){
									nDoubleDeep = deepTime[1]*10/60;//x比上deepTime[1] = 10/60;分成10分
									tvsleepDeep.setText(deepTime[0] +"."+nDoubleDeep);
									//tvsleepDeep.setText(deepTime[0] +"."+deepTime[1]/60);
								}
								int x = 100 *(deepTime[0]*60 + deepTime[1]) /360;//350啥
								if(x > 100) x = 100;
								tvsleepPercent.setText(x+"");
								Sleep sleep = new Sleep();
								sleep.setSleepDate(ntime);
								sleep.setSleepDeep(deepTime[0] +"."+nDoubleDeep);
								sleep.setSleepPercent(x+"");
								if(sleepTime[0] > 0 || sleepTime[1] > 0)
								model.insertSleep(sleep);
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

		/*ArrayList<Sleep> sleeps = model.sleepSearth();
		int date = 0;
		if(sleeps.size() > 0 ){
			String ndate = sleeps.get(0).getSleepDate();//intToByteArray1
			ndate = ndate.replace("-", "");
			if(ndate != null && !ndate.equals("")){
				date = Integer.parseInt(ndate);
			}
		}*/
		byte[] msgBody = new byte[1];
		//byte[] msgBody = {(byte) (date/1000000),(byte) (date/10000%100),(byte) (date%10000 / 100) ,(byte) (date%10000 % 100)};
		byte[] newcmd = CmdProtocol.newCmd(msgBody, 1);
		byte[] Cmd = CmdProtocol.packageCmd((byte)0x04, newcmd, 1);//数据长度 命令码+数据
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
					        SleepActivity.this.registerReceiver(mReceiver, filter);
					       
					        //  扫描完成
					        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
					        SleepActivity.this.registerReceiver(mReceiver, filter);
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
							SleepActivity.this.registerReceiver(mReceiver, filter);
							//MyApplication.datacomm.SetCommState(3);
						}
 						if(nEventType == EBbraceletcom.BT_EVENT_DEVICE_CONNECTING){
 						  }
						if(nEventType == EBbraceletcom.BT_EVENT_DEVICE_CONNECTED){
							if(mReceiver != null) SleepActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播   && status == 1
							   mReceiver = null;
							   device = null;
							 //  status = 0;
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
							sendCmdSleep();
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
	            	if(mReceiver != null) SleepActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播
	            	mReceiver = null;
	           }
	        }
	    }
}
