package com.xiao.smartband.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiao.smartband.R;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.HealthScan;
import com.xiao.smartband.entity.HealthState;

import java.util.ArrayList;
import java.util.List;

public class ScanView extends LinearLayout {
	
	private TextView tvdate,tvsteps,tvstepsPer;
	private TextView tvheartRate,tvheartRatePer;
	private TextView tvkaloria,tvkaloriaPer;
	private TextView tvsleepDeep,tvsleepDeepPer;
	private TextView tvsportTime,tvsportTimePer;
	private TextView tvscore,tvhealthScanTips;
	private TextView tvpressure,tvweight,tvsugar,tvsugarAfter;
	private ImageView iv1,iv2,iv3,iv4,iv5;
	private ModelDao model;
	ArrayList<HealthScan> scans;
	public ScanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		
	}
	
	private void init(){
		View view = LayoutInflater.from(getContext()).inflate(R.layout.bracelet_scanview, null);
		tvdate = (TextView)view.findViewById(R.id.tvhsDate);
		tvsteps = (TextView)view.findViewById(R.id.tvhsSteps);
		tvstepsPer = (TextView)view.findViewById(R.id.tvhsStepsPer);
		tvheartRate = (TextView)view.findViewById(R.id.tvhsHeartRate);
		tvheartRatePer = (TextView)view.findViewById(R.id.tvhsHeartRatePer);
		tvkaloria = (TextView)view.findViewById(R.id.tvhsKaloria);
		tvkaloriaPer = (TextView)view.findViewById(R.id.tvhsKaPer);
		tvsleepDeep = (TextView)view.findViewById(R.id.tvhsSleepDeep);
		tvsleepDeepPer = (TextView)view.findViewById(R.id.tvhsSleepDeepPer);
		tvsportTime = (TextView)view.findViewById(R.id.tvhsSportTime);
		tvsportTimePer = (TextView)view.findViewById(R.id.tvhsSportTimePer);
		tvscore = (TextView)view.findViewById(R.id.tvhsScore);
		tvhealthScanTips = (TextView)view.findViewById(R.id.tvhealthScanTips);
		tvpressure = (TextView)view.findViewById(R.id.tvbloodPressure);
		tvsugar = (TextView)view.findViewById(R.id.tvbloodSugar);
		tvweight = (TextView)view.findViewById(R.id.tvweight);
		tvsugarAfter = (TextView)view.findViewById(R.id.tvbloodSugarAfter);
		iv1 = (ImageView)view.findViewById(R.id.iv1);
		iv2 = (ImageView)view.findViewById(R.id.iv2);
		iv3 = (ImageView)view.findViewById(R.id.iv3);
		iv4 = (ImageView)view.findViewById(R.id.iv4);
		iv5 = (ImageView)view.findViewById(R.id.iv5);
		addView(view);
		//HealthScan scan = model.queryHealthyScan(date);
	}
	
	public void dealData(int i){
		model = new ModelDao(getContext());
		scans = model.HealthScanSearth();
		if(scans.size() >0 ){
			String ndate = scans.get(i).getHShealthDate();
			String nscore = scans.get(i).getHShealthScore();
			String nheartRate = scans.get(i).getHSheart();
			String ncaloria = scans.get(i).getHSkaloria();
			String nsleepDeep = scans.get(i).getHSsleepDeep();
			String nsteps = scans.get(i).getHSsteps();
			String nstepTime = scans.get(i).getHSstepTime();
			String nstepPer = scans.get(i).getHSstepsPer();
			String nsleepDeepPer = scans.get(i).getHSsleepDeeper();
			String nheartRatePer = scans.get(i).getHSheartRatePer();
			if(ndate != null )
			tvdate.setText(ndate.substring(5).replace("-", " 月")+"日");
			tvsteps.setText(nsteps);
			tvheartRate.setText(nheartRate);
			tvkaloria.setText(ncaloria);
			tvsleepDeep.setText(nsleepDeep+"小时");
			tvsportTime.setText(nstepTime+"分钟");
			tvscore.setText(nscore);
			tvstepsPer.setText(nstepPer);
			tvsportTimePer.setText(nstepPer);
			tvkaloriaPer.setText(nstepPer);
			tvsleepDeepPer.setText(nsleepDeepPer);
			tvheartRatePer.setText(nheartRatePer);
			int hsScore = 0 ;
			if(nscore != null && !nscore.equals("") && !nscore.equals("差")){
				hsScore = Integer.parseInt(nscore);
			}
			if(hsScore > 60 && hsScore < 80){
				   iv1.setBackgroundResource(R.drawable.bright);
				   iv2.setBackgroundResource(R.drawable.bright);
				   iv3.setBackgroundResource(R.drawable.bright);
				   iv4.setBackgroundResource(R.drawable.gray);
				   iv5.setBackgroundResource(R.drawable.gray);
				   tvhealthScanTips.setText("您昨日运动指标良好，请继续保持!");
				}else if(hsScore > 80 && hsScore < 90){
					   iv1.setBackgroundResource(R.drawable.bright);
					   iv2.setBackgroundResource(R.drawable.bright);
					   iv3.setBackgroundResource(R.drawable.bright);
					   iv4.setBackgroundResource(R.drawable.bright);
					   iv5.setBackgroundResource(R.drawable.gray);
					   tvhealthScanTips.setText("您昨日运动指标良好，请继续保持!");
				}else if(hsScore > 90 ){
					   iv1.setBackgroundResource(R.drawable.bright);
					   iv2.setBackgroundResource(R.drawable.bright);
					   iv3.setBackgroundResource(R.drawable.bright);
					   iv4.setBackgroundResource(R.drawable.bright);
					   iv5.setBackgroundResource(R.drawable.bright);
					   tvhealthScanTips.setText("您昨日运动指标良好，请继续保持!");
				}else if(hsScore == 0){
					tvhealthScanTips.setText("1.您昨日的运动指标不好,请加强身体锻炼\n2.可能是您使用方法不当.");
				}
			List<HealthState> states = model.healthStateSearth();
			if(states.size() > 0){
			   for(int j = 0 ;j<states.size(); j++){
				   if(ndate != null && states.get(j).getDate().equals(ndate)){
					   tvpressure.setText(states.get(j).getBloodPressure());
					   tvsugar.setText(states.get(j).getBloodSugar());
					   tvsugarAfter.setText(states.get(j).getBloodSugarAfter());
					   tvweight.setText(states.get(j).getBloodPressureLow());
					   break;
				   }
			   }
			}
		}
	}
}
