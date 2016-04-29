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
import com.xiao.smartband.adapter.SleepAdapter;
import com.xiao.smartband.adapter.SportAdapter;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.Sleep;
import com.xiao.smartband.entity.Sport;
import com.xiao.smartband.view.TitleView;

import java.util.ArrayList;

public class SportHistoryActivity extends Activity {
	private TitleView titleView;
	private ListView LVlist;
	private SportAdapter adapter;
	private SleepAdapter sleepAdapter;
	private ArrayList<Sport> sports;
	private ArrayList<Sleep> sleeps;
	private ArrayList<Sleep> newSleeps = new ArrayList<Sleep>();
	private ArrayList<Sport> newSports = new ArrayList<Sport>();
	private TextView tvdate,tvfinish,tvfinishRate;
	private ModelDao model;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_history);
		init();
	}
	
	private void init(){
		titleView = (TitleView)findViewById(R.id.titleview);
		model = new ModelDao(getApplicationContext());
		tvdate = (TextView)findViewById(R.id.tvdate_title);
		tvfinish = (TextView)findViewById(R.id.tvcomTitle);
		tvfinishRate = (TextView)findViewById(R.id.tvComRateTitle);
		tvdate.setText(R.string.date);
		titleView.setBack(R.drawable.steps_back, new TitleView.onBackLister() {
			
			@Override
			public void onClick(View button) {
				finish();
			}
		});
		if(MyApplication.activityStuts == 1){
			titleView.setTitle(R.string.pedometer_his);
			titleView.setbg(R.drawable.stepstitlebg);
			titleView.titleImg(R.drawable.stepstitleimg);
			tvdate.setTextColor(Color.rgb(24,144,240));
			tvfinish.setTextColor(Color.rgb(24,144,240));
			tvfinishRate.setTextColor(Color.rgb(24,144,240));
			tvfinish.setText(R.string.taday_step);
			tvfinishRate.setText(R.string.complet_rate);
			titleView.setTitle(R.string.pedometerHis);
			sports = model.sportSearth();
			if(sports.size() >= 10){
			 for(int i = 0 ; i < sports.size() - 10; i++ ) {
					 model.delete(sports.get(i).getId());
			    }
				sports = model.sportSearth();
			}
		}else if(MyApplication.activityStuts == 2){
			titleView.setTitle(R.string.kaloria_his);
			titleView.setbg(R.drawable.kariabg);
			titleView.titleImg(R.drawable.ka_titleimg);
			tvfinish.setText(R.string.today_caloria);
			tvfinishRate.setText(R.string.complet_rate);
			titleView.setTitle(R.string.kaloria_his);

			tvdate.setTextColor(Color.rgb(254,165,10));
			tvfinish.setTextColor(Color.rgb(254,165,10));
			tvfinishRate.setTextColor(Color.rgb(254,165,10));
			sports = model.sportSearth();
			if(sports.size() >= 10){
				for(int i = 0 ; i < sports.size() - 10; i++ ) {
						 model.delete(sports.get(i).getId());
				    }
				sports = model.sportSearth();
				}
		}else if(MyApplication.activityStuts == 4){
			tvfinish.setText(R.string.sleep_deep_time);
			tvfinishRate.setText(R.string.complet_rate);
			titleView.setTitle(R.string.sleep_his);
			titleView.titleImg(R.drawable.sleeptitleimg);
			titleView.setbg(R.drawable.sleeptitlebg);
			tvdate.setTextColor(Color.rgb(181,72,254));
			tvfinish.setTextColor(Color.rgb(181,72,254));
			tvfinishRate.setTextColor(Color.rgb(181,72,254));
			sleeps = model.sleepSearth();
			if(sleeps.size() >= 100){
				 for(int i = 0 ; i < sleeps.size() - 99; i++){
						 model.deleteSleepId(sleeps.get(i).getId());
				    }
					sleeps = model.sleepSearth();
				}
		}else if(MyApplication.activityStuts == 5){
			tvfinish.setText(R.string.sport_time);
			tvfinishRate.setText(R.string.complet_rate);
			titleView.setTitle(R.string.sport_his);
			titleView.setbg(R.drawable.sport_titlebg);
			titleView.titleImg(R.drawable.sport_titleimg);
			tvdate.setTextColor(Color.rgb(24,144,240));
			tvfinish.setTextColor(Color.rgb(24,144,240));
			tvfinishRate.setTextColor(Color.rgb(24,144,240));
			sports = model.sportSearth();
			for(int i = 0 ; i < sports.size() - 10; i++ ) {
					 model.delete(sports.get(i).getId());
			}
			sports = model.sportSearth();
		}
		LVlist = (ListView)findViewById(R.id.LVlist);
		
		if(MyApplication.activityStuts == 4){
			for(int i = sleeps.size() - 1; i >= 0; i -- ){
				newSleeps.add(sleeps.get(i));
			}
			sleepAdapter = new SleepAdapter(getApplicationContext(), newSleeps);
			LVlist.setAdapter(sleepAdapter);
		}else {
			for(int i = sports.size() - 1; i >= 0; i -- ){
				newSports.add(sports.get(i));
			}
			adapter = new SportAdapter(getApplicationContext(), newSports);
			LVlist.setAdapter(adapter);
		}
		
	}
}
