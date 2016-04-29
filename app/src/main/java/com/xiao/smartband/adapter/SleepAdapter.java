package com.xiao.smartband.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiao.smartband.R;
import com.xiao.smartband.entity.Sleep;

import java.util.ArrayList;

public class SleepAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Sleep> sleeps;
	private Context context;
	//int test = 0 ;

	public void setBreakRules(ArrayList<Sleep> sleeps) {
		if(sleeps != null)
			this.sleeps = sleeps;
		else 
			sleeps = new ArrayList<Sleep>();
	}
	public SleepAdapter(Context context ,ArrayList<Sleep> sleeps) {
		this.inflater = LayoutInflater.from(context);
		this.setBreakRules(sleeps);
		this.context = context;
	}
	public void changeData(ArrayList<Sleep> musics){
		this.setBreakRules(musics);
		this.notifyDataSetChanged();
		this.notifyDataSetInvalidated();
	}
	@Override
	public int getCount() {
		return sleeps.size();
	}

	@Override
	public Object getItem(int position) {
		return sleeps.get(position);
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
			convertView = inflater.inflate(R.layout.bracelet_history_item, null);
			holder.tvdate = (TextView) convertView.findViewById(R.id.tvdate);
			holder.tvfinish = (TextView) convertView.findViewById(R.id.tvfinish);
			holder.tvfinishRate = (TextView) convertView.findViewById(R.id.tvfinishRate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Sleep sleep = sleeps.get(position);
		
		holder.tvdate.setText(sleep.getSleepDate());
		holder.tvfinish.setText(sleep.getSleepDeep());
		holder.tvfinishRate.setText(sleep.getSleepPercent());
		return convertView;
	}
	class ViewHolder {
		TextView tvdate;
		TextView tvfinish;
		TextView tvfinishRate;
	}
}
