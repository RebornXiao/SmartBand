package com.xiao.smartband.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiao.smartband.R;
import com.xiao.smartband.entity.HeartRate;

import java.util.ArrayList;

public class HeartRateAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<HeartRate> rates;
	private Context context;
	//int test = 0 ;

	public void setBreakRules(ArrayList<HeartRate> rates) {
		if(rates != null)
			this.rates = rates;
		else 
			rates = new ArrayList<HeartRate>();
	}
	public HeartRateAdapter(Context context ,ArrayList<HeartRate> rates) {
		this.inflater = LayoutInflater.from(context);
		this.setBreakRules(rates);
		this.context = context;
	}
	public void changeData(ArrayList<HeartRate> rates){
		this.setBreakRules(rates);
		this.notifyDataSetChanged();
		this.notifyDataSetInvalidated();
	}
	@Override
	public int getCount() {
		return rates.size();
	}

	@Override
	public Object getItem(int position) {
		return rates.get(position);
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
			convertView = inflater.inflate(R.layout.bracelet_history_two_item, null);
			holder.tvdate = (TextView) convertView.findViewById(R.id.tvdate);
			//holder.tvfinish = (TextView) convertView.findViewById(R.id.tvfinish);
			holder.tvdata = (TextView) convertView.findViewById(R.id.tvdata);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		HeartRate rate = rates.get(position);
		
		holder.tvdate.setText(rate.getHeartDate());
		//holder.tvfinish.setText(sport.getFinish());
		holder.tvdata.setText(rate.getHeartRate());
		return convertView;
	}
	class ViewHolder {
		TextView tvdate;
	//	TextView tvfinish;
		TextView tvdata;
	}
}
