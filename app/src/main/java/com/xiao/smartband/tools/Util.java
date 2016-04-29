package com.xiao.smartband.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xiao.smartband.MyApplication;
import com.xiao.smartband.R;
import com.xiao.smartband.activity.AdviceInfoActivity;
import com.xiao.smartband.bluetooth.EBEventHander;
import com.xiao.smartband.bluetooth.EBbraceletcom;
import com.xiao.smartband.client.PatientRestClient;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.HealthScan;
import com.xiao.smartband.entity.HealthState;
import com.xiao.smartband.entity.HeartRate;
import com.xiao.smartband.entity.Push;
import com.xiao.smartband.entity.Register;
import com.xiao.smartband.entity.ScanFlag;
import com.xiao.smartband.entity.ScandateFlag;
import com.xiao.smartband.entity.Sleep;
import com.xiao.smartband.entity.Sport;
import com.xiao.smartband.entity.UId;
import com.xiao.smartband.http.RequestParams;
import com.xiao.smartband.http.XmlHttpResponseHandler;
import com.xiao.smartband.protocal.CmdProtocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Util {
	private ModelDao model = null;
	private Context context;
	private int weight = 0;
	private int smallRate = 0,bigRate = 0;
	private int year,month,day ;
	private Calendar ca;
    public Util(Context mContext) {
    	context = mContext;
    	model = new ModelDao(mContext);
    	ca = Calendar.getInstance();
		year = ca.get(Calendar.YEAR);
		month=ca.get(Calendar.MONTH);//+1
		day=ca.get(Calendar.DATE);
		
		ArrayList<Register> registers = model.registerSearth();
		if(registers.size() > 0){
		   String nweight  = registers.get(0).getWeight();
		   if(nweight != null && !nweight.equals("")){
			  weight = Integer.parseInt(nweight);
		   }
		}
	}
	public void sendCmdHealthScan(){
		MyApplication.eBbraceletcom.Start(new EBEventHander(){
			@Override
			public void OnRecv(byte[] buffer, int lenght) {
			   super.OnRecv(buffer, lenght);
			   byte [] cmd = new byte[ 1000 ];
		       int nLen = CmdProtocol.DecodeCmdData(buffer, lenght, cmd);
		       if(nLen > 0 ){
				  int offset = 4;//命令码的下一位
				  byte[] nCmdPara = new byte[1];
				  if(CmdProtocol.AckExam(cmd, nLen, nCmdPara) == 1){
					if(nCmdPara[0] == 0x05){
					  if(MyApplication.ways == null) MyApplication.ways = new CommentWays();
							  
							//if(lenght != 26) return;
						for(int i = 0 ; i<(nLen - 6)/20; i++){
						  byte[] date = new byte[4];
						  byte[] stepTime = new byte[3];
						  byte[] steps = new byte[4];
								
						  byte[] heartRateTime = new byte[2];
						  byte[] heartRateData = new byte[1];
								
						  byte[] sleepTimeDian = new byte[2];
						  byte[] deepTime = new byte[2];
						  byte[] lowTime = new byte[2];
								//byte[] kaloria = new byte[4];
						  offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, date, 4);
						  offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, stepTime, 3);
						  offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, steps, 4);
								
						  offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, heartRateTime, 2);
						  offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, heartRateData, 1);
								
						  offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, sleepTimeDian, 2);
						  offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, deepTime, 2);
						  offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, lowTime, 2);
								
						  ScanFlag scanFlag = new ScanFlag();
						  int DATE = (date[0]*100 + date[1])*10000 +date[2]*100 + date[3];
						  scanFlag.setDate(DATE);
						  scanFlag.setFlag(1);
						  model.insertScanFlag(scanFlag);
								
						  String date2 = String.format("%02d", date[2]);
						  String date3 = String.format("%02d", date[3]);
						  String ndate =date[0]*100 + date[1]+"-"+date2+"-"+date3;
						  String showDate = date2+"月"+date3+"日";
								
						  int nsteps = CmdProtocol.Byte2Int(steps, 0);
						  int nstepTime = stepTime[0]*60+stepTime[1];
						  Log.i("info", "ndatevv="+ndate);
								
						  int pedometerPer = nsteps * 100/10000;
						  if(pedometerPer > 100) pedometerPer = 100;
						  double K = 0.885120; 
						  double Kcal = 0 ;
						  double standardKa = 1;
						  /*DOUBLE K = 0.885120; 
						    Kcal = weight(kg) * distance(km) * K 
							千卡 = 体重（kg） x （步数 x 步伐）x K*/
							//计算卡路里,标准的步幅60Cm
							
						//   Log.i("info", "weight="+weight);
						   if(weight > 0){
							 Kcal = weight*(nsteps * 0.6)/1000 * K;  
								}else {
							 Kcal = 60*(nsteps * 0.6)/1000 * K;  
							   }
							DecimalFormat df = new DecimalFormat("###.00"); 

							//心率是每分钟60至100之间，在这个范围都是正常的
							int deepTimePer = 0;
							int  nDoubleDeep = 0;
							if(deepTime[0] > 0 || deepTime[1] >0){
							  //默认深睡5个小时 为正常睡眠时间
							  nDoubleDeep = deepTime[1]*10/60;//x比上deepTime[1] = 10/60;分成10分
							  //tvsleepDeep.setText(deepTime[0]+"."+nDoubleDeep+"小时");
							  //tvsleepDeep.setText(deepTime[0]+"."+deepTime[1]/60+"小时");
								deepTimePer = deepTime[0] * 100/5;
							 if(deepTimePer > 100) deepTimePer = 100;
								}

							//显示离70最远的心率.
							int nheartRate = getSpecialHeartRate(ndate);
							int heartRatePer = 0 ;
							if(nheartRate >= 60 && nheartRate <= 100){
								heartRatePer = 100;
							}
								
							int Score = 0 ;
							if(pedometerPer > 0 && deepTimePer > 0 && heartRatePer > 0){
							   Score = (pedometerPer*3 + deepTimePer + heartRatePer) / 5;
							}
							HealthScan scan = new HealthScan();
							scan.setHShealthDate(ndate);
							if(Score < 60)scan.setHShealthScore(context.getResources().getString(R.string.poor));
							else scan.setHShealthScore(Score + "");
							scan.setHSheart(nheartRate+"");
							if(Kcal > 1 )
							scan.setHSkaloria(df.format(Kcal));
							else scan.setHSkaloria("0"+df.format(Kcal));
							scan.setHSsleepDeep(deepTime[0]+"."+nDoubleDeep);
							scan.setHSsteps(nsteps + "");
							scan.setHSstepTime(nstepTime+"");
							scan.setHSstepsPer(pedometerPer+"%");
							scan.setHSsleepDeeper(deepTimePer + "%");
							scan.setHSheartRatePer(heartRatePer+"%");
							scan.setHShealthAsses(context.getResources().getString(R.string.healthtip));//tvhealthScanTips.getText().toString()
							//if(stepTime[0] > 0 || stepTime[1] > 1 || stepTime[2] > 0 || sleepTime[0] > 0 || sleepTime[1] > 0){
								//tvdate.setText(showDate);
							model.insertHealthScan(scan);
								//}
									
							MyApplication.ways.ToastShow(context, context.getResources().getString(R.string.health_data_finish));
							/**
							  *断开蓝牙设备 
							 */
							MyApplication.eBbraceletcom.Stop();
							/**
							  *提交数据至服务器 
							  */
									
							CommentWays.DialogCancel();
							if(CommentWays.isWifi(context) == true 
						       || CommentWays.is3G(context) == true){
							   json(ndate);
							  }else {
							   //保存昨天的日期
								MyApplication.ways.ToastShow(context, context.getResources().getString(R.string.net));
								ScandateFlag flag = new ScandateFlag();
								flag.setDate(ndate);
								model.insertDateScanFlag(flag);
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
					MyApplication.ways.ToastShow(context, "蓝牙已断开");
					MyApplication.eBbraceletcom.Stop();
				}
			
			}
		});

		byte[] msgBody = new byte[1];
		byte[] newcmd = CmdProtocol.newCmd(msgBody, 1);
		byte[] Cmd = CmdProtocol.packageCmd((byte)0x05, newcmd, 1);//数据长度 命令码+数据
		MyApplication.eBbraceletcom.Write(Cmd, Cmd.length);
	}
	
	public void json(String date){
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
		HealthScan healthScan = model.queryHealthyScan(date);
		if(healthScan == null) return;
		try {
			object2.put("steps", healthScan.getHSsteps());
			object2.put("sportTime", healthScan.getHSstepTime());
			object2.put("calories", healthScan.getHSkaloria());
			object2.put("deepTime", healthScan.getHSsleepDeep());
		//	int nheartRate = getSpecialHeartRate(date);
			object2.put("heartRate", healthScan.getHSheart());
			object2.put("stepsPer", healthScan.getHSstepsPer());
			object2.put("deepTimePer", healthScan.getHSsleepDeeper());
			object2.put("heartRatePer", healthScan.getHSheartRatePer());
			if(healthScan.getHShealthScore().equals("差"))
				object2.put("score", "0");
		   else object2.put("score", healthScan.getHShealthScore());
			
			object2.put("time", healthScan.getHShealthDate());
			
			List<HealthState> states = model.healthStateSearth();
			if(states.size() > 0){
			   for(int i = 0 ;i<states.size(); i++){
				   String dateState = states.get(i).getDate().substring(0,10);
				   if(date != null && dateState.equals(date)){
					   //依次获取最后一条
					   object2.put("higPressure", states.get(i).getBloodPressure());
					   object2.put("lowPressure", states.get(i).getBloodPressureLow());
					   object2.put("befmeaBlood", states.get(i).getBloodSugar());
					   object2.put("afterBlood", states.get(i).getBloodSugarAfter());
				   }
			   }
			}
			info = object2.toString();
			array.put(object2);
			
			
			//JSONObject object2 = new JSONObject();
				/*object2.put("steps", "10000");
				object2.put("sportTime", "60分钟");
				object2.put("calories", "121.2");
				object2.put("sleepTime", "8.1");
				object2.put("deepTime", "6.1小时");
				object2.put("heartRate", "80");
				object2.put("stepsPer", "80");
				object2.put("deepTimePer", "70");
				object2.put("heartRatePer", "100");
				object2.put("score", "80");
			   object2.put("time", "2014-11-11");
			   info = object2.toString();
			   array.put(object2);
			*/
			
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
				Log.i("info", "onFinish同步数据");
			}
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				try {
			//		Log.i("info", "ok同步数据");
				String ack = response.getString("ack");
				String op = response.getString("op");
				if(ack.equals("0") && op.equals("10002")){
					MyApplication.ways.ToastShow(context, "提交成功");
					String err = response.getString("err");
					String info = response.getString("info");
					String uId = response.getString("uId");
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
	
	
	public void sendCmdHeartRate(){
		MyApplication.eBbraceletcom.Start(new EBEventHander(){
			@Override
			public void OnRecv(byte[] buffer, int lenght) {
				super.OnRecv(buffer, lenght);
				
				byte [] cmd = new byte[ 1000 ];
		        int nLen = CmdProtocol.DecodeCmdData(buffer, lenght, cmd);
		        if(nLen > 0 ){

		        	byte[] nCmdPara = new byte[1];
					int offset = 4;//命令码的下一位
					if(CmdProtocol.AckExam(cmd, nLen, nCmdPara) == 1){
					  if(nCmdPara[0] == 0x03){
						  if(MyApplication.ways == null) MyApplication.ways = new CommentWays();

							MyApplication.ways.ToastShow(context, "心率接收数据成功");
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
								//tvheartRateTime.setText(time);
								//tvheartRate.setText(heartRate[0] + "");
								HeartRate rate = new HeartRate();
								rate.setHeartDate(time);
								rate.setHeartRate(heartRate[0]+"");
								rate.setHeartRated(heartRate[0]);
								model.insertHeartRate(rate);
								//发送健康浏览命令成功
							}
					    }
					}
					//当全部取完数据后,再执行下一个命令
					//取昨天的数据，要是昨天没运动就全为0,每天只能取一次
					boolean flag = healthScanDate();
					 if(flag == true){
					   MyApplication.eBbraceletcom.Stop();
					   CommentWays.DialogCancel();
					   return;
						}
					MyApplication.util.sendCmdHealthScan();
				}
			}
			@Override
			public void OnBtEvent(int nEvent) {
				super.OnBtEvent(nEvent);
				if(nEvent == EBbraceletcom.BT_EVENT_DEVICE_DISCONNET){
					MyApplication.CONNETET = false;
					MyApplication.ways.ToastShow(context, "蓝牙已断开");
					MyApplication.eBbraceletcom.Stop();
				}
			}
		});

		byte[] msgBody = new byte[1];
		byte[] newcmd = CmdProtocol.newCmd(msgBody, 1);
		byte[] Cmd = CmdProtocol.packageCmd((byte)0x03, newcmd, 1);
		MyApplication.eBbraceletcom.Write(Cmd, Cmd.length);
	}
	public void sendCmdSteps(){
		MyApplication.eBbraceletcom.Start(new EBEventHander(){
			@Override
			public void OnRecv(byte[] buffer, int lenght) {
				super.OnRecv(buffer, lenght);
				
				//长度小于17直接return;
				byte [] cmd = new byte[ 1000 ];
		        int nLen = CmdProtocol.DecodeCmdData(buffer, lenght, cmd);
		        if(nLen > 0 ){
		        	
					int offset = 4;//命令码的下一位
					//Sport sport = null;
					byte[] nCmdPara = new byte[1];
 					if(CmdProtocol.AckExam(cmd, nLen, nCmdPara) == 1){
						if(nCmdPara[0] == 0x02){
							if(MyApplication.ways == null) MyApplication.ways = new CommentWays();
							
							MyApplication.ways.ToastShow(context, "计步数据处理完成");
							for(int i = 0 ; i<(nLen -4 - 2)/11; i++){
								byte[] date = new byte[4];
								byte[] stepTime = new byte[3];
								byte[] steps = new byte[4];
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, date, 4);
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, stepTime, 3);
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, steps, 4);

								
								if(steps[0] == 0 && steps[1] == 0 && steps[2] == 0 && steps[3] == 0){
									//发送睡眠数据
								//	MyApplication.util.sendCmdSleep();
									break;
								}
									
								String date2 = String.format("%02d", date[2]);
								String date3 = String.format("%02d", date[3]);
								String ndate = date[0]*100+date[1]+"-"+date2+"-"+date3;
								
								
								int nstepTime = stepTime[0]*60+stepTime[1];
								int nsteps = CmdProtocol.Byte2Int(steps, 0);
								int pedometerPer = nsteps * 100/10000;
								if(pedometerPer > 100) pedometerPer = 100;
								
						//		Log.i("info", "nsteps ="+nsteps);
								/*if(ndate.equals(MyApplication.ways.getDate())){
									tvsteps.setText(nsteps + "");
									tvpedometerPer.setText(pedometerPer+"");
								}*/
								DecimalFormat df = new DecimalFormat("###.00");
								double K = 0.885120; 
								double Kcal = 0 ;
								if(weight > 0){
								   Kcal = weight*(nsteps * 0.6)/1000 * K;  
								}else {
									Kcal = 60*(nsteps * 0.6)/1000 * K;  
								}
								/* Kcal = weight(kg) * distance(km) * K */
								//千卡 = 体重（kg） x （步数 x 步伐）x K
								Sport sport2 = model.querySport(MyApplication.ways.getDate());
								
								if(ndate.equals(MyApplication.ways.getDate()) &&sport2 != null){
									sport2.setDate(ndate);
									sport2.setSportTime(nstepTime+"");
									sport2.setStep(nsteps + "");
									if(Kcal > 1){
										sport2.setKoloria(df.format(Kcal));
									}else {
										sport2.setKoloria("0"+df.format(Kcal));
									}
									sport2.setStepRate(pedometerPer+"");
									model.updateSport(sport2,ndate);
								}else {
									Sport sport = new Sport();
									sport.setDate(ndate);
									sport.setSportTime(nstepTime+"");
									sport.setStep(nsteps + "");
									if(Kcal > 1){
										sport.setKoloria(df.format(Kcal));
									}else {
										sport.setKoloria("0"+df.format(Kcal));
									}
									sport.setStepRate(pedometerPer+"");
									model.insertSport(sport);
								} 
							}
							
							//循环完数据，再发送睡眠数据
							MyApplication.util.sendCmdSleep();
						}
					}
				}
			//}
	    }
			@Override
			public void OnBtEvent(int nEvent) {
				super.OnBtEvent(nEvent);
				if(nEvent == EBbraceletcom.BT_EVENT_DEVICE_DISCONNET){
					MyApplication.CONNETET = false;
					MyApplication.ways.ToastShow(context, "蓝牙已断开");
					MyApplication.eBbraceletcom.Stop();
					//return;
				}
			}
		});
		//(byte) (year/100),(byte) (year%100),(byte) (month +1) ,(byte) day
		ArrayList<Sport> sports = model.sportSearth();
		int date = 0;
		// date = 20141025;
		if(sports.size() > 0 ){
			String ndate = sports.get(sports.size() - 1).getDate();//intToByteArray1
			ndate = ndate.replace("-", "");
			if(ndate != null && !ndate.equals("")){
				date = Integer.parseInt(ndate);
				
			}
		}
		//date = 0;
	//	Log.i("info", "date="+date);
		byte[] msgBody = {(byte) (date/1000000),(byte) (date/10000%100),(byte) (date%10000 / 100) ,(byte) (date%10000 % 100)};
		byte[] newcmd = CmdProtocol.newCmd(msgBody, 4);
		byte[] Cmd = CmdProtocol.packageCmd((byte)0x02, newcmd, 4);//数据长度 命令码+数据
		MyApplication.eBbraceletcom.Write(Cmd, Cmd.length);
	}
	
	private void sendCmdSleep(){
		MyApplication.eBbraceletcom.Start(new EBEventHander(){
			@Override
			public void OnRecv(byte[] buffer, int lenght) {
				super.OnRecv(buffer, lenght);
				

				byte [] cmd = new byte[ 1000 ];
		        int nLen = CmdProtocol.DecodeCmdData(buffer, lenght, cmd);
		        
		        if(nLen > 0 ){
		        	
					int offset = 4;//命令码的下一位
					byte[] nCmdPara = new byte[1];
					if(CmdProtocol.AckExam(cmd, nLen, nCmdPara) == 1){
						if(nCmdPara[0] == 0x04){
							if(MyApplication.ways == null) MyApplication.ways = new CommentWays();

							MyApplication.ways.ToastShow(context, "睡眠数据处理完成");
							for(int i = 0 ; i< (nLen - 6)/10; i++){
								byte[] time = new byte[6];
								byte[] lowTime = new byte[2];
								 
								byte[] deepTime = new byte[2];
								//byte[] kaloria = new byte[4];
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, time, 6);
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, deepTime, 2);
								offset = CmdProtocol.GetPackageItem(cmd, nLen, offset, lowTime, 2);
								
							//	Log.i("info", "deeptime="+deepTime[0] + " " + deepTime[1]);
							//	Log.i("info", "lowTime="+lowTime[0] + " " + lowTime[1]);
								if(lowTime[0] == 0 && lowTime[1] == 0 && 
								   deepTime[0] == 0 && deepTime[1] == 0){
									//发送心率数据
								//	MyApplication.util.sendCmdHeartRate();
									break;
								}
								//int lowDeep = (sleepTime[0]*60+sleepTime[1] ) - (deepTime[0]*60+deepTime[1]);
								//String nlowDeep = "";
								/*int nDoubleLow ;
								if(lowDeep > 60){
									nDoubleLow = (lowDeep%60*10/60);
									nlowDeep = lowDeep/60 + "."+nDoubleLow;
								}else {
									nDoubleLow = (lowDeep*10/60);
									nlowDeep = "0."+nDoubleLow;
								}*/
								
								
								String ntime = time[0]*100+time[1]+"-"+time[2]+"-"+time[3];
								
								int nDoubleLow = 0 ; 
								if(lowTime[0] > 0 || lowTime[1] > 0){
									nDoubleLow = lowTime[1]*10/60;//x比上deepTime[1] = 10/60;分成10分
								}
								
								
								int nDoubleDeep = 0 ; 
								if(deepTime[0] > 0 || deepTime[1] > 0){
									nDoubleDeep = deepTime[1]*10/60;//x比上deepTime[1] = 10/60;分成10分
								}
								int x = 100 *(deepTime[0]*60 + deepTime[1]) /360;//350啥
								if(x > 100) x = 100;
								Sleep sleep = new Sleep();
								sleep.setSleepDate(ntime);
								sleep.setSleepDeep(deepTime[0] +"."+nDoubleDeep);
								sleep.setSleepTime(lowTime[0] + "."+nDoubleLow);
								sleep.setSleepPercent(x+"");
							  if(lowTime[0] > 0 || lowTime[1] > 0 ||deepTime[0] > 0 || deepTime[1] > 0)
								model.insertSleep(sleep);
							}
							//循环完数据，发送心率数据
							MyApplication.util.sendCmdHeartRate();
						}
					}
				}
			}
			
			@Override
			public void OnBtEvent(int nEvent) {
				super.OnBtEvent(nEvent);
				if(nEvent == EBbraceletcom.BT_EVENT_DEVICE_DISCONNET){
					MyApplication.CONNETET = false;
					MyApplication.ways.ToastShow(context, "蓝牙已断开");
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
	
	
	public void jsonRegis(){
		String op = "10001";
		String info = "";
		String chk = null;
		JSONObject object2 = new JSONObject();
		int sexint = 0 ;
		ArrayList<Register> registers = model.registerSearth();
		if(registers.size() == 0) return;
		String name = registers.get(0).getName();
		String nsex = registers.get(0).getSex();
		String nage = registers.get(0).getAge();
		String nheight = registers.get(0).getHeight();
		String nweight = registers.get(0).getWeight();
		String num = registers.get(0).getNum();//"13965656556";//
		
		
		try {
			object2.put("name", name);
			if(nsex.equals("男")){
				sexint = 1;
				object2.put("sex", sexint);
			}else if(nsex.equals("女")){
				sexint = 2;
				object2.put("sex", sexint);
			}
			object2.put("age", nage);
			object2.put("height", nheight);
			object2.put("weight", nweight);
			object2.put("phone", num);
			object2.put("mac", MyApplication.mac);
			//协议上说mac12位，不包括冒号!?
			String chkPara = op+ name+sexint+nage+num +nheight + nweight+MyApplication.mac+MyApplication.pswd;
			chk = CmdProtocol.md5str(chkPara);
			info = object2.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("op", op);
		map2.put("info", info);
		map2.put("chk", chk);
		RequestParams params = new RequestParams(map2);
	//	Log.i("info", "发送注册消息");
		PatientRestClient.post("register", params, new XmlHttpResponseHandler(){
			@Override
			public void onFinish() {
				super.onFinish();
			//	Log.i("info", "onFinish");
			}
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				try {
				//	Log.i("info", "主界面提交注册");
				String ack = response.getString("ack");
				String op = response.getString("op");
				if(ack.equals("0") && op.equals("10001")){
					//MyApplication.ways.ToastShow(getApplicationContext(), "提交成功");
					String err = response.getString("err");
					String info = response.getString("info");
					String uId = response.getString("uId");
					
					UId id = new UId();
					id.setuId(uId);
					model.insertuId(id);
					/**
					 *再次提交注册信息后，发送推送信息 
					 */
					jsonPush();
					//提交成功后，删除service表
					model.deleteSendService();
				//	MyApplication.uId = uId;
				}else if(ack.equals("1")){
					MyApplication.ways.ToastShow(context, "电话号码已存在");
					
				}else if(ack.equals("2")){
					MyApplication.ways.ToastShow(context, "提交失败");
					
				}else if(ack.equals("3")){
					MyApplication.ways.ToastShow(context, "电话号码不能为空");
					
				}else if(ack.equals("4")){
					MyApplication.ways.ToastShow(context, "op配对失败");
					
				}else if(ack.equals("5")){
					MyApplication.ways.ToastShow(context, "chk配对失败");
					
				  }
				} catch (JSONException e) {
					e.printStackTrace();
				}
			//	Log.i("info", "sucess");
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				//没有网络，运动至此,失败
			/*	SendServiceFlag sendServiceFlag = new SendServiceFlag();
				sendServiceFlag.setRegisterFlag(1);//1代表失败
				model.insertsendService(sendServiceFlag);*/
				Log.i("info", "注册false");
			}
		});
		
	}
	
	
	public void jsonPush(){
		ArrayList<UId> uids = model.uIdSearth();
		if(uids.size() == 0) return;
		String uId = uids.get(0).getuId();//"94718188"; //
		String op = "10003";
		String info = "";
		JSONObject object2 = new JSONObject();
		ArrayList<Push> pushs = model.pushSearth();
		
		try {
			if(pushs.size() > 0){
				//2014-11-18 06:54:44
//				Toast.makeText(context, "", 3000).show();
				object2.put("time", pushs.get(pushs.size() - 1).getDate());
			}else {
				//object2.put("time", "2014-11-18 06:54:44");
//				Toast.makeText(context, "", 3000).show();
				object2.put("time", "0");
			}
			
			
			
			//object2.put("time", "2014-11-21 15:42:38");
 			info = object2.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("op", op);
		map2.put("info", info);
		map2.put("uId", uId);//"69836077" "8638836"
	//	map2.put("uId", "69836077");//"69836077" "8638836"
	//	Log.i("info", "info="+info.toString());
		RequestParams params = new RequestParams(map2);
		
		PatientRestClient.post("push", params, new XmlHttpResponseHandler(){//c10003
			//PatientRestClient.post("c10003", params, new XmlHttpResponseHandler(){//c10003
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
				
				if(ack.equals("0") && op.equals("10003")){
					String dayTime = response.getString("dayTime");
					if(dayTime != null) {
						model.insertServerTime(dayTime);
					}
					//MyApplication.ways.ToastShow(getApplicationContext(), "提交成功");
					String err = response.getString("err");
				//	String dayTime = response.getString("dayTime");测试时间
					String info = response.getString("info");
					JSONArray array = null;
					if(info != null && !info.equals("{}")){
					   getVoice();//声音再处理一下,哇靠.
					   array = new JSONArray(info);
					   Push push = null ;
						for(int i = 0 ; i< array.length(); i++){
							JSONObject objectdd = (JSONObject) array.get(i); 
					//		Log.i("info", "pb="+objectdd.toString());
							push = new Push();
							push.setContent(objectdd.getString("content"));
							push.setDate(objectdd.getString("time"));
							push.setTitle(objectdd.getString("pushTitle"));
							MyApplication.temPushs.add(push);
						}
						if(MyApplication.temPushs.size() > 0){
							for(int i = MyApplication.temPushs.size() - 1; i >= 0; i --){
								model.insertHealthPush(MyApplication.temPushs.get(i));
							}
							MyApplication.temPushs = new ArrayList<Push>();
						}
						//  getVoice();
					}
					
				}else if(ack.equals("1")){
					//MyApplication.ways.ToastShow(getApplicationContext(), "电话号码已存在");
					
				}else if(ack.equals("2")){
					//MyApplication.ways.ToastShow(getApplicationContext(), "提交失败");
					
				}else if(ack.equals("3")){
					//MyApplication.ways.ToastShow(getApplicationContext(), "电话号码不能为空");
					
				}else if(ack.equals("4")){
					//MyApplication.ways.ToastShow(getApplicationContext(), "op配对失败");
					
				}else if(ack.equals("5")){
					//MyApplication.ways.ToastShow(getApplicationContext(), "chk配对");
					
				}
				} catch (JSONException e) {
					e.printStackTrace();
				}
		//		Log.i("info", "sucess");
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.i("info", "fale");
				//MyApplication.ways.ToastShow(context, "推送失败");
			}
		});
		
	}
	
	private void getVoice(){
		 String Currentactivity =  MyApplication.ways.isRunningForeground(context);
		 if(!Currentactivity.equals("com.bt.elderbracelet.activity.MainActivity")){
			 NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); 
				Notification notification = null; //= new Notification(); 
				if(manager == null)manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
				if(notification == null)notification = new Notification(R.drawable.ic_launcher, "安心",System.currentTimeMillis());
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				//自定义声音   声音文件放在ram目录下，没有此目录自己创建一个
			//	notification.sound=Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" +R.raw.mm); 
				notification.defaults=Notification.DEFAULT_SOUND;
				Intent intent = new Intent(context, AdviceInfoActivity.class);//实例化intent
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);		
				PendingIntent pi = PendingIntent.getActivity(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//				notification.setLatestEventInfo(context, "如医","有新消息", pi);// 设置事件信息
				manager.notify(1, notification); 
			 }
		
	}

	private int getSpecialHeartRate(String date){
		
		List<HeartRate> Temrates = new ArrayList<HeartRate>();
		ArrayList<HeartRate> rates = model.HeartRateSearth();
		if(rates.size() > 0){
			for(int i =0 ;i < rates.size(); i++){
				String dateRate = rates.get(i).getHeartDate().substring(0, 10);
				if(dateRate.equals(date)){
					Temrates.add(rates.get(i));
				}
			}
		}
		if(Temrates.size() == 1){
			return Temrates.get(0).getHeartRated();
		}else if(Temrates.size() > 1){
			smallRate = Temrates.get(0).getHeartRated();
			bigRate = Temrates.get(0).getHeartRated();
			for(int i = 0 ; i<Temrates.size(); i++){
				if(smallRate > Temrates.get(i).getHeartRated() ){
					smallRate = Temrates.get(i).getHeartRated();
				}
				if(bigRate < Temrates.get(i).getHeartRated() ){
					bigRate = Temrates.get(i).getHeartRated();
				}
			}
			if(smallRate != 0 && bigRate != 0 ){
				if(Math.abs(smallRate - 70 ) > Math.abs(bigRate - 70 )){ 
					return smallRate;
				}else {
					return bigRate;
				}
			}
		}
		return 0;
	}
	
	/**
	 *健康浏览判断昨日日期
	 *1、假如是1号，并且是2月，再判断平闰年，可以直接得出当月的天数
	 *2、假如是1号，不是2月，直接通过月份得出当月的天数
	 */
	private boolean healthScanDate(){
		int scanDate = year*10000 + (month+1)*100 +  (day- 1);
		//boolean flag = model.queryScanFlag(year*10000 + (month+1)*100 +  (day- 1));
		if(day == 1 && (month+1) == 2){
			if((year % 4 == 0 && year % 100 != 0) || year % 400 == 0){//闰年
				scanDate = year*10000 + month*100 +  29;
			}else {//平年
				scanDate = year*10000 + month*100 +  28;
			}
		}else if(day == 1){
			if( month == 4 || month == 6 || month == 9 || month == 11){
				scanDate = year*10000 + month*100 +  30;
			}else {
				scanDate = year*10000 + month*100 +  31;
			}
		}
		boolean flag = model.queryScanFlag(scanDate);
		return flag;
	}
}
