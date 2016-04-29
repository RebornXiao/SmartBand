package com.xiao.smartband;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class ElderService extends Service {
    private static final String TAG = "MyService";
	private Handler handler;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    	//activity = new MainActivity();
       // Toast.makeText(this, "My Service created", Toast.LENGTH_LONG).show();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "My Service Stoped", Toast.LENGTH_LONG).show();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onStart(final Intent intent, int startid) {
    	    handler = new Handler() {
    	    public void handleMessage(Message msg) {
    	    	Intent intent = new Intent();
    	    	intent.setAction("com.zhg.elder");
    	    	intent.putExtra("msg", "这是我发送的广播");
    	    	sendBroadcast(intent);
    	    	super.handleMessage(msg);
    	    }
    	};

    	new Thread(new MyThread()).start();
    	
        Log.i(TAG, "onStart");
    }
    class MyThread implements Runnable {
	    @Override
	    public void run() {
	        while (true) {
	            try {
	                Message message = new Message();
	                message.what = 1;
	                handler.sendMessage(message);
	                Thread.sleep(60000*60);
	                Log.i("info", "hello");
	            } catch (InterruptedException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	    }
	}
}