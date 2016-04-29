package com.xiao.smartband.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiao.smartband.R;
import com.xiao.smartband.entity.HealthScan;

import java.util.ArrayList;

public class AdviceScanAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<HealthScan> scans;
	private Context context;
	//int test = 0 ;

	public void setBreakRules(ArrayList<HealthScan> scans) {
		if(scans != null)
			this.scans = scans;
		else 
			scans = new ArrayList<HealthScan>();
	}
	public AdviceScanAdapter(Context context ,ArrayList<HealthScan> scans) {
		this.inflater = LayoutInflater.from(context);
		this.setBreakRules(scans);
		this.context = context;
	}
	public void changeData(ArrayList<HealthScan> scans){
		this.setBreakRules(scans);
		this.notifyDataSetChanged();
		this.notifyDataSetInvalidated();
	}
	@Override
	public int getCount() {
		return scans.size();
	}

	@Override
	public Object getItem(int position) {
		return scans.get(position);
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
		
		HealthScan scan = scans.get(position);
		
		holder.tvdate.setText(scan.getHShealthDate());
		//holder.tvfinish.setText(sport.getFinish());
		holder.tvdata.setText(scan.getHShealthScore());
		return convertView;
	}
	class ViewHolder {
		TextView tvdate;
	//	TextView tvfinish;
		TextView tvdata;
	}
}
