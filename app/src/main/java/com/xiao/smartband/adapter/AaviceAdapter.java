package com.xiao.smartband.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiao.smartband.R;
import com.xiao.smartband.entity.Push;

import java.util.ArrayList;

public class AaviceAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Push> list;
	private Handler mhandler;
	public void setBreakRules(ArrayList<Push> list) {
		if(list != null)
			this.list = list;
		else 
			list = new ArrayList<Push>();
	}
	public AaviceAdapter(Context context ,Handler handler ,ArrayList<Push> list) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		this.setBreakRules(list);
		mhandler = handler;
	}
	public void changeData(ArrayList<Push> list){
		this.setBreakRules(list);
		this.notifyDataSetChanged();
		this.notifyDataSetInvalidated();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.bracelet_advice_item, null);
			holder.tvadviceTitle = (TextView) convertView.findViewById(R.id.tvadviceTitle);
			holder.tvadviceTime = (TextView) convertView.findViewById(R.id.tvadviceTime);
			holder.ivdetailAdvice = (ImageView) convertView.findViewById(R.id.ivdetailAdvice);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Push push = list.get(position);
		
		holder.tvadviceTitle.setText(push.getTitle());
		holder.tvadviceTime.setText(push.getDate());
		holder.ivdetailAdvice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
 				if(mhandler != null){
				   Message msg = new Message();
				   msg.what = 0;
				   msg.arg1 = position;
				   mhandler.sendMessage(msg);
				//   mhandler.sendMessage(msg);
				}
			}
		});
		return convertView;
	}
	class ViewHolder {
		TextView tvadviceTitle;
		TextView tvadviceTime;
		ImageView ivdetailAdvice;
	}
}
