package com.xiao.smartband.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DBNAME = "bracelet.db";
	/**
	 *mac&password表 
	 */
	public static final String braceTal = "braceTal";
	public static String braceId = "_id";
	public static String mac = "mac";
	public static String psword = "psword";
	/**
	 *保存状态表 
	 */

	public static final String statsTal = "statsTal";
	public static String stutas = "stutas";
	public static String statusId = "_id";
	
	/**
	 * 健康扫描标识表,如果健康扫描已向手环取昨天的数据，标志为1，否则为0
	 */
	public static final String scanFlagTal = "scanFlagTal";
	public static String scanFlagId = "_id";
	public static String date = "date";
	public static String flag = "flag";
	
	
	/**
	 * 健康浏览，假如没有网络，日期会保存一次
	 */
	public static final String scanDateFlagTal = "scanDateFlagTal";
	public static String scanDateFlagId = "_id";
	public static String scanDate = "scanDate";
	
	
	/**
	 * 睡眠标识表,如果健康扫描已向手环取今天的数据，标志为1，否则为0
	 */
	public static final String sleepFlagTal = "sleepFlagTal";
	public static String sleepFlagId = "_id";
	public static String sleepDateFlag = "sleepDateFlag";
	//public static String flag = "flag";
	
	
	/**
	 * 判断上传注册信息，如不成功，则重新提交
	 */
	public static final String sendServiceTal = "sendServiceTal";
	public static String sendServiceId = "_id";
	public static String registerFlag = "registerFlag";
	public static String healthScanFlag = "healthScanFlag";
	
	/**
	 *注册表
	 */
	public static final String registerTal = "registerTal";
	public static String registerId = "_id";
	public static String name = "name";
	public static String phoneNum = "num";
	public static String age = "age";
	public static String sex = "sex";
	public static String height = "height";
	public static String weight = "weight";
	
	/**
	 *注册成功后的uId 
	 */
	public static final String uIdTal = "uIdTal";
	public static String uIdId = "_id";
	public static String uId = "uId";
	/**
	 *计步,运动时间,卡路里表 
	 */
	public static final String pedometerTal = "pedometerTal";
	public static String pedometerId = "_id";
	public static String steps = "steps";
	public static String sportTime = "sportTime";
	public static String kaloria = "kaloria";
	public static String PDTdate = "PDTdate";
	public static String PDTpercent = "PDTpercent";//完成率
	
	
	
	/**
	 *心率表 
	 */
	public static final String heartRateTal = "heartRateTal";
	public static String heartRateId = "_id";
	public static String heartRate = "heartRate";
	public static String HRdate = "HRdate";
	public static String HRpercent = "HRpercent";
	public static String heartRated = "heartRated";
	

	/**
	 *睡眠表 
	 */
	public static final String sleepTal = "sleepTal";
	public static String sleepId = "_id";
	public static String sleepDeep = "sleepDeep";//深睡时间
	public static String sleepTime = "sleepTime";//总睡眠时间
	public static String sleepDate = "sleepDate";
	public static String sleepPercent = "sleepPercent";
	
	
	/**
	 *健康扫描表 
	 */
	public static final String healthScanTal = "healthScanTal";
	public static String healthId = "_id";
	public static String healthSteps = "healthSteps";
	public static String healthHeart = "healthHeart";
	public static String healthKaloria = "healthKaloria";
	public static String healthSleepDeep = "healthSleepDeep";
	public static String healthStepTime = "healthStepTime";
	public static String healthScore = "healthScore";
	public static String healthDate = "healthDate";
	public static String healthSteper = "healthSteper";
	public static String healtheartRatePer = "healtheartRatePer";
	public static String healthSleepDeepPer = "healthSleepDeepPer";
	public static String healthAsses = "healthAsses";
	
 	
	
	/**
	 *健康推送表 
	 */
	public static final String healthPushTal = "healthPushTal";
	public static String healthpushId = "_id";
	public static String healthPushTitle = "healthPushTitle";
	public static String healthPushcontent = "healthPushcontent";
	public static String healthPushDate = "healthPushDate";
	public static String healthCollect = "healthCollect";
	//public static String serverTime = "serverTime";
	
	/**
	 *健康推送，服务器返回时间
	 */

	public static final String serverTimeTal = "serverTimeTal";
	public static String serverTimeId = "_id";
	public static String serverReturnTime = "serverReturnTime";
	
	/**
	 *今天健康状况 
	 */
	public static final String HealthStateTal = "HealthStateTal";
	public static String HealthStateId = "_id";
	public static String bloodPressure = "bloodPressure";
	public static String bloodSugar = "bloodSugar";
	public static String bloodSugarAfter = "bloodSugarAfter";
	public static String healthStateWeight = "healthStateWeight";
	public static String healthStateDate = "healthStateDate";
	
	public DBOpenHelper(Context context){
 		super(context,DBNAME,null,9);
	}
	
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String macSql = "create table if not exists " + braceTal + "(" +
				"_id integer primary key autoincrement,mac text,psword text"+
				")";
		db.execSQL(macSql);
		
		
		String statsSql = "create table if not exists " + statsTal + "(" +
				"_id integer primary key autoincrement,stutas integer"+
				")";
		db.execSQL(statsSql);
		
		String scanFlagSql = "create table if not exists " + scanFlagTal + "(" +
				"_id integer primary key autoincrement,date integer,flag integer"+
				")";
		db.execSQL(scanFlagSql);
		
		String scanDateFlagSql = "create table if not exists " + scanDateFlagTal + "(" +
				"_id integer primary key autoincrement,scanDate text"+
				")";
		db.execSQL(scanDateFlagSql);
		
		
		String sleepFlagSql = "create table if not exists " + sleepFlagTal + "(" +
				"_id integer primary key autoincrement,sleepDateFlag integer"+
				")";
		db.execSQL(sleepFlagSql);
		
		
		String sendServiceSql = "create table if not exists " + sendServiceTal + "(" +
				"_id integer primary key autoincrement,registerFlag integer,healthScanFlag integer"+
				")";
		db.execSQL(sendServiceSql);
		
		
		String registerSql = "create table if not exists " + registerTal + "(" +
				"_id integer primary key autoincrement,name text,num text,"+
				"age text,sex text,height text,weight text"+
				")";
		db.execSQL(registerSql);
		
		String uIdSql = "create table if not exists " + uIdTal + "(" +
				"_id integer primary key autoincrement,uId text"+
				")";
		db.execSQL(uIdSql);
		
		
		String pedometersql = "create table if not exists " + pedometerTal + "(" +
				"_id integer primary key autoincrement,name text,steps text,"+
				"sportTime text,kaloria text,PDTdate text,PDTpercent text"+
				")";
		db.execSQL(pedometersql);
		
		String heartRateSql = "create table if not exists " + heartRateTal + "(" +
				"_id integer primary key autoincrement,heartRate text,HRdate text," +
				"HRpercent text,heartRated integer"+
				")";
		db.execSQL(heartRateSql);
		
		String sleepSql = "create table if not exists " + sleepTal + "(" +
				"_id integer primary key autoincrement,sleepDeep text,sleepTime text,sleepDate text,sleepPercent text"+
				")";
		db.execSQL(sleepSql);
		
		String healthSql = "create table if not exists " + healthScanTal + "(" +
				"_id integer primary key autoincrement,healthDate text,healthSteps text,"+
				"healthHeart text,healthKaloria text,healthSleepDeep text,healthStepTime text,"+
				"healthScore text,healthSteper text,healtheartRatePer text,healthSleepDeepPer text,healthAsses text"+
				")";
		db.execSQL(healthSql);
		
		String healthPushSql = "create table if not exists " + healthPushTal + "(" +
				"_id integer primary key autoincrement,healthPushTitle text,healthPushcontent text,"+
				"healthPushDate text,healthCollect integer"+
				")";
		db.execSQL(healthPushSql);
		
		String healthStateSql = "create table if not exists " + HealthStateTal + "(" +
				"_id integer primary key autoincrement,bloodPressure text,bloodSugar text,"+
				"healthStateWeight text,healthStateDate text,bloodSugarAfter text"+
				")";
		db.execSQL(healthStateSql);
		
		String serverTimeSql = "create table if not exists " + serverTimeTal + "(" +
				"_id integer primary key autoincrement,serverReturnTime text"+
				")";
		db.execSQL(serverTimeSql);
	}
	
	
	/*@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.i("info", "DBOpenHelper.onUpgrade()");
		onCreate(db);
	}*/
	    //定义升级函数
		private void upgradeDatabaseToVersion1(SQLiteDatabase db) {        
			// Add 'new' column to mytable table. 
			//当更新的时候要把上一次更新的删除，否则会报  重复添加字段 一次.
 //			db.execSQL("ALTER TABLE healthPushTal ADD COLUMN healthCollect integer");
//			db.execSQL("ALTER TABLE HealthStateTal ADD COLUMN bloodSugarAfter TEXT");
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			//oldVersion = 0;
			 switch (oldVersion) {     
			 case 8://假如旧版本号是4，新版本号是5         
			  if (newVersion <= 1) {                
				  return;            
				  }            
				 db.beginTransaction();            
				 try {               
					 upgradeDatabaseToVersion1(db);                
					 db.setTransactionSuccessful();           
					 } catch (Throwable ex) {               
					   Log.e("info", ex.getMessage(), ex);              
					   break;           
					 } finally {               
					   db.endTransaction();            
						}	   
				     return;   
				 }   
			 Log.e("info", "Destroying all old data.");   
			 //dropAll(db);    
			 onCreate(db);
		}
}
