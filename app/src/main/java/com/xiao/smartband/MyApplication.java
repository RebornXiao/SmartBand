package com.xiao.smartband;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.xiao.smartband.bluetooth.EBbraceletcom;
import com.xiao.smartband.entity.Push;
import com.xiao.smartband.tools.CommentWays;
import com.xiao.smartband.tools.Util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {
	private	static  MyApplication instance;
	public static ArrayList<Push> pushs;
	//普通的健康建议信息
	public static ArrayList<Push> pushsNomal;
	//已收藏的健康建议信息
	public static ArrayList<Push> pushsCollect = new ArrayList<Push>();
	public static ArrayList<Push> temPushs = new ArrayList<Push>();
    //目的是当前界面销毁，在上一个界面执行onResume方法,重新刷新数据
    public static boolean adviceDel = false;
    //此标志判断是普通建议的详情，还是收藏建议的详情,默认是普通建议标志
    public static boolean adviceNomalorCollect = false;
	public static CommentWays ways ;
	public static boolean CONNETET = false;
	public  byte[] arr = new byte[4];
	private List<Activity> activityList=new LinkedList<Activity>();
    public static Context mcontext = null;
    public static int activityStuts = 0;
    public static EBbraceletcom eBbraceletcom;
    public static int style = 0 ;
    public static int registerValue;
    public static String mac;
    public static String pswd = "";
    public static String date = "";
    public static Util util = null;
//    public static boolean healthScanInit = false;
	private MyApplication(){
		eBbraceletcom = new EBbraceletcom();
	}
	public static MyApplication getInstance(){
		  getWays();
		if(null == instance){
			instance = new MyApplication();
		}
		return instance;
	}
	public static  void getWays(){
		if(ways == null)
		   ways = new  CommentWays();
	}
	/**
	 * 退出程序使用
	 */
	 //添加Activity 到容器中
	 public void addActivity(Activity activity)
	 {
		 activityList.add(activity);
	 }
	 //遍历所有Activity 并finish
	
	 public void exit()
	 {
	   for(Activity activity:activityList){
	 	 activity.finish();
	 }
	}
	 private boolean ExistSDCard() {  
	  if (android.os.Environment.getExternalStorageState().equals(  
		  android.os.Environment.MEDIA_MOUNTED)) {  
		    return true;  
		   } else{
			return false; 
		  } 
	  }

}
