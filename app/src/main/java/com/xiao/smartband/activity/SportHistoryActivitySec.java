package com.xiao.smartband.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.xiao.smartband.MyApplication;
import com.xiao.smartband.R;
import com.xiao.smartband.adapter.AdviceScanAdapter;
import com.xiao.smartband.adapter.HeartRateAdapter;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.HealthScan;
import com.xiao.smartband.entity.HeartRate;
import com.xiao.smartband.view.TitleView;

import java.util.ArrayList;

public class SportHistoryActivitySec extends Activity {
	private TitleView titleView;
	private ListView LVlist;
	private HeartRateAdapter adapter;
	private AdviceScanAdapter scanAdapter;
	private ArrayList<HeartRate> rates;
	private ArrayList<HealthScan> scans;
	private ArrayList<HeartRate> newRates = new ArrayList<HeartRate>();
	private ArrayList<HealthScan> newScans = new ArrayList<HealthScan>();
	private TextView tvdate,tvfinishRate;
	private ModelDao model;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_history_two);
		init();
	}
	
	private void init(){
		titleView = (TitleView)findViewById(R.id.titleview);
		LVlist = (ListView)findViewById(R.id.LVlist);
		tvdate = (TextView)findViewById(R.id.tvdate_title);
		tvfinishRate = (TextView)findViewById(R.id.tvComRateTitle);
		model = new ModelDao(getApplicationContext());
		if(MyApplication.activityStuts == 3){
			
			tvdate.setTextColor(Color.rgb(248,101,113));
			tvfinishRate.setTextColor(Color.rgb(248,101,113));
			rates = model.HeartRateSearth();
			if(rates.size() >= 100){
				 for(int i = 0 ; i < rates.size() - 99; i++){
						 model.deleteHeartRateId(rates.get(i).getId());
				    }
				 rates = model.HeartRateSearth();
				}
			tvdate.setText(R.string.date);
			tvfinishRate.setText(R.string.heart_rate_his);//("心率(次/分钟)");
			titleView.setTitle(R.string.heartRate_his);
			titleView.setbg(R.drawable.heart_titlebg);
			titleView.titleImg(R.drawable.heart_titleimg);
		}else if(MyApplication.activityStuts == 6){
			tvdate.setTextColor(Color.rgb(14,185,65)); 
			tvfinishRate.setTextColor(Color.rgb(14,185,65)); 
			scans = model.HealthScanSearth();
			if(scans.size() >= 10){
				 for(int i = 0 ; i < scans.size() - 10; i++){
						 model.deleteHealthScanId(scans.get(i).getId());
				    }
				 scans = model.HealthScanSearth();
				}
			tvdate.setText(R.string.date);
			tvfinishRate.setText(R.string.healthyshu);
			titleView.setTitle(R.string.healthy_his);
			titleView.setbg(R.drawable.health_santitleimg);
			titleView.titleImg(R.drawable.health_scanimg);
		}
		if(MyApplication.activityStuts == 3){
			//rates = model.HeartRateSearth();
			for(int i = rates.size() - 1; i >= 0; i--){
				newRates.add(rates.get(i));
			}
			adapter = new HeartRateAdapter(getApplicationContext(), newRates);
			LVlist.setAdapter(adapter);
		}else if(MyApplication.activityStuts == 6){
			//scans = model.HealthScanSearth();
			for(int i = scans.size() - 1; i >= 0; i --){
				newScans.add(scans.get(i));
			}
			scanAdapter = new AdviceScanAdapter(getApplicationContext(), newScans);
			LVlist.setAdapter(scanAdapter);
		}
		titleView.setBack(R.drawable.steps_back, new TitleView.onBackLister() {
			
			@Override
			public void onClick(View button) {
				finish();
			}
		});
	}
}
