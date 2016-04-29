package com.xiao.smartband.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiao.smartband.MyApplication;
import com.xiao.smartband.R;
import com.xiao.smartband.bluetooth.EBEventHander;
import com.xiao.smartband.client.PatientRestClient;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.Register;
import com.xiao.smartband.entity.SendServiceFlag;
import com.xiao.smartband.entity.Status;
import com.xiao.smartband.entity.UId;
import com.xiao.smartband.http.RequestParams;
import com.xiao.smartband.http.XmlHttpResponseHandler;
import com.xiao.smartband.protocal.CmdProtocol;
import com.xiao.smartband.tools.CommentWays;
import com.xiao.smartband.view.ArrayWheelAdapter;
import com.xiao.smartband.view.NumericWheelAdapter;
import com.xiao.smartband.view.OnWheelChangedListener;
import com.xiao.smartband.view.OnWheelScrollListener;
import com.xiao.smartband.view.TitleView2;
import com.xiao.smartband.view.WheelView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity implements OnClickListener{
	private TitleView2 titleView;
	private EditText etname,etnum;
	private EditText eturgentname,eturgentnum;
	private TextView tvnext,age,sex;
	private TextView argee_pro,height,weight;
	private Button btnsure,btnsureSex;
	private CheckBox box;
	private boolean flag = false;
	private SpannableString msp = null;
	private String name,num,nsex,nage,nweight,nheight;
	private String nungentName,nungentNum;
	private ModelDao model;
	private PopupWindow popupWindow,popupWindowSex;
    private Animation anim;
    private boolean wheelScrolled = false;
	private int value;
	private int registerStatus;
	private View viewWindow,viewWindowSex;
	private WheelView wheel,wheelSex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_register);
		MyApplication.getInstance().addActivity(this);
		init();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sendCmd();
	}
	private void init(){
		model = new ModelDao(getApplicationContext());
		titleView = (TitleView2)findViewById(R.id.titleview);
		titleView.setbg(R.drawable.register_titlebg);
		titleView.setTitle(R.string.registerInfo);
		tvnext = (TextView)findViewById(R.id.tvbasic_next);
		etname = (EditText)findViewById(R.id.etname);
		etnum = (EditText)findViewById(R.id.etnum);
		eturgentname = (EditText)findViewById(R.id.eturgentname);
		eturgentnum = (EditText)findViewById(R.id.eturgentnum);
		age = (TextView)findViewById(R.id.tvage);
		sex = (TextView)findViewById(R.id.tvsex);
		height = (TextView)findViewById(R.id.tvheight);
		weight= (TextView)findViewById(R.id.tvweight);
		age.setOnClickListener(this);
		sex.setOnClickListener(this);
		height.setOnClickListener(this);
		weight.setOnClickListener(this);
		argee_pro = (TextView)findViewById(R.id.argee_pro);
		msp = new SpannableString("<<"+"用户使用协议"+">>");
		box = (CheckBox)findViewById(R.id.checkbox_agree);
		box.setOnClickListener(this);
		msp.setSpan(new UnderlineSpan(), 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
		argee_pro.setText(msp);
    	anim = AnimationUtils.loadAnimation(this, R.drawable.slide_bottom);
		viewWindow = (View)this.getLayoutInflater().inflate(R.layout.bracelet_regi_view, null);
		
		viewWindowSex = (View)this.getLayoutInflater().inflate(R.layout.bracelet_regi_sex, null);
		btnsure = (Button) viewWindow.findViewById(R.id.btnsure);
		btnsure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(registerStatus == 1){
					age.setText(value+"");
				}else if(registerStatus == 3){
					weight.setText(value+"");
				}else if(registerStatus == 2){
					height.setText(value+"");
				}
				popupWindow.dismiss();	
			}
		});
		
		btnsureSex = (Button) viewWindowSex.findViewById(R.id.btnsure);
		btnsureSex.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(registerStatus == 4){
					Log.i("info", "val="+value);
					if(value == 1){
					   sex.setText("男");
					}else if(value == 0){
						sex.setText("女");
					}
					popupWindowSex.dismiss();	
				}
			}
		});
		//int width = botton_layout.getWidth();
		popupWindow = new PopupWindow(viewWindow, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		
		popupWindowSex = new PopupWindow(viewWindowSex, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindowSex.setOutsideTouchable(true);
		popupWindowSex.setBackgroundDrawable(new BitmapDrawable());
		initWheel();
		initWheelSex();
		argee_pro.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			//Intent intent = new Intent(getApplicationContext(), ProtocolDescription.class);
			//startActivity(intent);
			}
		});
		tvnext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				 name = etname.getText().toString();
			     num = etnum.getText().toString();
			     nsex = sex.getText().toString();
			     nage = age.getText().toString();
			     nheight = height.getText().toString();
			     nweight = weight.getText().toString();
			     nungentName = eturgentname.getText().toString();
			     nungentNum = eturgentnum.getText().toString();
			     if(name.equals("")){
			    	MyApplication.ways.ToastShow(getApplicationContext(), "姓名不能为空");
			    	return; 
			     }
			     if(num.equals("")){
				    MyApplication.ways.ToastShow(getApplicationContext(), "电话号码不能为空");
				    return; 
				     }
			     if(nungentName.equals("")){
					MyApplication.ways.ToastShow(getApplicationContext(), "紧急联系人姓名不能为空");
					return; 
			     }
			     if(nungentNum.equals("")){
						MyApplication.ways.ToastShow(getApplicationContext(), "紧急联系人号码不能为空");
						return; 
				     }
			     if(flag == false){
					MyApplication.ways.ToastShow(getApplicationContext(), "请选择同意协议");
			    	 return;
			     }
			     
				/**
				*该状态表示已经绑定过的设备就不会进入  绑定页面 
				*/
			    if(!nweight.equals(""))sendCmdWeight(nweight + "");
				Status status = new Status();
				status.setStaus(1);
				model.insertStatus(status);
				
				if(CommentWays.isWifi(getApplicationContext()) == true
			               || CommentWays.is3G(getApplicationContext()) == true){
					jsonRegis();
				}else {
					MyApplication.ways.ToastShow(getApplicationContext(), "请检查网络是否良好");
					SendServiceFlag sendServiceFlag = new SendServiceFlag();
					sendServiceFlag.setRegisterFlag(1);
					model.insertsendService(sendServiceFlag);
				}
				
				Intent intent = new Intent(getApplicationContext(), BoundSuccessActivity.class);
				saveRegister();
				startActivity(intent);
			}
		});
	}
	// Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }
        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
        }
    };
    
    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
        	value = newValue;
            if (!wheelScrolled) {
            }
        }
    };
    

    /**
     * Initializes wheel
     */
    private void initWheel() {
        wheel = getWheel(R.id.wvAge);
        //wheel.setCurrentItem((int)(Math.random() * 10));
        wheel.setAdapter(new NumericWheelAdapter(0, 200));
        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }
    
    
    private void initWheelSex() {
    	wheelSex = getWheelSex(R.id.wvSex);
	 	String countries[] = new String[] {"女", "男"};
	 	wheelSex.setVisibleItems(3);
	 	wheelSex.setAdapter(new ArrayWheelAdapter<String>(countries));
	 	//wheelSex.setAdapter(new ArrayWheelAdapter<String>(countries, 2));
    	wheelSex.addChangingListener(changedListener);
    	wheelSex.addScrollingListener(scrolledListener);
    	wheelSex.setCyclic(true);
    	wheelSex.setInterpolator(new AnticipateOvershootInterpolator());
    }
    
    /**
     * Returns wheel by Id
     * @param id the wheel Id
     * @return the wheel with passed Id
     */
    private WheelView getWheel(int id) {
    	return (WheelView) viewWindow.findViewById(id);
    }
    
    private WheelView getWheelSex(int id) {
    	return (WheelView) viewWindowSex.findViewById(id);
    }
   
	private void jsonRegis(){
		String op = "10001";
		String info = "";
		String chk = null;
		JSONObject object2 = new JSONObject();
		int sexint = 0 ;
		try {
			object2.put("name", name);
			if(nsex.equals("男") || nsex.equals("")){
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
			object2.put("urgentName", nungentName);
			object2.put("urgentPhone", nungentNum);
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
		
		PatientRestClient.post("register", params, new XmlHttpResponseHandler(){
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
				if(ack.equals("0") && op.equals("10001")){
					
					MyApplication.ways.ToastShow(getApplicationContext(), "提交成功");
					String err = response.getString("err");
					String info = response.getString("info");
					String uId = response.getString("uId");
					UId id = new UId();
					id.setuId(uId);
					model.insertuId(id);
					//注册成功后，也保存一个状态，万无一失
					SendServiceFlag sendServiceFlag = new SendServiceFlag();
					sendServiceFlag.setRegisterFlag(2);
					model.insertsendService(sendServiceFlag);
					//MyApplication.uId = uId;
				}else if(ack.equals("1")){
					MyApplication.ways.ToastShow(getApplicationContext(), "电话号码已存在");
					
				}else if(ack.equals("2")){
					MyApplication.ways.ToastShow(getApplicationContext(), "提交失败");
					
				}else if(ack.equals("3")){
					MyApplication.ways.ToastShow(getApplicationContext(), "电话号码不能为空");
					
				}else if(ack.equals("4")){
					MyApplication.ways.ToastShow(getApplicationContext(), "op配对失败");
					
				}else if(ack.equals("5")){
					MyApplication.ways.ToastShow(getApplicationContext(), "chk配对");
					
				}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.i("info", "sucess");
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				//没有网络，运动至此,失败
				
				Log.i("info", "false");
			}
		});
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvage:
			registerStatus = 1;
			if(registerStatus == 1){
	        	value = 55;
	            wheel.setCurrentItem(value);
	        }
			popupWindow.setAnimationStyle(R.style.AnimationPreview);
			popupWindow.showAsDropDown(age, 0, -3);
			MyApplication.registerValue = 1;
			break;
		case R.id.tvsex:
			registerStatus = 4;
			value = 0 ;
			MyApplication.registerValue = 2; 
			popupWindowSex.setAnimationStyle(R.style.AnimationPreview);
			popupWindowSex.showAsDropDown(sex, 0, -3);
			break;
		case R.id.tvheight:
			registerStatus = 2;
			if(registerStatus == 2){
	        	value = 165;
	        	wheel.setCurrentItem(value);
	        }
			MyApplication.registerValue = 3;
			popupWindow.setAnimationStyle(R.style.AnimationPreview);
			popupWindow.showAsDropDown(age, 0, -3);
			break;
		case R.id.tvweight:
			registerStatus = 3;
			if(registerStatus == 3){
	        	value = 55;
	            wheel.setCurrentItem(value);
	        }
			MyApplication.registerValue = 4;
			popupWindow.setAnimationStyle(R.style.AnimationPreview);
			popupWindow.showAsDropDown(age, 0, -3);
			break;
		case R.id.checkbox_agree:
			if(box.isChecked() == false){
				flag = false;
			}else {
				flag = true;
			}
			break;
		
		}
	}
	
	
	private void sendCmd(){
		MyApplication.eBbraceletcom.Start(new EBEventHander(){
			@Override
			public void OnRecv(byte[] buffer, int lenght) {
				super.OnRecv(buffer, lenght);
				if(buffer != null && lenght > 0){
					for(int i = 0 ; i<buffer.length; i++){
						Log.i("info", "绑定buffer="+buffer[i]);
					}
					int offset = 4;//命令码的下一位
					byte[] nCmdPara = new byte[1];
 					if(CmdProtocol.AckExam(buffer, lenght, nCmdPara) == 1){
						if(nCmdPara[0] == 0x01){
							if(MyApplication.ways == null) MyApplication.ways = new CommentWays();
							MyApplication.ways.ToastShow(getApplicationContext(), "绑定成功");
						}else if(nCmdPara[0] == 0x00){
							if(MyApplication.ways == null) MyApplication.ways = new CommentWays();
							MyApplication.ways.ToastShow(getApplicationContext(), "绑定失败");
						}
					}
				}
			}
			
		});
		
		byte[] msgBody = new byte[1];
		byte[] newcmd = CmdProtocol.newCmd(msgBody, 1);
		byte[] Cmd = CmdProtocol.packageCmd((byte)0x01, newcmd, 1);//数据长度 命令码+数据
		MyApplication.eBbraceletcom.Write(Cmd, Cmd.length);
	}
	
	
	private void sendCmdWeight(String weight){
		MyApplication.eBbraceletcom.Start(new EBEventHander(){
			@Override
			public void OnRecv(byte[] buffer, int lenght) {
				super.OnRecv(buffer, lenght);
				/*if(buffer != null && lenght > 0){
					for(int i = 0 ; i<buffer.length; i++){
						Log.i("info", "绑定buffer="+buffer[i]);
					}
					int offset = 4;//命令码的下一位
					byte[] nCmdPara = new byte[1];
 					if(CmdProtocol.AckExam(buffer, lenght, nCmdPara) == 1){
						if(nCmdPara[0] == 0x01){
							if(MyApplication.ways == null) MyApplication.ways = new CommentWays();
							MyApplication.ways.ToastShow(getApplicationContext(), "绑定成功");
						}else if(nCmdPara[0] == 0x00){
							if(MyApplication.ways == null) MyApplication.ways = new CommentWays();
							MyApplication.ways.ToastShow(getApplicationContext(), "绑定失败");
						}
					}
				}*/
			}
			
		});
		
		byte[] msgBody = new byte[1];
		int nweight = Integer.parseInt(weight);
		msgBody[0] = (byte) nweight;
		byte[] newcmd = CmdProtocol.newCmd(msgBody, 1);
		byte[] Cmd = CmdProtocol.packageCmd((byte)0x07, newcmd, 1);//数据长度 命令码+数据
		MyApplication.eBbraceletcom.Write(Cmd, Cmd.length);
		Log.i("info", "发送体重");
	}
	/**
	 *保存注册数据 
	 */
	private void saveRegister(){
		Register register = new Register();
		register.setName(name);
		register.setNum(num);
		register.setAge(nage);
		register.setSex(nsex);
		register.setWeight(nweight);
		register.setHeight(nheight);
		model.insertRegister(register);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		else return super.onKeyDown(keyCode, event);
	}
}
