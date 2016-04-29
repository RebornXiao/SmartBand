package com.xiao.smartband.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.xiao.smartband.R;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.HealthState;
import com.xiao.smartband.view.TitleView;

import java.util.Calendar;
import java.util.List;

public class HealthStateActivity extends Activity {
	private TitleView titleView;
	private EditText etbloodPressure,etbloodPressureLow;
	private EditText etbloodSugar,etbloodSugarAfter;
	private TextView tvsure,tvdate;
	private ModelDao model;
	private Calendar ca;
	private int year,month,day,hour,minute ;
	private String nPressure,nbloodPressureLow,nSugar,nSugarAfter;
	private String date2,date3,nhour,nminit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_health_state);
		init();
	}
	//date[0]*100 + date[1]+"-"+date2+"-"+date3;
	private void init(){
		titleView = (TitleView)findViewById(R.id.titleview);
		titleView.setbg(R.drawable.register_titlebg);
		titleView.setTitle(R.string.todayhealtystate);
		titleView.right(R.string.history, new TitleView.onSetLister() {
			
			@Override
			public void onClick(View button) {
				Intent intent = new Intent(getApplicationContext(), HealthSateHisActivity.class);
				startActivity(intent);
			}
		});
		etbloodPressure = (EditText)findViewById(R.id.etbloodPressure);
		etbloodSugar = (EditText)findViewById(R.id.etbloodSugar);
		etbloodPressureLow = (EditText)findViewById(R.id.etbloodPressureLow);
		etbloodSugarAfter = (EditText)findViewById(R.id.etbloodSugarAfter);
		tvsure = (TextView)findViewById(R.id.tvsure);
		tvdate = (TextView)findViewById(R.id.tvdate);
		model = new ModelDao(getApplicationContext());
		ca = Calendar.getInstance();
		year = ca.get(Calendar.YEAR);
		month=ca.get(Calendar.MONTH);//+1
		day=ca.get(Calendar.DATE);
		hour = ca.get(Calendar.HOUR);
		minute = ca.get(Calendar.MINUTE);
		date2 = String.format("%02d", month + 1);
		date3 = String.format("%02d", day);
		nhour = String.format("%02d", hour);
		nminit = String.format("%02d", minute);
		List<HealthState> states = model.healthStateSearth();
		//删除大于10条的数据
		if(states.size() > 10){
			for(int i = 0 ; i < states.size() - 10; i++ ) {
				 model.delete(states.get(i).getId());
		    }
			states = model.healthStateSearth();
		}
		if(states.size() > 0){
		 //  for(int i = 0 ;i<states.size(); i++){
			   //如果当天有数据就把它显示出来
		//	   if(states.get(i).getDate().equals(year+"-"+date2+"-"+date3)){
			       int i = states.size() - 1;
				   etbloodPressure.setText(states.get(i).getBloodPressure());
				   etbloodSugar.setText(states.get(i).getBloodSugar());
				   etbloodPressureLow.setText(states.get(i).getBloodPressureLow());
				   etbloodSugarAfter.setText(states.get(i).getBloodSugarAfter());
				   tvdate.setText(states.get(i).getDate()+"健康指标");
				   etbloodPressure.setSelection(states.get(i).getBloodPressure().length());
			//   }
		 //  }
		}
		tvsure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nPressure = etbloodPressure.getText().toString();
				nbloodPressureLow = etbloodPressureLow.getText().toString();
				nSugar = etbloodSugar.getText().toString();
				nSugarAfter = etbloodSugarAfter.getText().toString();
				HealthState state = new HealthState();
				state.setBloodPressure(nPressure);
				state.setBloodPressureLow(nbloodPressureLow);
				state.setBloodSugar(nSugar);
				state.setBloodSugarAfter(nSugarAfter);
				
				
				state.setDate(year+"-"+date2+"-"+date3+" "+nhour+":"+nminit);
			//	if(model.queryHealthState(year+"-" + date2 +"-"+  (date3)) == false)
				   model.insertHealthState(state);
			//	else model.updateHealthState(state, year+"-"+date2+"-"+date3);
				finish();
			}
		});
		titleView.setBack(R.drawable.steps_back, new TitleView.onBackLister() {
			
			@Override
			public void onClick(View button) {
				finish();
			}
		});
	}
}
