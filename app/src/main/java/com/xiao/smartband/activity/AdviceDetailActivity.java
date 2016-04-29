package com.xiao.smartband.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiao.smartband.MyApplication;
import com.xiao.smartband.R;
import com.xiao.smartband.data.ModelDao;
import com.xiao.smartband.entity.Push;
import com.xiao.smartband.view.TitleView;

public class AdviceDetailActivity extends Activity {
	private TitleView titleView;
	private TextView tvDetailCon,tvadviceTitle;
	private Intent intent = null;
	private SpannableString msp = null;
	private PopupWindow adviceWindow;
	private View adviceView;
	private AlertDialog diaLog;
	private ModelDao model;
	private int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bracelet_advice_detail);
		init();
	}
	private void init(){
		model = new ModelDao(getApplicationContext());
		titleView = (TitleView)findViewById(R.id.titleview);
		titleView.setbg(R.drawable.hearlth_advice_titlebg);
		titleView.titleImg(R.drawable.hearlth_advice_titleimg);
		tvDetailCon = (TextView)findViewById(R.id.tvadviceDetailCon);
		tvadviceTitle = (TextView)findViewById(R.id.tvadviceTitle);
		intent = getIntent();
		String value = intent.getStringExtra("value");
		id = Integer.parseInt(value);
		if(MyApplication.adviceNomalorCollect == false){
			titleView.setTitle(R.string.healthyAdviceDetail);
			
			tvadviceTitle.setText(MyApplication.pushsNomal.get(id).getTitle());
			if(MyApplication.pushsNomal.get(id).getContent().startsWith("http")){
				String con = MyApplication.pushsNomal.get(id).getContent();
				int len = MyApplication.pushsNomal.get(id).getContent().length();
				msp = new SpannableString(con);
				msp.setSpan(new URLSpan(con),0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			    msp.setSpan(new ForegroundColorSpan(Color.BLACK),0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				tvDetailCon.setMovementMethod(LinkMovementMethod.getInstance()); 
				tvDetailCon.setText(msp); 
			}else {
				tvDetailCon.setText(MyApplication.pushsNomal.get(id).getContent());
			}
			
		}else {
			titleView.setTitle(R.string.healthyAdviceCollectDetail);
			
			tvadviceTitle.setText(MyApplication.pushsCollect.get(id).getTitle());
			if(MyApplication.pushsCollect.get(id).getContent().startsWith("http")){
				String con = MyApplication.pushsCollect.get(id).getContent();
				int len = MyApplication.pushsCollect.get(id).getContent().length();
				msp = new SpannableString(con);
				msp.setSpan(new URLSpan(con),0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			    msp.setSpan(new ForegroundColorSpan(Color.BLACK),0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				tvDetailCon.setMovementMethod(LinkMovementMethod.getInstance()); 
				tvDetailCon.setText(msp); 
			}else {
				tvDetailCon.setText(MyApplication.pushsCollect.get(id).getContent());
			}
		}
		
		adviceView = this.getLayoutInflater().inflate(R.layout.bracelet_advice_operate, null);
		TextView tvoperate = (TextView)adviceView.findViewById(R.id.tvadviceCollect);
		TextView tvdel = (TextView)adviceView.findViewById(R.id.tvadviceDel);
		DisplayMetrics dm = new DisplayMetrics();getWindowManager().getDefaultDisplay().getMetrics(dm);
	    int  width = dm.widthPixels / 4;//宽度
	    int  height = dm.heightPixels ;//高度//780 1280
		adviceWindow = new PopupWindow(adviceView, width,
				       LayoutParams.WRAP_CONTENT, true);
		adviceWindow.setOutsideTouchable(true);
		adviceWindow.setBackgroundDrawable(new BitmapDrawable());
		
		titleView.setBack(R.drawable.steps_back, new TitleView.onBackLister() {
			
			@Override 
			public void onClick(View button) {
				finish();
			}
		});
		if(MyApplication.adviceNomalorCollect == false){
			titleView.right(R.string.operate, new TitleView.onSetLister() {
				
				@Override
				public void onClick(View view) {
					if(MyApplication.pushsNomal.size() > 0 ){
						adviceWindow.showAsDropDown(view, -3, 0);
					}else {
						MyApplication.ways.ToastShow(getApplicationContext(), "没有数据");
					}
				}
			});
		}
		
		tvoperate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//收藏 从新做处理
 				if(MyApplication.pushsCollect.size() > 0){
				   for(int i =0; i<MyApplication.pushsCollect.size(); i++){
					   if(MyApplication.pushsNomal.get(id).getDate().
						  equals(MyApplication.pushsCollect.get(i).getDate())){
				   MyApplication.ways.ToastShow(getApplicationContext(), "该建议已收藏，不能重复收藏");
				   return;
					   }
				   }
				}
				Push push = new Push();
				push.setTitle(MyApplication.pushsNomal.get(id).getTitle());
				push.setContent(MyApplication.pushsNomal.get(id).getContent());
				push.setDate(MyApplication.pushsNomal.get(id).getDate());
				push.setCollect(1);
				model.insertHealthPush(push);
				MyApplication.ways.ToastShow(getApplicationContext(), "收藏成功");
				adviceWindow.dismiss();
			}
		});
		tvdel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//删除
				delAdviceDialog();
			}
		});
	}
	private void delAdviceDialog(){
		 Builder builder = new Builder(AdviceDetailActivity.this);
		 diaLog = builder.setMessage("                   删除后不可恢复").setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				model.deleteHealthAdvice(MyApplication.pushsNomal.get(id).getId());
				MyApplication.pushsNomal.remove(id);
				diaLog.dismiss();
				adviceWindow.dismiss();
				MyApplication.ways.ToastShow(getApplicationContext(), "删除成功");
				MyApplication.adviceDel = true;
				finish();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				diaLog.dismiss();
				adviceWindow.dismiss();
			}
		}).create();
		 diaLog.show();
	 }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1){
			if(MyApplication.pushsNomal.size() > 0){
				//String sss = data.getStringExtra("positionId");
			   String content = MyApplication.pushsNomal.get(requestCode).getContent();
				tvDetailCon.setText(content);
			}
		}
	}
}
