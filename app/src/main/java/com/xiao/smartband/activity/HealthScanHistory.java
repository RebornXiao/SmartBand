package com.xiao.smartband.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.xiao.smartband.R;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.HealthScan;
import com.xiao.smartband.view.ScanView;
import com.xiao.smartband.view.TitleView;

import java.util.ArrayList;

public class HealthScanHistory extends Activity {
	private TitleView titleView;
	private ScanView view1,view2,view3;
	private ScanView view4,view5,view6;
	private ScanView view7,view8,view9;
	ArrayList<HealthScan> scans;
	private ModelDao model;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_health_scan_his);
		init();
	}
	private void init(){
		titleView = (TitleView)findViewById(R.id.titleview);
		model = new ModelDao(getApplicationContext());
		scans = model.HealthScanSearth();
		view1 = (ScanView)findViewById(R.id.view1);
		view2 = (ScanView)findViewById(R.id.view2);
		view3 = (ScanView)findViewById(R.id.view3);
		view4 = (ScanView)findViewById(R.id.view4);
		view5 = (ScanView)findViewById(R.id.view5);
		view6 = (ScanView)findViewById(R.id.view6);
		view7 = (ScanView)findViewById(R.id.view7);
		view8 = (ScanView)findViewById(R.id.view8);
		view9 = (ScanView)findViewById(R.id.view9);
		
		//处理数据,最多10条，9条历史显示倒数2,3,4，5,6.。。
		if(scans.size() == 10){
			view1.dealData(1);
			view2.dealData(2);
			view3.dealData(3);
			view4.dealData(4);
			view5.dealData(5);
			view6.dealData(6);
			view7.dealData(7);
			view8.dealData(8);
			view9.dealData(9);
		}else if(scans.size() == 9){

			view1.dealData(1);
			view2.dealData(2);
			view3.dealData(3);
			view4.dealData(4);
			view5.dealData(5);
			view6.dealData(6);
			view7.dealData(7);
			view8.dealData(8);
			view9.setVisibility(View.GONE);
		}else if(scans.size() == 8){

			view1.dealData(1);
			view2.dealData(2);
			view3.dealData(3);
			view4.dealData(4);
			view5.dealData(5);
			view6.dealData(6);
			view7.dealData(7);
			view8.setVisibility(View.GONE);
			view9.setVisibility(View.GONE);
		}else if(scans.size() == 7){

			view1.dealData(1);
			view2.dealData(2);
			view3.dealData(3);
			view4.dealData(4);
			view5.dealData(5);
			view6.dealData(6);
			view7.setVisibility(View.GONE);
			view8.setVisibility(View.GONE);
			view9.setVisibility(View.GONE);
		}else if(scans.size() == 6){

			view1.dealData(1);
			view2.dealData(2);
			view3.dealData(3);
			view4.dealData(4);
			view5.dealData(5);
			view6.setVisibility(View.GONE);
			view7.setVisibility(View.GONE);
			view8.setVisibility(View.GONE);
			view9.setVisibility(View.GONE);
		}else if(scans.size() == 5){
			view1.dealData(1);
			view2.dealData(2);
			view3.dealData(3);
			view4.dealData(4);
			view5.setVisibility(View.GONE);
			view6.setVisibility(View.GONE);
			view7.setVisibility(View.GONE);
			view8.setVisibility(View.GONE);
			view9.setVisibility(View.GONE);
		}else if(scans.size() == 4){
			view1.dealData(1);
			view2.dealData(2);
			view3.dealData(3);
			view4.setVisibility(View.GONE);
			view5.setVisibility(View.GONE);
			view6.setVisibility(View.GONE);
			view7.setVisibility(View.GONE);
			view8.setVisibility(View.GONE);
			view9.setVisibility(View.GONE);
		}else if(scans.size() == 3){
			view1.dealData(1);
			view2.dealData(2);
			view3.setVisibility(View.GONE);
			view4.setVisibility(View.GONE);
			view5.setVisibility(View.GONE);
			view6.setVisibility(View.GONE);
			view7.setVisibility(View.GONE);
			view8.setVisibility(View.GONE);
			view9.setVisibility(View.GONE);
		}else if(scans.size() == 2){
			view1.dealData(1);
			view2.setVisibility(View.GONE);
			view3.setVisibility(View.GONE);
			view4.setVisibility(View.GONE);
			view5.setVisibility(View.GONE);
			view6.setVisibility(View.GONE);
			view7.setVisibility(View.GONE);
			view8.setVisibility(View.GONE);
			view9.setVisibility(View.GONE);
		}else if(scans.size() == 1){
			view1.setVisibility(View.GONE);
			view2.setVisibility(View.GONE);
			view3.setVisibility(View.GONE);
			view4.setVisibility(View.GONE);
			view5.setVisibility(View.GONE);
			view6.setVisibility(View.GONE);
			view7.setVisibility(View.GONE);
			view8.setVisibility(View.GONE);
			view9.setVisibility(View.GONE);
		}
		titleView.setTitle(R.string.healthy_his);
		titleView.setbg(R.drawable.health_santitleimg);
		titleView.titleImg(R.drawable.health_scanimg);
		
		titleView.setBack(R.drawable.steps_back, new TitleView.onBackLister() {
			
			@Override
			public void onClick(View button) {
				finish();
			}
		});
	}
}
