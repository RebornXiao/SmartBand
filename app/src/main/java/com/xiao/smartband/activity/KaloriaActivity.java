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
import com.xiao.smartband.entity.Register;
import com.xiao.smartband.entity.Sport;
import com.xiao.smartband.protocal.CmdProtocol;
import com.xiao.smartband.tools.CommentWays;
import com.xiao.smartband.view.TitleView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class KaloriaActivity extends Activity {
	private TitleView titleView;
	private TextView tvkaCurrent,tvkaPercent;
	private ModelDao model;
	private BluetoothDevice device = null;
    static private BtDeviceReceiver mReceiver;
    private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	private int Times = 0;
	//private int status;
	int weight = 60;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_kaloria);
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
		model = new  ModelDao(getApplicationContext());
		ArrayList<Register> registers = model.registerSearth();
		if(registers.size() > 0){
			  String strWeight = registers.get(0).getWeight();
			  if(!strWeight.equals("") && !strWeight.equals("0") && !strWeight.equals("体重")){
				  weight = Integer.parseInt(strWeight);
			  }
			}
		titleView = (TitleView)findViewById(R.id.titleview);
		tvkaCurrent = (TextView)findViewById(R.id.tvkaCurrent);
		tvkaPercent = (TextView)findViewById(R.id.tvkaPercent);
		ArrayList<Sport> sports = model.sportSearth();
		if(sports.size() > 0){
		   int index = sports.size() - 1;
		   tvkaCurrent.setText(sports.get(index).getKoloria());
		   tvkaPercent.setText(sports.get(index).getStepRate());
		}
		titleView.setTitle(R.string.kaloria);
		titleView.titleImg(R.drawable.ka_titleimg);
		titleView.setbg(R.drawable.kariabg);
		titleView.setBack(R.drawable.steps_back, new TitleView.onBackLister() {
			
			@Override
			public void onClick(View button) {
			//	MyApplication.eBbraceletcom.Stop();
				if(mReceiver != null) KaloriaActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播
				   mReceiver = null;
				   finish();
			}
		});
		titleView.right(R.string.history, new TitleView.onSetLister() {
			
			@Override
			public void onClick(View button) {
				Intent intent = new Intent(getApplicationContext(), SportHistoryActivity.class);
				MyApplication.activityStuts = 2;
				startActivity(intent);
			}
		});
		
		
	}

	private void sendCmd(){
		MyApplication.eBbraceletcom.Start(new EBEventHander(){
			@Override
			public void OnRecv(byte[] buffer, int lenght) {
				super.OnRecv(buffer, lenght);

				byte [] cmd = new byte[ 1000 ];
		        int nLen = CmdProtocol.DecodeCmdData(buffer, lenght, cmd);
		        if(nLen > 0 ){
		        	
				//if(lenght < 17) return;
				//if(buffer != null && lenght > 0){
					int offset = 4;//命令码的下一位
					//Sport sport = null;
					byte[] nCmdPara = new byte[1];
					if(CmdProtocol.AckExam(cmd, nLen, nCmdPara) == 1){
						if(nCmdPara[0] == 0x02){
							if(MyApplication.ways == null) MyApplication.ways = new CommentWays();
							MyApplication.ways.ToastShow(getApplicationContext(), "接收数据成功");
							for(int i = 0 ; i<(nLen - 4 - 2)/11; i++){
								byte[] date = new byte[4];
								byte[] stepTime = new byte[3];
								byte[] steps = new byte[4];
								//byte[] kaloria = new byte[4];
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, date, 4);
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, stepTime, 3);
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, steps, 4);
								if(steps[0] == 0 && steps[1] == 0 && steps[2] == 0 && steps[3] == 0)
									break;
								String date2 = String.format("%02d", date[2]);
								String date3 = String.format("%02d", date[3]);
								String ndate = date[0]*100+date[1]+"-"+date2+"-"+date3;
								int nstepTime = stepTime[0]*60+stepTime[1];
								int nsteps = CmdProtocol.Byte2Int(steps, 0);
								int pedometerPer = nsteps * 100/10000;
								if(pedometerPer > 100) pedometerPer = 100;
								double K = 0.885120; 
								double Kcal = 0 ;
								double standardKa = 1;
								/*DOUBLE K = 0.885120; 
								 Kcal = weight(kg) * distance(km) * K 
								千卡 = 体重（kg） x （步数 x 步伐）x K*/
								//计算卡路里,标准的步幅60Cm
								if(weight > 0){
								   Kcal = weight*(nsteps * 0.6)/1000 * K;  
								}else {
									Kcal = 60*(nsteps * 0.6)/1000 * K;  
								}
								
								DecimalFormat df = new DecimalFormat("###.00");  
								/*//1000步对应的卡路里
								if(weight > 0){
									standardKa = weight*(10000 * 0.6)/1000 * K;  
									}else {
									standardKa = 60*(10000 * 0.6)/1000 * K;  
									}
								//计算百分比
								int strKaper =  (int)(Kcal * 100/standardKa) ;*/
								if(ndate.equals(MyApplication.ways.getDate())){
									tvkaPercent.setText(pedometerPer + "");

									if(Kcal > 1){
										tvkaCurrent.setText(df.format(Kcal));
									}else {
										tvkaCurrent.setText("0"+df.format(Kcal));
									}
								}
								Sport sport2 = model.querySport(MyApplication.ways.getDate());
								if(ndate.equals(MyApplication.ways.getDate()) &&  sport2 != null){
									sport2.setDate(ndate);
									sport2.setSportTime(nstepTime+"分钟");
									sport2.setStep(nsteps + "");
									if(Kcal > 1){
										sport2.setKoloria(df.format(Kcal));
									}else {
										sport2.setKoloria("0"+df.format(Kcal));
									}
									sport2.setStepRate(pedometerPer+"%");
									model.updateSport(sport2,ndate);
								}else {
									Sport sport = new Sport();
									sport.setDate(ndate);
									sport.setSportTime(nstepTime+"分钟");
									sport.setStep(nsteps + "");
									if(Kcal > 1){
										sport.setKoloria(df.format(Kcal));
									}else {
										sport.setKoloria("0"+df.format(Kcal));
									}
									sport.setStepRate(pedometerPer+"%");
									model.insertSport(sport);
								}
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

		ArrayList<Sport> sports = model.sportSearth();
		int date = 0;
		if(sports.size() > 0 ){
			String ndate = sports.get(sports.size() - 1).getDate();//intToByteArray1
			ndate = ndate.replace("-", "");
			if(ndate != null && !ndate.equals("")){
				date = Integer.parseInt(ndate);
			}
		}
		//byte[] msgBody = new byte[1];
		byte[] msgBody = {(byte) (date/1000000),(byte) (date/10000%100),(byte) (date%10000 / 100) ,(byte) (date%10000 % 100)};
		byte[] newcmd = CmdProtocol.newCmd(msgBody, 4);
		byte[] Cmd = CmdProtocol.packageCmd((byte)0x02, newcmd, 4);//数据长度 命令码+数据
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
				
				MyApplication.eBbraceletcom = new EBbraceletcom();
				MyApplication.eBbraceletcom.Start(new EBEventHander(){
					public void OnBtEvent(int nEventType){
						
						if(nEventType == EBbraceletcom.BT_EVENT_DEVICE_SCANNING){
					        //  注册广播 发现设备
					        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
					        KaloriaActivity.this.registerReceiver(mReceiver, filter);
					       
					        //  扫描完成
					        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
					        KaloriaActivity.this.registerReceiver(mReceiver, filter);
					       // status = 1;
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
							KaloriaActivity.this.registerReceiver(mReceiver, filter);
							// status = 1;
							//MyApplication.datacomm.SetCommState(3);
						}
 						if(nEventType == EBbraceletcom.BT_EVENT_DEVICE_CONNECTING){
 						  }
						if(nEventType == EBbraceletcom.BT_EVENT_DEVICE_CONNECTED){
							if(mReceiver != null) KaloriaActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播  && status == 1
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
							sendCmd();
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
	            //status = 1;
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
	            	if(mReceiver != null) KaloriaActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播
	            	mReceiver = null;
	           }
	        }
	    }
	
	
}
