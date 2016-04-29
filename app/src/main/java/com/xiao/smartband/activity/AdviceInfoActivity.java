package com.xiao.smartband.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.xiao.smartband.MyApplication;
import com.xiao.smartband.R;
import com.xiao.smartband.adapter.AaviceAdapter;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.Push;
import com.xiao.smartband.view.TitleView;

import java.util.ArrayList;

public class AdviceInfoActivity extends Activity implements Callback{
	private TitleView titleView;
	private ListView Lvadvice;
	private AaviceAdapter adapter;
	private ModelDao model;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_advice_list);
		/*model = new ModelDao(getApplicationContext());
		Push push = new Push();
		push.setTitle("1");
		push.setContent("111111111111111111111");
		model.insertHealthPush(push);
		
		push = new Push();
		push.setTitle("2");
		push.setContent("2222222222222222");
		model.insertHealthPush(push);
		
		push = new Push();
		push.setTitle("3");
		push.setContent("33333333333333333");
		model.insertHealthPush(push);
		
		push = new Push();
		push.setTitle("3");
		push.setContent("33333333333333333");
		model.insertHealthPush(push);*/
 		init();
	}
	private void init(){
	 	handler = new Handler(AdviceInfoActivity.this);
		model = new ModelDao(getApplicationContext());
		MyApplication.pushsNomal = new ArrayList<Push>();
		MyApplication.pushs = model.pushSearth();
		//第一步判断 整个数组是否大于0
		if(MyApplication.pushs.size() > 0){
	       for(int i = 0; i < MyApplication.pushs.size(); i++){
			   if(MyApplication.pushs.get(i).getCollect() == 0){
				  MyApplication.pushsNomal.add(MyApplication.pushs.get(i));
			}
		}
	}
	     //第二部判断普通建议数据是否大于10
		if(MyApplication.pushsNomal.size() > 10){
			for(int i = 0 ; i < MyApplication.pushsNomal.size() - 10; i++ ) {
				model.deleteHealthAdvice(MyApplication.pushsNomal.get(i).getId());
		    }
			//重新初始化
			MyApplication.pushsNomal = new ArrayList<Push>();
			MyApplication.pushs = model.pushSearth();
			for(int i = 0; i < MyApplication.pushs.size(); i++){
				if(MyApplication.pushs.get(i).getCollect() == 0){
					MyApplication.pushsNomal.remove(MyApplication.pushs.get(i));
				}
			}
		 }
		
		//第三部判断 收藏的健康建议数据
		
		Lvadvice = (ListView)findViewById(R.id.Lvadvice);
		adapter = new AaviceAdapter(getApplicationContext(),handler, MyApplication.pushsNomal);
		Lvadvice.setAdapter(adapter);
		titleView = (TitleView)findViewById(R.id.titleview);
		titleView.setTitle(R.string.healthyAdvice);
		titleView.setbg(R.drawable.hearlth_advice_titlebg);
		titleView.titleImg(R.drawable.hearlth_advice_titleimg);
		
		titleView.setBack(R.drawable.steps_back, new TitleView.onBackLister() {
			
			@Override
			public void onClick(View button) {
				finish();
			}
		});
		titleView.right(R.string.collect, new TitleView.onSetLister() {
			
			@Override
			public void onClick(View button) {
				//点击按钮时 做数据处理
				MyApplication.pushsCollect = new ArrayList<Push>();
				MyApplication.pushs = model.pushSearth();
				if(MyApplication.pushs.size() > 0){
			       for(int i = 0; i < MyApplication.pushs.size(); i++){
					   if(MyApplication.pushs.get(i).getCollect() == 1){
						  MyApplication.pushsCollect.add(MyApplication.pushs.get(i));
						}
					}
				}
				MyApplication.adviceNomalorCollect = true;
				Intent intent = new Intent(getApplicationContext(), AdviceCollectAcvitity.class);
				startActivity(intent);
			}
		});
	}
	/*private void json(){
		ArrayList<UId> uids = model.uIdSearth();
		if(uids.size() == 0) return;
		String uId = uids.get(0).getuId();
		String op = "10003";
		String info = "";
		JSONObject object2 = new JSONObject();
		ArrayList<Push> pushs = model.pushSearth();
		
		try {
			if(pushs.size() > 0)
			   object2.put("time", pushs.get(pushs.size() - 1).getDate());
			else object2.put("time", "0");
			info = object2.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("op", op);
		map2.put("info", info);
		map2.put("uId", uId);//"69836077" "8638836"
		RequestParams params = new RequestParams(map2);
		
		PatientRestClient.post("push", params, new XmlHttpResponseHandler(){
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
					MyApplication.ways.ToastShow(getApplicationContext(), "提交成功");
					String err = response.getString("err");
					String info = response.getString("info");
					JSONArray array = null;
					if(info != null && !info.equals("{}")){
					   array = new JSONArray(info);
					   Push push = null ;
						for(int i = 0 ; i< array.length(); i++){
							JSONObject objectdd = (JSONObject) array.get(i);
							Log.i("info", "pb="+objectdd.toString());
							push = new Push();
							push.setContent(objectdd.getString("content"));
							push.setDate(objectdd.getString("time"));
							push.setTitle("健康建议"+(i+1));
							model.insertHealthPush(push);
							MyApplication.pushs.add(push);
						}
						  getVoice();
						if(adapter != null)
						   adapter.changeData(MyApplication.pushs);
					}
					
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
				Log.i("info", "fale");
			}
		});
		
	}*/
	
	/*private void getVoice(){
		提示声音
		NotificationManager manger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); 
	
		Notification notification = new Notification(); 
				
		
		//自定义声音   声音文件放在ram目录下，没有此目录自己创建一个
	//	notification.sound=Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" +R.raw.mm); 
		//使用系统默认声音用下面这条
		
		notification.defaults=Notification.DEFAULT_SOUND;

		
		Intent intent = new Intent(getApplicationContext(), AdviceInfoActivity.class);//实例化intent
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(getApplicationContext(), "如医","推送有消息", pi);// 设置事件信息
		manger.notify(1, notification);
	}*/
	@Override
	protected void onResume() {
	if(MyApplication.adviceDel == true){
//		MyApplication.pushs = model.pushSearth();
		adapter.changeData(MyApplication.pushsNomal);
		MyApplication.adviceDel = false;
	}
		super.onResume();
	}
	@Override
	public boolean handleMessage(Message msg) {
		if(msg.what == 0){
		   Intent intent = new Intent(getApplicationContext(), AdviceDetailActivity.class);
		   intent.putExtra("value", msg.arg1+"");
		   startActivity(intent);
		}
		return false;
	}
}
