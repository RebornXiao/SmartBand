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
import com.xiao.smartband.view.TitleView;

public class AdviceCollectAcvitity extends Activity implements Callback{
	private TitleView titleView;
	private ListView Lvadvice;
	private AaviceAdapter adapter;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_advice_list);
 		init();
	}
	
	private void init(){
		handler = new Handler(AdviceCollectAcvitity.this);
		Lvadvice = (ListView)findViewById(R.id.Lvadvice);
		adapter = new AaviceAdapter(getApplicationContext(),handler, MyApplication.pushsCollect);
		Lvadvice.setAdapter(adapter);
		titleView = (TitleView)findViewById(R.id.titleview);
		titleView.setTitle(R.string.healthyAdviceCollect);
		titleView.setbg(R.drawable.hearlth_advice_titlebg);
		titleView.titleImg(R.drawable.hearlth_advice_titleimg);
		titleView.setBack(R.drawable.steps_back, new TitleView.onBackLister() {
			
			@Override
			public void onClick(View button) {
				MyApplication.adviceNomalorCollect = false;
				finish();
			}
		});
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
