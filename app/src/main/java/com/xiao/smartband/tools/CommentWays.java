package com.xiao.smartband.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextPaint;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class CommentWays {

	private Calendar ca;
	private int year,month,day ;
	private static ProgressDialog dialog;
	static String rootPath = Environment.getExternalStorageDirectory().getPath();  //��ȡSD���ĸ�Ŀ¼
	
	private Toast toast ;
	   public  void ToastShow(Context context,String msg){
		  if (toast == null)
				toast = Toast.makeText(context, msg, 3000);
			toast.setGravity(Gravity.CENTER, 10, 50);
			toast.setText(msg);
			toast.setDuration(3000);
			toast.show();
	   }
	   
	 /* public static void NetWork(){
		    String strVer=GetVersion.GetSystemVersion();
			strVer=strVer.substring(0,3).trim();
			float fv=Float.valueOf(strVer);
			if(fv>2.3){
			  StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			 .detectDiskReads().detectDiskWrites().detectNetwork() .penaltyLog().build()); 
			  StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().
			  penaltyLog().penaltyDeath().build());  
			 }
	   }*/
	  public String getDate(){
		    ca = Calendar.getInstance();
			year = ca.get(Calendar.YEAR);
			month=ca.get(Calendar.MONTH);
			day=ca.get(Calendar.DATE);
			String nmonth = String.format("%02d", month + 1);
			String nday = String.format("%02d", day);
			String date = year +"-"+ nmonth +"-"+nday;
			return date;
	   }
	  public void setBold(TextView tv){
			
			TextPaint tp = tv.getPaint(); 
			tp.setFakeBoldText(true);
	  }
	  /*public int getnDate(){
		    ca = Calendar.getInstance();
			year = ca.get(Calendar.YEAR);
			month=ca.get(Calendar.MONTH);
			day=ca.get(Calendar.DATE);
			return year*10000+(month+1)*100 + day;
	  }
	   public int getYear(){
		    ca = Calendar.getInstance();
			year = ca.get(Calendar.YEAR);
			return year;
	   }
	   public int getMonth(){
		    ca = Calendar.getInstance();
			month=ca.get(Calendar.MONTH);
			return (month+1);
	   }

	   public int getDay(){
		    ca = Calendar.getInstance();
			day=ca.get(Calendar.DATE);
			return day;
	   }*/
	  public  void dialog(Context context){
		 		dialog = new ProgressDialog(context);
			    dialog.setMessage("数据加载中，请稍候..");
			    dialog.setCancelable(true);
			    dialog.setIndeterminate(false);
			    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			    dialog.setMax(100);
			    dialog.show();
		       // dialog.setProgress(value);//设置在dialog.show之后，才有作用
	  }
	  
	  
	public static void DialogCancel(){
	    if(dialog != null)
	       dialog.cancel();
	       dialog = null;
	}

	  /** 
	 * 判断当前网络是否是wifi网络 
	* if(activeNetInfo.getType()==ConnectivityManager.TYPE_MOBILE) { //判断3G网 
	* 
	 * @param context 
	 * @return boolean 
	 */ 
			public static  boolean isWifi(Context context) { 
				 ConnectivityManager connectivityManager = (ConnectivityManager) context 
				 .getSystemService(Context.CONNECTIVITY_SERVICE); 
				 NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo(); 
				 if (activeNetInfo != null&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) { 
					 return true; 
				 } 
				 return false; 
				 } 
			public static boolean is3G(Context context) { 
				 ConnectivityManager connectivityManager = (ConnectivityManager) context 
				 .getSystemService(Context.CONNECTIVITY_SERVICE); 
				 NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();  
				 if(activeNetInfo != null && activeNetInfo.getType()==ConnectivityManager.TYPE_MOBILE) { 
					 return true;
				 }
				 return false; 
				 } 
			public static String Write(String name){
				String path1 = rootPath +"/"+"user"+"/";
				File file = new File(path1);
				if(!file.exists()){
					file.mkdirs();
				}
				String path2 =path1+"UserName.txt";
				try {
					File files = new File(path2);
					if(!files.exists()){
						files.createNewFile();
					}
					FileOutputStream os = new FileOutputStream(files);
					byte[] buf=name.getBytes(); 
					os.write(buf);
					os.close();
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return name;
			
			}
			//������ȡ�û�������
			@SuppressLint("NewApi")
			public static String read(){
				//String path = rootPath +"/"+"user"+"/";
				String path = rootPath +"/"+"user"+"/";
				File file = new File(path);
				if(!file.exists()){
					file.mkdirs();
				}
				String name = "";
				String path1 = path+"UserName.txt";
				try{
				File files = new File(path1);
				if(!files.exists()){
					files.createNewFile();
				}
				FileInputStream fin = new FileInputStream(files);
				int length = fin.available();
				byte [] buffer = new byte[length];
				fin.read(buffer);
				name = EncodingUtils.getString(buffer, "uft");//EncodingUtils
				fin.close();
				} catch (Exception e) {
					e.printStackTrace();
					}
				if(name == null || name.isEmpty() == true) 
					name = "0";

				return name;
			}
			
			public String isRunningForeground(Context context){
		        String packageName=getPackageName(context);
		        String topActivityClassName=getTopActivityName(context);
		        System.out.println("packageName="+packageName+",topActivityClassName="+topActivityClassName);
		      if(packageName!=null&&topActivityClassName!=null&&topActivityClassName.startsWith(packageName)) {
		            System.out.println("---> isRunningForeGround");
		         //   return true;
		        } else {
		            System.out.println("---> isRunningBackGround");
		          //  return false;
		        }
				return topActivityClassName;
		    }
		     
		     
		    public  String getTopActivityName(Context context){
		         String topActivityClassName=null;
		         ActivityManager activityManager =(ActivityManager)
		         (context.getSystemService(Context.ACTIVITY_SERVICE )) ;
		         List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1) ;
		       if(runningTaskInfos != null){
		          ComponentName f=runningTaskInfos.get(0).topActivity;
		          topActivityClassName=f.getClassName();
		         }
		         return topActivityClassName;
		    }
		     
		    public String getPackageName(Context context){
		         String packageName = context.getPackageName();  
		         return packageName;
		       }
}
