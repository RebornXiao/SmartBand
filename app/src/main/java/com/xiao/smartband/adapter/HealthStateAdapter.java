package com.xiao.smartband.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiao.smartband.R;
import com.xiao.smartband.entity.HealthState;

import java.util.ArrayList;
import java.util.List;

public class HealthStateAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<HealthState> states;
	private Context context;
	//int test = 0 ;

	public void setBreakRules(List<HealthState> states) {
		if(states != null)
			this.states = states;
		else 
			states = new ArrayList<HealthState>();
	}
	public HealthStateAdapter(Context context ,List<HealthState> states) {
		this.inflater = LayoutInflater.from(context);
		this.setBreakRules(states);
		this.context = context;
	}
	public void changeData(ArrayList<HealthState> states){
		this.setBreakRules(states);
		this.notifyDataSetChanged();
		this.notifyDataSetInvalidated();
	}
	@Override
	public int getCount() {
		return states.size();
	}

	@Override
	public Object getItem(int position) {
		return states.get(position);
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
			convertView = inflater.inflate(R.layout.bracelet_history_state_item, null);
			holder.tvdate = (TextView) convertView.findViewById(R.id.tvdate);
			holder.tvhigPre = (TextView) convertView.findViewById(R.id.tvhigPre);
			holder.tvlowPre = (TextView) convertView.findViewById(R.id.tvlowPre);
			holder.tvbefSuagar = (TextView) convertView.findViewById(R.id.tvbreSugar);
			holder.tvaftSuagar = (TextView) convertView.findViewById(R.id.tvafterSugar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		HealthState  state = states.get(position);
		
		holder.tvdate.setText(state.getDate());
		holder.tvhigPre.setText(state.getBloodPressure());
		holder.tvlowPre.setText(state.getBloodPressureLow());
		holder.tvbefSuagar.setText(state.getBloodSugar());
		holder.tvaftSuagar.setText(state.getBloodSugarAfter());
		return convertView;
	}
	class ViewHolder {
		TextView tvdate;
		TextView tvhigPre;
		TextView tvlowPre;
		TextView tvbefSuagar;
		TextView tvaftSuagar;
	}
}
