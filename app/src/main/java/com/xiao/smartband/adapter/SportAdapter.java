package com.xiao.smartband.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiao.smartband.MyApplication;
import com.xiao.smartband.R;
import com.xiao.smartband.entity.Sport;

import java.util.ArrayList;

public class SportAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Sport> sports;
	private Context context;
	//int test = 0 ;

	public void setBreakRules(ArrayList<Sport> sports) {
		if(sports != null)
			this.sports = sports;
		else 
			sports = new ArrayList<Sport>();
	}
	public SportAdapter(Context context ,ArrayList<Sport> sports) {
		this.inflater = LayoutInflater.from(context);
		this.setBreakRules(sports);
		this.context = context;
	}
	public void changeData(ArrayList<Sport> musics){
		this.setBreakRules(musics);
		this.notifyDataSetChanged();
		this.notifyDataSetInvalidated();
	}
	@Override
	public int getCount() {
		return sports.size();
	}

	@Override
	public Object getItem(int position) {
		return sports.get(position);
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
		
		Sport sport = sports.get(position);
		
		if(MyApplication.activityStuts == 1){//步数
			holder.tvfinish.setText(sport.getStep());
			holder.tvdate.setText(sport.getDate());
			holder.tvfinishRate.setText(sport.getStepRate());
			
		}else if(MyApplication.activityStuts == 2){//卡路里
			holder.tvfinish.setText(sport.getKoloria());
			holder.tvdate.setText(sport.getDate());
			holder.tvfinishRate.setText(sport.getStepRate());
			
		}else if(MyApplication.activityStuts == 5){//运动时间
			holder.tvfinish.setText(sport.getSportTime());
			holder.tvdate.setText(sport.getDate());
			holder.tvfinishRate.setText(sport.getStepRate());
			
		}
		return convertView;
	}
	class ViewHolder {
		TextView tvdate;
		TextView tvfinish;
		TextView tvfinishRate;
	}
}
