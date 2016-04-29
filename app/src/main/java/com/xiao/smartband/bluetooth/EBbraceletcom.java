package com.xiao.smartband.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class EBbraceletcom {
	private BluetoothAdapter mBtAdapter = null;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
    private static EBEventHander BTHandler;
    private BluetoothDevice device;
    private Boolean bStop = true;
    public static final int BT_EVENT_DEVICE_SCANNING = 1;       
    public static final int BT_EVENT_DEVICE_PAIRING = 2;     
    public static final int BT_EVENT_DEVICE_CONNECTING = 3; 
    public static final int BT_EVENT_DEVICE_CONNECTED = 4;  
    public static final int BT_EVENT_DEVICE_DISCONNET = 5;  
    public static final int FAILED = 5;
    public static int nCurState = 0;
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        	UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");    
	public EBbraceletcom() {
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		mBtAdapter.enable();
		nCurState = 0;
	}
	public int GetCommState(){
		return nCurState;
	}
	public int SetCommState(int nState){
		int nOldState = nCurState;
		nCurState = nState;
		return nOldState;
	}
	//搜索设备
	public void ScanDevice(){
		
		if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
		mBtAdapter.startDiscovery();
		
		BTHandler.sendEventMessage(BT_EVENT_DEVICE_SCANNING);
	}; 
	
	public void CancelScanDevice(){
		
		if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
	}; 
	//搜索设备服务
	public void ScanDeviceSerice(){ 
		BTHandler.sendEventMessage(BT_EVENT_DEVICE_SCANNING);
	}; 
	public void PairDevice(BluetoothDevice device){
//		mBtAdapter.cancelDiscovery();
		this.device = device;
		//device.createBond();
//		BluetoothDevice btDev = mBtAdapter.getRemoteDevice(MyApplication.address);
		//Log.i("info", "device.getAddress()="+device.getAddress());
		Boolean returnValue = false; 
		try {
			Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
			returnValue = (Boolean) createBondMethod.invoke(device);  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		if(returnValue.booleanValue() == true)
			BTHandler.sendEventMessage(BT_EVENT_DEVICE_PAIRING);//����Ժã�������Ϣ
		else{
			ConnectDevice(true);
		}
		
	};
	public Set<BluetoothDevice> GetPairedDevices(){
		return mBtAdapter.getBondedDevices();
	}
	
	 /*static public boolean createBond(Class<?> btClass, BluetoothDevice btDevice)  
	    throws Exception  
	    {  
	       // Method createBondMethod = btClass.getMethod("createBond" , null); 
		 	Method createBondMethod = btClass.getMethod("createBond"); 
	        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);  
	        return returnValue.booleanValue();  
	    }  
*/
	//连接设备
	public void ConnectDevice(boolean secure){
		//String address = data.getExtras().getString(MyApplication.address);//�豸��ַ
 //       BluetoothDevice device = mBtAdapter.getRemoteDevice(MyApplication.address);
        connect(device, secure);
		BTHandler.sendEventMessage(BT_EVENT_DEVICE_CONNECTING);
	}; 
	//连接设备
	public void ConnectDevice(BluetoothDevice device, boolean secure){
		//String address = data.getExtras().getString(MyApplication.address);//�豸��ַ
        connect(device, secure);
		BTHandler.sendEventMessage(BT_EVENT_DEVICE_CONNECTING);//l�Ӻõ��߳� 
	}; 
	
	//监听
	public void Start(final EBEventHander hander){
		EBEventHander oldHandler = BTHandler;
		BTHandler = hander;
		if(oldHandler == null){
			ScanDevice();
		}else {
			BTHandler.sendEventMessage(BT_EVENT_DEVICE_SCANNING);
		}
		
	//	BTHandler.sendEventMessage(0);
	};
	
	public void Stop(){
		bStop = true;
        if(BTHandler != null)BTHandler.ClearAllMessage();
        BTHandler = null;
	 }; 
	//发送数据
	public void Write(short [] data, int nLen){
		
		if (mConnectedThread != null) {
			byte [] sendData = new byte[nLen * 2];
			for(int x = 0; x < data.length; x++){
				sendData[x * 2] = (byte)(data[x] & 0xff);
				sendData[x * 2 + 1] = (byte)(data[x] >> 8);
			}
			mConnectedThread.write(sendData);
        }
	};
	
	public void Write(byte[] out, int nLen) {
        ConnectedThread r;
        if(mConnectedThread != null){
        	 synchronized (this) {
                 r = mConnectedThread;
             }
             r.write(out);
        }else if(BTHandler != null) BTHandler.sendEventMessage(BT_EVENT_DEVICE_DISCONNET);//重新连接
    }
	
	
	public synchronized void connect(BluetoothDevice device, boolean secure) {
	        if (mConnectThread != null) {
	            mConnectThread.cancel(); 
	            mConnectThread = null;}
        

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
        	mConnectedThread.cancel(); 
        	mConnectedThread = null;}

        // Start the thread to connect with the given device
 //       mBtAdapter.cancelDiscovery();
        bStop = false;
        mConnectThread = new ConnectThread(device, secure);
        mConnectThread.start();
    }
	
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;
        
        public ConnectThread(BluetoothDevice device, boolean secure) {
        	
            mmDevice = device;
            BluetoothSocket tmp = null; 
            mSocketType = secure ? "Secure" : "Insecure";
          //  int sdk = Integer.parseInt(Build.VERSION.SDK);
            try {
            	if(device == null) return;
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
                } else {
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID_INSECURE);
                }
            	
            	/*if (sdk >= 10) {
            		tmp = device.createInsecureRfcommSocketToServiceRecord(uuid);
            	 } else {
            		 tmp = device.createRfcommSocketToServiceRecord(uuid);
            	 }*/
            } catch (IOException e) {
                Log.e("info", "Socket Type: " + mSocketType + "create() failed", e);
            }
            mmSocket = tmp;
        }
        
        public void run() {
         //   Log.i("info", "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

              Log.i("info", "GGYY");
            // Always cancel discovery because it will slow down a connection
            mBtAdapter.cancelDiscovery();
            int i = 0;
            for(i = 0; i < 5 && bStop == false; i++){
            	if(mmSocket != null){
	            	boolean bConnt = true;
	                // Make a connection to the BluetoothSocket
	                try {
	                    // This is a blocking call and will only return on a
	                    // successful connection or an exception
	                	mmSocket.connect();
	                } catch (IOException e) {
	                    bConnt = false;
	                    // Close the socket
	                    try {
	                    	mmSocket.close();
	                    } catch (IOException e2) {
	                        Log.e("info", "unable to close() " + mSocketType +
	                                " socket during connection failure", e2);
	                    }

	                    if(bStop == true) break;
	                    try {
	        				Thread.sleep(1200);
	        				
	        			} catch (InterruptedException e1) {
	        				// TODO Auto-generated catch block
	        				e1.printStackTrace();
	        			}
	                }
	                if(bConnt == true) break;
            	}
            	mmSocket = null;
                BluetoothSocket tmp = null;
                try {
                	if(mmDevice != null){
                		if (mSocketType.equals("Secure") ) {
                        	tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
                        } else {
                        	tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID_INSECURE);
                        }
                	}
                 } catch (IOException e) {
                    Log.e("info", "Socket Type: " + mSocketType + "create() failed", e);
                }
                mmSocket = tmp;
            }
            if(i >= 5 || bStop == true){
            	if(BTHandler != null)
            		BTHandler.sendEventMessage(FAILED); 
            	if(mmSocket != null){
	            	try {
						mmSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	return; 
            }


            // Reset the ConnectThread because we're done
            synchronized (this) {
                mConnectThread = null;
            }

            // Start the connected thread
            //l�Ӻ�ķ��� 
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
            	if(mmSocket != null) mmSocket.close();
            } catch (IOException e) {
                Log.e("info", "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }
  
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            Log.d("info", "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            bStop = false;
            
            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("info", "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        
        public void run() {
            //Log.i("info", "BEGIN mConnectedThread");
            Log.i("info", "running");
            int bytes;
           // int sss = device.getType();
            while (bStop != true) {
				//byte[] buffer = new byte[130];
				byte[] buffer = new byte[4];
               try {
                	bytes = mmInStream.available();
                	//Log.i("info", "mConnectedThread="+mConnectedThread.isAlive());
                	//Log.i("info", "bytes="+bytes);
                	if(bytes <= 0){
                		Thread.sleep(2);
                		continue;
                	}
                    bytes = mmInStream.read(buffer);
                    if(BTHandler != null) BTHandler.sendReadMessage(buffer, bytes);
                    else break;
                    
                    
                  //  mHandler.obtainMessage(BluetoothChat.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                } catch (Exception e) {//IOException
                    Log.e("info", "disconnected=", e);
                  //  Toast.makeText(MyApplication.context, "l���ѶϿ�...", 3000).show();
                  //  connectionLost();
                    break;
                }
            }
            DisConnect();
            this.cancel();
            mConnectedThread = null;
        }
        public void DisConnect() {
            Log.i("info", "DisConnect mConnectedThread");
            try {
				mmInStream.close();
				mmOutStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
 
            try {
				mmSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
        
        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
            try {
            	if(mmOutStream != null){
                mmOutStream.write(buffer);
            	}
                // Share the sent message back to the UI Activity
                //mHandler.obtainMessage(BluetoothChat.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
            } catch (IOException e) {
                Log.e("info", "断开连接", e);
                Log.i("info", "mmOutStream="+mmOutStream);
                Log.i("info", "mConnectedThread="+mConnectedThread.isAlive());
                Log.i("info", "mConnectedThread="+mConnectedThread.isInterrupted());
                if(BTHandler != null) {
                	BTHandler.sendEventMessage(BT_EVENT_DEVICE_DISCONNET);
                }
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("info", "close() of connect socket failed", e);
            }
        }
    }
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device, final String socketType) {

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, socketType);
        mConnectedThread.start();//��l�Ӻ���߳�
        BTHandler.sendEventMessage(BT_EVENT_DEVICE_CONNECTED);
    }
    
}
