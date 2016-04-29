package com.xiao.smartband.bluetooth;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class EBEventHander {
    private Handler handler;
    public EBEventHander() {
        // Set up a handler to post events back to the correct thread if possible
        if(Looper.myLooper() != null) {
            handler = new Handler(){
                public void handleMessage(Message msg){
                	EBEventHander.this.handleMessage(msg);
                }
            };
        }
    }
    public void OnBtEvent(int nEvent){
    	
    };
    public void  ScanDevice(int nEvent){
    	
    };
    public void ScanDeviceSerice(int nEvent){
    	
    };
    public void PairDevice(int nEvent){
    	
    };
    public void ConnectDevice(int nEvent){
    	
    };
    public void OnRecv(short[] sample,int lenght){
		
	}
    public void OnRecv(byte[] sample,int lenght){
		
	}
	public void OnError(short[] sample){
		
	}

    protected void sendReadMessage(short []data, int nlen) {
        sendMessage(obtainMessage(0, data, nlen));
    }

    protected void sendReadMessage(byte []data, int nlen) {
        sendMessage(obtainMessage(1, data, nlen));
    }
    protected void sendEventMessage(int nEvent) {
        sendMessage(obtainMessage(3, nEvent));
    }
    protected void sendScanDeviceMessage(int nEvent) {
        sendMessage(obtainScanDeviceMessage(4, nEvent));
    }
    protected void sendScanDeviceSericeMessage(int nEvent) {
        sendMessage(obtainScanDeviceSericeMessage(5, nEvent));
    }
    protected void sendPairDeviceMessage(int nEvent) {
        sendMessage(obtainPairDeviceMessage(6, nEvent));
    }
    protected void sendConnectDeviceMessage(int nEvent) {
        sendMessage(obtainConnectDeviceMessage(7, nEvent));
    }
    protected void sendErrorMessage(short []data, int nlen) {
        sendMessage(obtainMessage(2, data, nlen));
    }
    protected void ClearAllMessage(){
    	handler.removeMessages(0);
    	handler.removeMessages(1);
    }

    // Methods which emulate android's Handler and Message methods
    protected void handleMessage(Message msg) {

        switch(msg.what) {
            case 0:
            {
                short []sample = (short[])msg.obj;
                OnRecv(sample, msg.arg1);
            }
                break;
            case 1:
            {
                byte []sample = (byte[])msg.obj;
                OnRecv(sample, msg.arg1);
            }
                break;
            case 2:
                OnError(null);
                break;
            case 3:
            	OnBtEvent(msg.arg1);
                break;
            case 4:
            	ScanDevice(msg.arg1);
                break;
            case 5:
            	ScanDeviceSerice(msg.arg1);
                break;
            case 6:
            	PairDevice(msg.arg1);
                break;
            case 7:
            	ConnectDevice(msg.arg1);
                break;
       }
    }

    protected void sendMessage(Message msg) {
        if(handler != null){
            handler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
    }

    protected Message obtainMessage(int responseMessage, short []data, int nlen) {
        Message msg = null;
        if(handler != null){
            msg = this.handler.obtainMessage(responseMessage, nlen, -1, data);
        }else{
            msg = new Message();
            msg.what = responseMessage;
            msg.obj = data;
        }
        return msg;
    }
    protected Message obtainMessage(int responseMessage, byte []data, int nlen) {
        Message msg = null;
        if(handler != null){
            msg = this.handler.obtainMessage(responseMessage, nlen, -1, data);
        }else{
            msg = new Message();
            msg.what = responseMessage;
            msg.obj = data;
        }
        return msg;
    }

    protected Message obtainMessage(int responseMessage, int nEvent) {
        Message msg = null;
        if(handler != null){
            msg = this.handler.obtainMessage(responseMessage, nEvent, -1, null);
        }else{
            msg = new Message();
            msg.what = responseMessage;
            msg.obj = nEvent;
        }
        return msg;
    }
    protected Message obtainScanDeviceMessage(int responseMessage, int nEvent) {
        Message msg = null;
        if(handler != null){
            msg = this.handler.obtainMessage(responseMessage, nEvent, -1, null);
        }else{
            msg = new Message();
            msg.what = responseMessage;
            msg.obj = nEvent;
        }
        return msg;
    }
    protected Message obtainScanDeviceSericeMessage(int responseMessage, int nEvent) {
        Message msg = null;
        if(handler != null){
            msg = this.handler.obtainMessage(responseMessage, nEvent, -1, null);
        }else{
            msg = new Message();
            msg.what = responseMessage;
            msg.obj = nEvent;
        }
        return msg;
    }
    protected Message obtainPairDeviceMessage(int responseMessage, int nEvent) {
        Message msg = null;
        if(handler != null){
            msg = this.handler.obtainMessage(responseMessage, nEvent, -1, null);
        }else{
            msg = new Message();
            msg.what = responseMessage;
            msg.obj = nEvent;
        }
        return msg;
    }
    protected Message obtainConnectDeviceMessage(int responseMessage, int nEvent) {
        Message msg = null;
        if(handler != null){
            msg = this.handler.obtainMessage(responseMessage, nEvent, -1, null);
        }else{
            msg = new Message();
            msg.what = responseMessage;
            msg.obj = nEvent;
        }
        return msg;
    }
}
