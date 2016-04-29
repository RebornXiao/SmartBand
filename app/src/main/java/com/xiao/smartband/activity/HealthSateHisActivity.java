package com.xiao.smartband.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.xiao.smartband.R;
import com.xiao.smartband.adapter.HealthStateAdapter;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.HealthState;
import com.xiao.smartband.view.TitleView;

import java.util.ArrayList;
import java.util.List;

public class HealthSateHisActivity extends Activity {
	private TitleView titleView;
	private ListView LVlist;
	private HealthStateAdapter adapter;
	private List<HealthState> states;
	private List<HealthState> newstates;
	private TextView tvdate,tvfinish,tvfinishRate;
	private ModelDao model;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_history_state);
		init();
	}
	
	private void init(){
		titleView = (TitleView)findViewById(R.id.titleview);
		model = new ModelDao(getApplicationContext());
		titleView.setBack(R.drawable.steps_back, new TitleView.onBackLister() {
			
			@Override
			public void onClick(View button) {
				finish();
			}
		});
		//	titleView.setTitle(R.string.pedometer_his);
		//	titleView.setbg(R.drawable.stepstitlebg);
		//	titleView.titleImg(R.drawable.stepstitleimg);
		//	tvdate.setTextColor(Color.rgb(24,144,240));
		//	tvfinish.setTextColor(Color.rgb(24,144,240));
			//tvfinishRate.setTextColor(Color.rgb(24,144,240));
		//	tvfinish.setText("当日步数");
		//	tvfinishRate.setText("完成率");
			titleView.setbg(R.drawable.register_titlebg);
			titleView.setTitle(R.string.healthstatehis);
			newstates = new ArrayList<HealthState>();
			states = model.healthStateSearth();
			if(states.size() >= 10){
			 for(int i = 0 ; i < states.size() - 10; i++ ) {
					 model.delete(states.get(i).getId());
			    }
			 states = model.healthStateSearth();
			}
			if(states.size() > 0){
				for(int i = states.size() - 1; i >= 0 ; i --  ){
					newstates.add(states.get(i));
				}
			}
			tvdate = (TextView)findViewById(R.id.tvdate_title);
			LVlist = (ListView)findViewById(R.id.LVhealthStateList);
		
			adapter = new HealthStateAdapter(getApplicationContext(), newstates);
			LVlist.setAdapter(adapter);
		
		
	}
}
