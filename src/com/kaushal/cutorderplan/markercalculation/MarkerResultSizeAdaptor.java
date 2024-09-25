package com.kaushal.cutorderplan.markercalculation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kaushal.cutorderplan.R;
import com.kaushal.cutorderplan.MarkerResultDetailActivity;

public class MarkerResultSizeAdaptor extends ArrayAdapter<Row> {
	
	private final LayoutInflater inflater;

	public MarkerResultSizeAdaptor(MarkerResultDetailActivity context) {
		super(context, R.layout.marker_sizes, R.id.mkr_size_name);
		inflater = LayoutInflater.from(context);
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		Holder holder;
		if (convertView == null){
			convertView = inflater.inflate(R.layout.marker_sizes, null);
			holder = new Holder();
			holder.mkr_size_name = (TextView)convertView.findViewById(R.id.mkr_size_name);
			holder.mkr_size_value = (TextView)convertView.findViewById(R.id.mkr_size_value);
			convertView.setTag(holder);
		}else {
			holder = (Holder)convertView.getTag();
		}
		int seltposi = MarkerResultDetailActivity.selection;
		
		holder.mkr_size_name.setText(MarkerResult.LAST_CALCULATED_RESULT.name.get(position) + "");
		holder.mkr_size_value.setText(MarkerResult.LAST_CALCULATED_RESULT.Result.get(seltposi).cells[position] + "");
		return convertView;
	}
	
	private class Holder {
		TextView mkr_size_name;
		TextView mkr_size_value;
	}

}
