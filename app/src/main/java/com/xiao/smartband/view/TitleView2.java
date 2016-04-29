package com.xiao.smartband.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiao.smartband.R;


public class TitleView2 extends LinearLayout implements OnClickListener{
	private LinearLayout layout_top_bg;
	private ImageView ivback;
	private TextView mTitle,tvright;
	private onBackLister monClickListener;
	private onShareLister mShareonClickListener;
	private onSetLister monSetClickListener;
	public interface onBackLister {
		public void onClick(View button);
	}

	public interface onShareLister {
		public void onClick(View button);
	}
	
	public interface onSetLister {
		public void onClick(View button);
	}
	
	public void setbg(int res){
		layout_top_bg.setBackgroundResource(res);
	}
	
	public void setBack(int res, onBackLister listener) {
		ivback.setBackgroundResource(res);
		ivback.setVisibility(View.VISIBLE);
		monClickListener = listener;
	}
	
	public void HiddenBackImg() {
		ivback.setVisibility(View.INVISIBLE);
	}
	
	public void showBack() {
		ivback.setVisibility(View.VISIBLE);
	}
	
	public void right( int resid,onSetLister listener) {
		//tvright.setBackgroundResource(resid);
		tvright.setText(resid);
		tvright.setVisibility(View.VISIBLE);
		monSetClickListener = listener;
	}
	public void HiddenSetImg() {
		tvright.setVisibility(View.INVISIBLE);
	}
	
	public void showSetImg() {
		tvright.setVisibility(View.VISIBLE);
	}

	public TitleView2(Context context) {
		this(context, null);
	}

	public TitleView2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("NewApi")
	public TitleView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.title_view2, this, true);
		layout_top_bg = (LinearLayout)findViewById(R.id.title_bg);
		mTitle = (TextView)findViewById(R.id.tvTitle);
		ivback = (ImageView)findViewById(R.id.ivback);
		tvright = (TextView)findViewById(R.id.tvright);
		ivback.setOnClickListener(this);
		tvright.setOnClickListener(this);
	}
	public void setTitle(int stringID) {
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(stringID);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivback:
			monClickListener.onClick(v);
			break;

		case R.id.ivtitleimg:
			mShareonClickListener.onClick(v);
			break;
		case R.id.tvright:

			monSetClickListener.onClick(v);
			break;
		}
	}

}
