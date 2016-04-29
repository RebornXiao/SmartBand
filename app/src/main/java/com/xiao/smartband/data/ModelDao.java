package com.xiao.smartband.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xiao.smartband.entity.HealthScan;
import com.xiao.smartband.entity.HealthState;
import com.xiao.smartband.entity.HeartRate;
import com.xiao.smartband.entity.Mac;
import com.xiao.smartband.entity.Push;
import com.xiao.smartband.entity.Register;
import com.xiao.smartband.entity.ScanFlag;
import com.xiao.smartband.entity.ScandateFlag;
import com.xiao.smartband.entity.SendServiceFlag;
import com.xiao.smartband.entity.Sleep;
import com.xiao.smartband.entity.SleepFlag;
import com.xiao.smartband.entity.Sport;
import com.xiao.smartband.entity.Status;
import com.xiao.smartband.entity.UId;

import java.util.ArrayList;
import java.util.List;

public class ModelDao {
	private DBOpenHelper helper;
	public ModelDao(Context context){
		helper = new DBOpenHelper(context);
	}
	public long insertSport(Sport sport){
		long rowId = -1;
		ContentValues values = new ContentValues();
		//values.put(DBOpenHelper.pedometerId, sport.getId());
		values.put(DBOpenHelper.steps, sport.getStep());
		values.put(DBOpenHelper.sportTime, sport.getSportTime());
		values.put(DBOpenHelper.kaloria, sport.getKoloria());
		values.put(DBOpenHelper.PDTdate, sport.getDate());
		values.put(DBOpenHelper.PDTpercent, sport.getStepRate());
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.pedometerTal, DBOpenHelper.pedometerId, values);
		db.close();
		return rowId;
	}
	public long updateSport(Sport sport,String date){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.steps, sport.getStep());
		values.put(DBOpenHelper.sportTime, sport.getSportTime());
		values.put(DBOpenHelper.kaloria, sport.getKoloria());
		values.put(DBOpenHelper.PDTdate, sport.getDate());
		values.put(DBOpenHelper.PDTpercent, sport.getStepRate());
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] args = {date};
		rowId = db.update(DBOpenHelper.pedometerTal, values, "PDTdate=?",args);//"photoId=?",args
		db.close();
		return rowId;
	}
	
	
	public Sport querySport(String date){
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c =db.rawQuery("select * from "+DBOpenHelper.pedometerTal +" "+ 
				"where PDTdate = ?", new String[]{date});
		Sport sport = null;
		if(c!=null && c.getCount()>0){
			String[] colNames = c.getColumnNames();
			while(c.moveToNext()){
				sport = new Sport();
			  for(String columnName : colNames){//columnName字段的值
				if(columnName.equals(DBOpenHelper.steps))
					sport.setStep(c.getString(c.getColumnIndex(columnName)));
				if(columnName.equals(DBOpenHelper.sportTime))
					sport.setSportTime(c.getString(c.getColumnIndex(columnName)));
				if(columnName.equals(DBOpenHelper.kaloria))
					sport.setKoloria(c.getString(c.getColumnIndex(columnName)));
				if(columnName.equals(DBOpenHelper.PDTpercent))
					sport.setStepRate(c.getString(c.getColumnIndex(columnName)));
				if(columnName.equals(DBOpenHelper.PDTdate))
					sport.setDate(c.getString(c.getColumnIndex(columnName)));
				flag = true;
				}
			}
		}
		c.close();
		db.close();
		return sport;
	}
	
	
	public long insertHealthState(HealthState state){
		long rowId = -1;
		ContentValues values = new ContentValues();
		//values.put(DBOpenHelper.pedometerId, sport.getId());
		values.put(DBOpenHelper.bloodPressure, state.getBloodPressure());
		values.put(DBOpenHelper.bloodSugar, state.getBloodSugar());
		values.put(DBOpenHelper.healthStateWeight, state.getBloodPressureLow());
		values.put(DBOpenHelper.bloodSugarAfter, state.getBloodSugarAfter());
		values.put(DBOpenHelper.healthStateDate, state.getDate());
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.HealthStateTal, DBOpenHelper.HealthStateId, values);
		db.close();
		return rowId;
	}
	
	
	public long insertScanFlag(ScanFlag flag){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.date, flag.getDate());
		values.put(DBOpenHelper.flag, flag.getFlag());
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.scanFlagTal, DBOpenHelper.scanFlagId, values);
		db.close();
		return rowId;
	}
	
	
	public long insertServerTime(String str){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.serverReturnTime, str.toString());
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.serverTimeTal, DBOpenHelper.serverTimeId, values);
		db.close();
		return rowId;
	}
	
	
	public long insertDateScanFlag(ScandateFlag scandateFlag){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.scanDate, scandateFlag.getDate());
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.scanDateFlagTal, DBOpenHelper.scanDateFlagId, values);
		db.close();
		return rowId;
	}
	
	public boolean queryScanFlag(int date){
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c =db.rawQuery("select * from "+DBOpenHelper.scanFlagTal +" "+ 
				"where date = ?", new String[]{date+""});
		ScanFlag scanFlag = null;
		if(c!=null && c.getCount()>0){
			String[] colNames = c.getColumnNames();
			while(c.moveToNext()){
				scanFlag = new ScanFlag();
			  for(String columnName : colNames){//columnName字段的值
				if(columnName.equals(DBOpenHelper.date))
					scanFlag.setDate(c.getInt(c.getColumnIndex(columnName)));
				if(columnName.equals(DBOpenHelper.flag))
					scanFlag.setFlag(c.getInt(c.getColumnIndex(columnName)));
				flag = true;
				}
			}
		}
		c.close();
		db.close();
		return flag;
	}
	
	
	public long updateHealthState(HealthState state,String date){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.bloodPressure, state.getBloodPressure());
		values.put(DBOpenHelper.bloodSugar, state.getBloodSugar());
		values.put(DBOpenHelper.healthStateWeight, state.getBloodPressureLow());
		values.put(DBOpenHelper.bloodSugarAfter, state.getBloodSugarAfter());
		values.put(DBOpenHelper.healthStateDate, state.getDate());
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] args = {date};
		rowId = db.update(DBOpenHelper.HealthStateTal, values, "healthStateDate=?",args);//"photoId=?",args
		db.close();
		return rowId;
	}
	
	public boolean queryHealthState(String date){
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c =db.rawQuery("select * from "+DBOpenHelper.HealthStateTal +" "+ 
				"where healthStateDate = ?", new String[]{date});
		//ScanFlag scanFlag = null;
		if(c!=null && c.getCount()>0){
			//String[] colNames = c.getColumnNames();
			while(c.moveToNext()){
				//scanFlag = new ScanFlag();
			  //for(String columnName : colNames){//columnName字段的值
				flag = true;
				//}
			}
		}
		c.close();
		db.close();
		return flag;
	}
	
	public long insertbrace(Mac mac){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.mac, mac.getMac());
		values.put(DBOpenHelper.psword, mac.getPsword());
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.braceTal, DBOpenHelper.braceId, values);
		db.close();
		return rowId;
	}
	
	
	public long insertsendService(SendServiceFlag serviceFlag){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.registerFlag, serviceFlag.getRegisterFlag());
		values.put(DBOpenHelper.healthScanFlag, serviceFlag.getHealthScanFlag());
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.sendServiceTal, DBOpenHelper.sendServiceId, values);
		db.close();
		return rowId;
	}
	
	
	public long insertRegister(Register register){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.name, register.getName());
		values.put(DBOpenHelper.phoneNum, register.getNum());
		values.put(DBOpenHelper.age, register.getAge());
		values.put(DBOpenHelper.sex, register.getSex());
		values.put(DBOpenHelper.height, register.getHeight());
		values.put(DBOpenHelper.weight, register.getWeight());
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.registerTal, DBOpenHelper.registerId, values);
		db.close();
		return rowId;
	}
	
	
	public long insertuId(UId uid){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.uId, uid.getuId());
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.uIdTal, DBOpenHelper.uIdId, values);
		db.close();
		return rowId;
	}
	
	public long insertStatus(Status status){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.stutas, status.getStaus());
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.statsTal, DBOpenHelper.statusId, values);
		db.close();
		return rowId;
	}
	
	public long insertHeartRate(HeartRate heartRate){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.heartRate, heartRate.getHeartRate());
		values.put(DBOpenHelper.HRdate, heartRate.getHeartDate());
		values.put(DBOpenHelper.HRpercent, heartRate.getHRpercent());
		values.put(DBOpenHelper.heartRated, heartRate.getHeartRated());
		
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.heartRateTal, DBOpenHelper.heartRateId, values);
		db.close();
		return rowId;
	}
	
	public long updateHeartRate(HeartRate heartRate){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.heartRate, heartRate.getHeartRate());
		values.put(DBOpenHelper.HRdate, heartRate.getHeartDate());
		values.put(DBOpenHelper.HRpercent, heartRate.getHRpercent());
		values.put(DBOpenHelper.heartRated, heartRate.getHeartRated());
		
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.update(DBOpenHelper.heartRateTal, values, null, null);
		db.close();
		return rowId;
	}
	
	public long insertSleepFlag(SleepFlag sleepFlag){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.sleepDateFlag, sleepFlag.getDate());
		
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.sleepFlagTal, DBOpenHelper.sleepFlagId, values);
		db.close();
		return rowId;
	}
	
	public boolean querySleepflag(int date){
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c =db.rawQuery("select * from "+DBOpenHelper.sleepFlagTal +" "+ 
				"where sleepDateFlag = ?", new String[]{date+""});
		Sleep sleep = null;
		if(c!=null && c.getCount()>0){
			String[] colNames = c.getColumnNames();
			while(c.moveToNext()){
				sleep = new Sleep();
			  for(String columnName : colNames){//columnName字段的值
				  if(columnName.equals(DBOpenHelper.sleepDateFlag))
					  sleep.setSleepDate(c.getString(c.getColumnIndex(columnName)));
				  flag = true;
				}
			}
		}
		c.close();
		db.close();
		return flag;
	}
	
	
	public int delete(){
		int  count = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenHelper.pedometerTal, null, null);
		db.close();	
		return count;
	}
	
	public int deleteSendService(){
		int  count = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenHelper.sendServiceTal, null, null);
		db.close();	
		return count;
	}
	public int deleteSleep(){
		int  count = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenHelper.sleepTal, null, null);
		db.close();	
		return count;
	}
	public long insertSleep(Sleep sleep){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.sleepDeep, sleep.getSleepDeep());
		values.put(DBOpenHelper.sleepDate, sleep.getSleepDate());
		values.put(DBOpenHelper.sleepTime, sleep.getSleepTime());
		values.put(DBOpenHelper.sleepPercent, sleep.getSleepPercent());
		
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.sleepTal, DBOpenHelper.sleepId, values);
		db.close();
		return rowId;
	}
	
	public long updateSleep(Sleep sleep){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.sleepDeep, sleep.getSleepDeep());
		values.put(DBOpenHelper.sleepDate, sleep.getSleepDate());
		values.put(DBOpenHelper.sleepTime, sleep.getSleepTime());
		values.put(DBOpenHelper.sleepPercent, sleep.getSleepPercent());
		
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.update(DBOpenHelper.sleepTal, values, null, null);
		db.close();
		return rowId;
	}
	
	public boolean querySleep(String date){
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c =db.rawQuery("select * from "+DBOpenHelper.sleepTal +" "+ 
				"where sleepDate = ?", new String[]{date});
		Sleep sleep = null;
		if(c!=null && c.getCount()>0){
			String[] colNames = c.getColumnNames();
			while(c.moveToNext()){
				sleep = new Sleep();
			  for(String columnName : colNames){//columnName字段的值
				  if(columnName.equals(DBOpenHelper.sleepDate))
					  sleep.setSleepDate(c.getString(c.getColumnIndex(columnName)));
				  flag = true;
				}
			}
		}
		c.close();
		db.close();
		return flag;
	}
	
	public long insertHealthScan(HealthScan scan){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.healthDate, scan.getHShealthDate());
		values.put(DBOpenHelper.healthSteps, scan.getHSsteps());
		values.put(DBOpenHelper.healthHeart, scan.getHSheart());
		values.put(DBOpenHelper.healthKaloria, scan.getHSkaloria());
		values.put(DBOpenHelper.healthSleepDeep, scan.getHSsleepDeep());
		values.put(DBOpenHelper.healthStepTime, scan.getHSstepTime());
		values.put(DBOpenHelper.healthScore, scan.getHShealthScore());
		values.put(DBOpenHelper.healthSteper, scan.getHSstepsPer());
		values.put(DBOpenHelper.healtheartRatePer, scan.getHSheartRatePer());
		values.put(DBOpenHelper.healthSleepDeepPer, scan.getHSsleepDeeper());
		values.put(DBOpenHelper.healthAsses, scan.getHShealthAsses());
		
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.healthScanTal, DBOpenHelper.healthId, values);
		db.close();
		return rowId;
	}


	public HealthScan queryHealthyScan(String date){
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c =db.rawQuery("select * from "+DBOpenHelper.healthScanTal +" "+ 
				"where healthDate = ?", new String[]{date});
		HealthScan healthScan = null;
		if(c!=null && c.getCount()>0){
			String[] colNames = c.getColumnNames();
			while(c.moveToNext()){
				healthScan = new HealthScan();
			  for(String columnName : colNames){//columnName字段的值
				if(columnName.equals(DBOpenHelper.healthSteps))
					healthScan.setHSsteps(c.getString(c.getColumnIndex(columnName)));
				if(columnName.equals(DBOpenHelper.healthHeart))
					healthScan.setHSheart(c.getString(c.getColumnIndex(columnName)));

				if(columnName.equals(DBOpenHelper.healthKaloria))
					healthScan.setHSkaloria(c.getString(c.getColumnIndex(columnName)));
				if(columnName.equals(DBOpenHelper.healthSleepDeep))
					healthScan.setHSsleepDeep(c.getString(c.getColumnIndex(columnName)));

				if(columnName.equals(DBOpenHelper.healthStepTime))
					healthScan.setHSstepTime(c.getString(c.getColumnIndex(columnName)));
				if(columnName.equals(DBOpenHelper.healthScore))
					healthScan.setHShealthScore(c.getString(c.getColumnIndex(columnName)));
				if(columnName.equals(DBOpenHelper.healthDate))
					healthScan.setHShealthDate(c.getString(c.getColumnIndex(columnName)));
				if(columnName.equals(DBOpenHelper.healthSteper))
					healthScan.setHSstepsPer(c.getString(c.getColumnIndex(columnName)));

				if(columnName.equals(DBOpenHelper.healtheartRatePer))
					healthScan.setHSsleepDeeper(c.getString(c.getColumnIndex(columnName)));

				if(columnName.equals(DBOpenHelper.healthSleepDeepPer))
					healthScan.setHSsleepDeeper(c.getString(c.getColumnIndex(columnName)));
				

				if(columnName.equals(DBOpenHelper.healthAsses))
					healthScan.setHShealthAsses(c.getString(c.getColumnIndex(columnName)));
				flag = true;
				}
			}
		}
		c.close();
		db.close();
		return healthScan;
	}
	
	
	public long insertHealthPush(Push push){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.healthPushTitle, push.getTitle());
		values.put(DBOpenHelper.healthPushcontent, push.getContent());
		values.put(DBOpenHelper.healthPushDate, push.getDate());
		values.put(DBOpenHelper.healthCollect, push.getCollect());
		//values.put(DBOpenHelper.serverTime, push.getServerTime());
		SQLiteDatabase db = helper.getWritableDatabase();
		rowId = db.insert(DBOpenHelper.healthPushTal, DBOpenHelper.healthpushId, values);
		db.close();
		return rowId;
	}
	
	
	
	public ArrayList<Sport> sportSearth(){
 		ArrayList<Sport> sports = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DBOpenHelper.pedometerTal,null); 
		sports = new ArrayList<Sport>();
		if(c!=null && c.getCount()>0){
			while(c.moveToNext()){
				Sport sport = new Sport();
				sport.setId(c.getInt(c.getColumnIndex(DBOpenHelper.pedometerId)));
				sport.setStep(c.getString(c.getColumnIndex(DBOpenHelper.steps)));
				sport.setSportTime(c.getString(c.getColumnIndex(DBOpenHelper.sportTime)));
				sport.setKoloria(c.getString(c.getColumnIndex(DBOpenHelper.kaloria)));
				sport.setDate(c.getString(c.getColumnIndex(DBOpenHelper.PDTdate)));
				sport.setStepRate(c.getString(c.getColumnIndex(DBOpenHelper.PDTpercent)));
				sports.add(sport);
			}
		} 
		db.close();
		c.close();
		return sports;
	}
	public int delete(int id){
		int  count = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenHelper.pedometerTal, "_id="+id, null);
		db.close();	
		return count;
	}
	
	public int deleteHealthStateId(int id){
		int  count = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenHelper.HealthStateTal, "_id="+id, null);
		db.close();	
		return count;
	}
	
	public int deleteHealthAdvice(int id){
		int  count = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenHelper.healthPushTal, "_id="+id, null);
		db.close();	
		return count;
	}
	public int deleteHeartRateId(int id){
		int  count = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenHelper.heartRateTal, "_id="+id, null);
		db.close();	
		return count;
	}
	
	public int deleteSleepId(int id){
		int  count = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenHelper.sleepTal, "_id="+id, null);
		db.close();	
		return count;
	}
	
	public int deleteHealthScanId(int id){
		int  count = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenHelper.healthScanTal, "_id="+id, null);
		db.close();	
		return count;
	}
	
	public int deleteScanDateFlag(){
		int  count = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenHelper.scanDateFlagTal, null, null);
		db.close();	
		return count;
	}
	
	public int deleteScanDateFlagId(int id){
		int  count = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenHelper.scanDateFlagTal, "_id="+id, null);
		db.close();	
		return count;
	}
	
	public int deletePush(){
		int  count = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenHelper.healthPushTal, null, null);
		db.close();	
		return count;
	}
	
	public ArrayList<Mac> macSearth(){
 		ArrayList<Mac> macs = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DBOpenHelper.braceTal,null); 
		macs = new ArrayList<Mac>();
		if(c!=null && c.getCount()>0){
			while(c.moveToNext()){
				Mac mac = new Mac();
				mac.setMac(c.getString(c.getColumnIndex(DBOpenHelper.mac)));
				mac.setPsword(c.getString(c.getColumnIndex(DBOpenHelper.psword)));
				macs.add(mac);
			}
		} 
		db.close();
		c.close();
		return macs;
	}
	
	public ArrayList<Register> registerSearth(){
 		ArrayList<Register> registers = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DBOpenHelper.registerTal,null); 
		registers = new ArrayList<Register>();
		if(c!=null && c.getCount()>0){
			while(c.moveToNext()){
				Register register = new Register();
				register.setName(c.getString(c.getColumnIndex(DBOpenHelper.name)));
				register.setNum(c.getString(c.getColumnIndex(DBOpenHelper.phoneNum)));
				register.setAge(c.getString(c.getColumnIndex(DBOpenHelper.age)));
				register.setSex(c.getString(c.getColumnIndex(DBOpenHelper.sex)));
				register.setHeight(c.getString(c.getColumnIndex(DBOpenHelper.height)));
				register.setWeight(c.getString(c.getColumnIndex(DBOpenHelper.weight)));
				registers.add(register);
			}
		} 
		db.close();
		c.close();
		return registers;
	}
	
	
	public ArrayList<UId> uIdSearth(){
 		ArrayList<UId> uids = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DBOpenHelper.uIdTal,null); 
		uids = new ArrayList<UId>();
		if(c!=null && c.getCount()>0){
			while(c.moveToNext()){
				UId uId = new UId();
				uId.setuId(c.getString(c.getColumnIndex(DBOpenHelper.uId)));
				uids.add(uId);
			}
		} 
		db.close();
		c.close();
		return uids;
	}
	
	
	public ArrayList<Sleep> sleepSearth(){
 		ArrayList<Sleep> sleeps = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DBOpenHelper.sleepTal,null); 
		sleeps = new ArrayList<Sleep>();
		if(c!=null && c.getCount()>0){
			while(c.moveToNext()){
				Sleep sleep = new Sleep();
				sleep.setId(c.getInt(c.getColumnIndex(DBOpenHelper.sleepId)));
				sleep.setSleepDeep(c.getString(c.getColumnIndex(DBOpenHelper.sleepDeep)));
				sleep.setSleepDate(c.getString(c.getColumnIndex(DBOpenHelper.sleepDate)));
				sleep.setSleepPercent(c.getString(c.getColumnIndex(DBOpenHelper.sleepPercent)));
				sleep.setSleepTime(c.getString(c.getColumnIndex(DBOpenHelper.sleepTime)));
				sleeps.add(sleep);
			}
		} 
		db.close();
		c.close();
		return sleeps;
	}
	
	public ArrayList<HeartRate> HeartRateSearth(){
 		ArrayList<HeartRate> heartRates = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DBOpenHelper.heartRateTal,null); 
		heartRates = new ArrayList<HeartRate>();
		if(c!=null && c.getCount()>0){
			while(c.moveToNext()){
				HeartRate heartRate = new HeartRate();
				heartRate.setId(c.getInt(c.getColumnIndex(DBOpenHelper.heartRateId)));
				heartRate.setHeartRate(c.getString(c.getColumnIndex(DBOpenHelper.heartRate)));
				heartRate.setHeartDate(c.getString(c.getColumnIndex(DBOpenHelper.HRdate)));
				heartRate.setHRpercent(c.getString(c.getColumnIndex(DBOpenHelper.HRpercent)));
				heartRate.setHeartRated(c.getInt(c.getColumnIndex(DBOpenHelper.heartRated)));
				heartRates.add(heartRate);
			}
		} 
		db.close();
		c.close();
		return heartRates;
	}
	
	public ArrayList<HealthScan> HealthScanSearth(){
 		ArrayList<HealthScan> scans = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DBOpenHelper.healthScanTal,null); 
		scans = new ArrayList<HealthScan>();
		if(c!=null && c.getCount()>0){
			while(c.moveToNext()){
				HealthScan scan = new HealthScan();
				scan.setId(c.getInt(c.getColumnIndex(DBOpenHelper.healthId)));
				scan.setHSsteps(c.getString(c.getColumnIndex(DBOpenHelper.healthSteps)));
				scan.setHSheart(c.getString(c.getColumnIndex(DBOpenHelper.healthHeart)));
				scan.setHSkaloria(c.getString(c.getColumnIndex(DBOpenHelper.healthKaloria)));
				scan.setHSsleepDeep(c.getString(c.getColumnIndex(DBOpenHelper.healthSleepDeep)));
				scan.setHSstepTime(c.getString(c.getColumnIndex(DBOpenHelper.healthStepTime)));
				scan.setHShealthScore(c.getString(c.getColumnIndex(DBOpenHelper.healthScore)));
				scan.setHShealthDate(c.getString(c.getColumnIndex(DBOpenHelper.healthDate)));
				scan.setHSstepsPer(c.getString(c.getColumnIndex(DBOpenHelper.healthSteper)));
				scan.setHSheartRatePer(c.getString(c.getColumnIndex(DBOpenHelper.healtheartRatePer)));
				scan.setHSsleepDeeper(c.getString(c.getColumnIndex(DBOpenHelper.healthSleepDeepPer)));
				scan.setHShealthAsses(c.getString(c.getColumnIndex(DBOpenHelper.healthAsses)));
				scans.add(scan);
			}
		} 
		db.close();
		c.close();
		return scans;
	}
	
	public ArrayList<Push> pushSearth(){
 		ArrayList<Push> pushs = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DBOpenHelper.healthPushTal,null); 
		pushs = new ArrayList<Push>();
		if(c!=null && c.getCount()>0){
			while(c.moveToNext()){
				Push push = new Push();
				push.setId(c.getInt(c.getColumnIndex(DBOpenHelper.healthpushId)));
				push.setTitle(c.getString(c.getColumnIndex(DBOpenHelper.healthPushTitle)));
				push.setContent(c.getString(c.getColumnIndex(DBOpenHelper.healthPushcontent)));
				push.setDate(c.getString(c.getColumnIndex(DBOpenHelper.healthPushDate)));
				push.setCollect(c.getInt(c.getColumnIndex(DBOpenHelper.healthCollect)));
			//	push.setServerTime(c.getString(c.getColumnIndex(DBOpenHelper.serverTime)));
				pushs.add(push);
			}
		} 
		db.close();
		c.close();
		return pushs;
	}
	
	public ArrayList<Status> statusSearth(){
 		ArrayList<Status> status = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DBOpenHelper.statsTal,null); 
		status = new ArrayList<Status>();
		if(c!=null && c.getCount()>0){
			while(c.moveToNext()){
				Status status2 = new Status();
				status2.setStaus(c.getInt(c.getColumnIndex(DBOpenHelper.stutas)));
				status.add(status2);
			}
		} 
		
		db.close();
		c.close();
		return status;
	}
	
	public ArrayList<SendServiceFlag> serviceFlagSearth(){
 		ArrayList<SendServiceFlag> serviceFlags = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DBOpenHelper.sendServiceTal,null); 
		serviceFlags = new ArrayList<SendServiceFlag>();
		if(c!=null && c.getCount()>0){
			while(c.moveToNext()){
				SendServiceFlag serviceFlag = new SendServiceFlag();
				serviceFlag.setRegisterFlag(c.getInt(c.getColumnIndex(DBOpenHelper.registerFlag)));
				serviceFlag.setHealthScanFlag(c.getInt(c.getColumnIndex(DBOpenHelper.healthScanFlag)));
				serviceFlags.add(serviceFlag);
			}
		} 
		db.close();
		c.close();
		return serviceFlags;
	}
	
	public ArrayList<ScandateFlag> scanDateSearth(){
 		ArrayList<ScandateFlag> scandateFlags = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DBOpenHelper.scanDateFlagTal,null); 
		scandateFlags = new ArrayList<ScandateFlag>();
		if(c!=null && c.getCount()>0){
			while(c.moveToNext()){
				ScandateFlag scandateFlag = new ScandateFlag();
				scandateFlag.setId(c.getInt(c.getColumnIndex(DBOpenHelper.scanDateFlagId)));
				scandateFlag.setDate(c.getString(c.getColumnIndex(DBOpenHelper.scanDate)));
				scandateFlags.add(scandateFlag);
			}
		} 
		db.close();
		c.close();
		return scandateFlags;
	}
	
	public ArrayList<String> serverTimeSearth(){
 		ArrayList<String> strings = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DBOpenHelper.serverTimeTal,null); 
		strings = new ArrayList<String>();
		if(c!=null && c.getCount()>0){
			while(c.moveToNext()){
				strings.add(c.getString(c.getColumnIndex(DBOpenHelper.serverReturnTime)));
			}
		} 
		db.close();
		c.close();
		return strings;
	}
	
	public List<HealthState> healthStateSearth(){
 		ArrayList<HealthState> states = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DBOpenHelper.HealthStateTal,null); 
		states = new ArrayList<HealthState>();
		if(c!=null && c.getCount()>0){
			while(c.moveToNext()){
				HealthState state = new HealthState();
				state.setId(c.getInt(c.getColumnIndex(DBOpenHelper.HealthStateId)));
				state.setBloodPressure(c.getString(c.getColumnIndex(DBOpenHelper.bloodPressure)));
				state.setBloodSugar(c.getString(c.getColumnIndex(DBOpenHelper.bloodSugar)));
				state.setBloodPressureLow(c.getString(c.getColumnIndex(DBOpenHelper.healthStateWeight)));
				state.setBloodSugarAfter(c.getString(c.getColumnIndex(DBOpenHelper.bloodSugarAfter)));
				state.setDate(c.getString(c.getColumnIndex(DBOpenHelper.healthStateDate)));
				states.add(state);
			}
		} 
		db.close();
		c.close();
		return states;
	}
}
