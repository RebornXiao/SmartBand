package com.xiao.smartband.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiao.smartband.ElderService;
import com.xiao.smartband.MyApplication;
import com.xiao.smartband.R;
import com.xiao.smartband.bluetooth.EBEventHander;
import com.xiao.smartband.bluetooth.EBbraceletcom;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.Mac;
import com.xiao.smartband.entity.SendServiceFlag;
import com.xiao.smartband.entity.Status;
import com.xiao.smartband.protocal.CmdProtocol;
import com.xiao.smartband.tools.CommentWays;
import com.xiao.smartband.tools.Util;
import com.xiao.smartband.view.TitleView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends Activity implements OnClickListener {
    private TitleView titleView;
    private TextView tvnext;
    private ImageView ivsteps, ivsportTime, ivkaloria, ivBloodSugar;
    private ImageView ivsleep, ivheartRate, ivhealtyScan, ivhealtyAdvice;
    private EditText etinputMac, etinputMacAagin;
    private BluetoothDevice device = null;
    static private BtDeviceReceiver mReceiver;
    private BluetoothAdapter mBtAdapter
            = BluetoothAdapter.getDefaultAdapter();
    private ArrayList<Status> status;
    private static Handler handler;
    private String nmac, nmacAgain;
    private ModelDao model;
    private Calendar ca;
    private int year, month, day;
    private int hour, minute, seconds;
    private int Times = 0;
    private int CCC = 0;
    private boolean bcFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        model = new ModelDao(getApplicationContext());
        status = model.statusSearth();
        Log.i("info", "satus.size=" + status.size());
        ca = Calendar.getInstance();
        year = ca.get(Calendar.YEAR);
        month = ca.get(Calendar.MONTH);//+1
        day = ca.get(Calendar.DATE);
        hour = ca.get(Calendar.HOUR_OF_DAY);
        minute = ca.get(Calendar.MINUTE);//+1
        seconds = ca.get(Calendar.SECOND);
//		if(status.size() > 0){
        if (status.size() == 0) {
            setContentView(R.layout.main_activity);
            MyApplication.getInstance().addActivity(this);
            init();
            ArrayList<Mac> macs = model.macSearth();
            if (macs.size() > 0) {
                MyApplication.mac = macs.get(0).getMac();
                MyApplication.pswd = macs.get(0).getPsword();
            }
            connetBtDeice();
        } else {
            setContentView(R.layout.bracelet_code);
            MyApplication.getInstance().addActivity(this);
            titleView = (TitleView) findViewById(R.id.titleview);
            titleView.setbg(R.drawable.register_titlebg);
            titleView.setTitle(R.string.inputCode);
            tvnext = (TextView) findViewById(R.id.tvcode_next);
            etinputMac = (EditText) findViewById(R.id.etinputMac);
            etinputMacAagin = (EditText) findViewById(R.id.etinputMacAgain);
            tvnext.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    nmac = etinputMac.getText().toString();
                    nmacAgain = etinputMacAagin.getText().toString();
                    if (!nmac.equals("") && !nmacAgain.equals("")) {
                        if (nmac.equals(nmacAgain)) {
                            if (nmac.length() == 12) {
                                MyApplication.mac = nmac;//必须要的
                            } else {
                                MyApplication.mac = nmac.substring(0, 12);//必须要的
                                MyApplication.pswd = nmac.substring(12, nmac.length());//必须要的
                            }
                            connetBtDeice();
                        } else {
                            MyApplication.ways.ToastShow(getApplicationContext(), "验证码不一致");
                        }
                    } else {
                        MyApplication.ways.ToastShow(getApplicationContext(), "验证码不能为空");
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void init() {

        MyApplication.getInstance();
        if (MyApplication.util == null) MyApplication.util = new Util(getApplicationContext());
        ivsteps = (ImageView) findViewById(R.id.ivsteps);
        ivsportTime = (ImageView) findViewById(R.id.ivsportTime);
        ivkaloria = (ImageView) findViewById(R.id.ivkaloria);
        ivsleep = (ImageView) findViewById(R.id.ivsleepTime);
        ivheartRate = (ImageView) findViewById(R.id.ivheartRate);
        ivhealtyScan = (ImageView) findViewById(R.id.ivhealthScan);
        ivhealtyAdvice = (ImageView) findViewById(R.id.ivhealthAdvice);
        ivBloodSugar = (ImageView) findViewById(R.id.ivBloodSugar);
        ivBloodSugar.setOnClickListener(this);
        ivsteps.setOnClickListener(this);
        ivsportTime.setOnClickListener(this);
        ivkaloria.setOnClickListener(this);
        ivsleep.setOnClickListener(this);
        ivheartRate.setOnClickListener(this);
        ivhealtyScan.setOnClickListener(this);
        ivhealtyAdvice.setOnClickListener(this);
        /**
         *判断注册的信息是否成功提交至服务器,最好再做个网络判断，没有网络直接不执行
         *假如健康扫描存了多次，我们就取最后一次
         */
        if (CommentWays.isWifi(getApplicationContext()) == true
                || CommentWays.is3G(getApplicationContext()) == true) {
            ArrayList<SendServiceFlag> serviceFlags = model.serviceFlagSearth();
            if (serviceFlags.size() > 0 && serviceFlags.get(0).getRegisterFlag() != 2) {
                MyApplication.util.jsonRegis();
            }
        } else {
            MyApplication.ways.ToastShow(getApplicationContext(), "请检查网络是否良好");
        }
        Intent intentService = new Intent(getApplicationContext(), ElderService.class);
        startService(intentService);
        taskGetPush();//启动接收推送消息的线程
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivsteps:
                Intent intent1 = new Intent(getApplicationContext(), PedometerActivity.class);
                startActivity(intent1);
                break;
            case R.id.ivkaloria:
                Intent intent2 = new Intent(getApplicationContext(), KaloriaActivity.class);
                startActivity(intent2);
                break;
            case R.id.ivheartRate:
                Intent intent3 = new Intent(getApplicationContext(), HeartRateActivity.class);
                startActivity(intent3);
                break;
            case R.id.ivsleepTime:
                Intent intent4 = new Intent(getApplicationContext(), SleepActivity.class);
                startActivity(intent4);
                break;
            case R.id.ivsportTime:
                Intent intent5 = new Intent(getApplicationContext(), SportTimeActivity.class);
                startActivity(intent5);
                break;
            case R.id.ivhealthScan:
                Intent intent6 = new Intent(getApplicationContext(), HealthScanActivity.class);
                startActivity(intent6);
                break;
            case R.id.ivhealthAdvice:
                Intent intent7 = new Intent(getApplicationContext(), AdviceInfoActivity.class);
                startActivity(intent7);
                break;
            case R.id.ivBloodSugar:
                Intent intent8 = new Intent(getApplicationContext(), HealthStateActivity.class);
                startActivity(intent8);
                break;
        }
    }

    public static class pushInfoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if ("com.zhg.elder".equals(action)) {
                Message message = new Message();
                message.what = 2;

                if (handler != null)
                    handler.sendMessage(message);// 发送消息
            }
        }
    }

    /**
     * 定时获取推送消息,通过广播通知
     */
    private void taskGetPush() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (CommentWays.isWifi(getApplicationContext()) == true
                        || CommentWays.is3G(getApplicationContext()) == true) {
                    MyApplication.util.jsonPush();
                }
            }
        };
    }


    private void sendTimeCmd() {
        //byte[] msgBody = new byte[1];
        MyApplication.eBbraceletcom.Start(new EBEventHander() {
            @Override
            public void OnRecv(byte[] buffer, int lenght) {
                super.OnRecv(buffer, lenght);
            }
        });
        byte[] msgBody = {(byte) (year / 100), (byte) (year % 100), (byte) (month + 1)
                , (byte) day, (byte) hour, (byte) (minute), (byte) seconds};
        byte[] Cmd = CmdProtocol.packageCmd((byte) 0x06, msgBody, 7);
        MyApplication.eBbraceletcom.Write(Cmd, Cmd.length);
    }


    private void connetBtDeice() {
        if (!mBtAdapter.isEnabled()) {
            mBtAdapter.enable();//直接打开
        }
        if (mReceiver == null) mReceiver = new BtDeviceReceiver();
        if (MyApplication.eBbraceletcom != null) MyApplication.eBbraceletcom.Stop();
        MyApplication.eBbraceletcom = null;

        if (MyApplication.eBbraceletcom == null) MyApplication.eBbraceletcom = new EBbraceletcom();
        MyApplication.eBbraceletcom.Start(new EBEventHander() {
            public void OnBtEvent(int nEventType) {

                if (nEventType == EBbraceletcom.BT_EVENT_DEVICE_SCANNING) {
                    //  注册广播 发现设备
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    MainActivity.this.registerReceiver(mReceiver, filter);
                    bcFlag = true;
                    //  扫描完成
                    filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    MainActivity.this.registerReceiver(mReceiver, filter);
                    bcFlag = true;
                    java.util.Set<BluetoothDevice> deviceList = MyApplication.eBbraceletcom.GetPairedDevices();
                    for (BluetoothDevice dev : deviceList) {
                        // if(dev.getAddress().replace(":", "").equals(MyApplication.mac) && device == null){
                        if (dev.getAddress().replace(":", "").equals("98d33180239b") && device == null) {//"38:25:10:1A:62:61"
                            device = dev;
                            MyApplication.eBbraceletcom.ConnectDevice(dev, true);//调用连接方法
                            //Toast.makeText(getApplicationContext(), "正在连接蓝牙设备中....", 3000).show();
                            MyApplication.ways.ToastShow(getApplicationContext(), "正在连接蓝牙设备中....");
                        }

                    }
                }
                if (nEventType == EBbraceletcom.BT_EVENT_DEVICE_PAIRING) {

                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                    MainActivity.this.registerReceiver(mReceiver, filter);
                    bcFlag = true;
                    //MyApplication.datacomm.SetCommState(3);
                }
                if (nEventType == EBbraceletcom.BT_EVENT_DEVICE_CONNECTING) {
                }
                if (nEventType == EBbraceletcom.BT_EVENT_DEVICE_CONNECTED) {
                    if (mReceiver != null && bcFlag == true)
                        MainActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播  && style == 1
                    mReceiver = null;
                    // style = 0;
                    device = null;
                    //Toast.makeText(getApplicationContext(), "蓝牙设备连接成功", 1500).show();
                    MyApplication.ways.ToastShow(getApplicationContext(), "蓝牙设备连接成功");
                    String str = "MTKSPPForMMI";
                    byte[] msgBody = str.getBytes();
                    MyApplication.eBbraceletcom.Write(msgBody, msgBody.length);
                    MyApplication.eBbraceletcom.Start(new EBEventHander() {
                        public void OnRecv(byte[] sample, int lenght) {
                            if (sample != null) {

                            }
                        }

                        ;
                    });

                    if (status.size() == 0) {
                        /**
                         *保存mac地址
                         */
                        Mac MAC = new Mac();
                        //String macAddress = ;//mac地址
                        //String password = ;//8位秘钥
                        MAC.setMac(MyApplication.mac);
                        MAC.setPsword(MyApplication.pswd);
                        model.insertbrace(MAC);
                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(intent);

                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sendTimeCmd();//发送时间

                    } else {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sendTimeCmd();//发送时间

                        MyApplication.CONNETET = true;


                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //发送计步命令
                        MyApplication.ways.dialog(MainActivity.this);
                        MyApplication.util.sendCmdSteps();
                    }


                }
                if (nEventType == EBbraceletcom.FAILED) {
                    if (Times == 0 && Times < 3) {
                        // Toast.makeText(getApplicationContext(), "蓝牙设备连接失败", 1500).show();
                        MyApplication.ways.ToastShow(getApplicationContext(), "蓝牙设备连接失败");
                        connetBtDeice();
                        Times += 1;
                    }
                }
            }

            public void OnError(byte[] data) {

            }
        });
        //	}
    }

    public class BtDeviceReceiver extends BroadcastReceiver {
        //	    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String strName = dev.getName();
                if (dev.getAddress().replace(":", "").equals(MyApplication.mac) && device == null) {
                    device = dev;
                    MyApplication.eBbraceletcom.PairDevice(dev);//设备配对
                }
            }
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1);
                int reason = intent.getIntExtra("android.bluetooth.device.extra.REASON", -1);

                MyApplication.eBbraceletcom.ConnectDevice(true);//调用连接方法
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action) && device == null) {
                //Toast.makeText(getApplicationContext(), "没有搜索到任何设备", 3000).show();
                MyApplication.ways.ToastShow(getApplicationContext(), "没有搜索到任何设备");
                if (mReceiver != null && bcFlag == true)
                    MainActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播
                mReceiver = null;
            }
        }
    }

    @Override
    protected void onResume() {
        if (CCC == 1) {
            Log.i("info", "后台执行resume");
            ca = Calendar.getInstance();
            year = ca.get(Calendar.YEAR);
            month = ca.get(Calendar.MONTH);//+1
            day = ca.get(Calendar.DATE);
            hour = ca.get(Calendar.HOUR_OF_DAY);
            minute = ca.get(Calendar.MINUTE);//+1
            seconds = ca.get(Calendar.SECOND);
            connetBtDeice();
            CCC = 0;
        }
        super.onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mReceiver != null && bcFlag == true)
                MainActivity.this.unregisterReceiver(mReceiver);//取消蓝牙广播  && status == 1
            mReceiver = null;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && status.size() > 0) {
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
            startActivitySafely(startIntent);
            CCC = 1;
            return true;
        } else return super.onKeyDown(keyCode, event);
    }

    void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Toast.makeText(this, "ActivityNotFoundException",  Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            //  Toast.makeText(this, "SecurityException",Toast.LENGTH_SHORT).show();

        }
    }
}
