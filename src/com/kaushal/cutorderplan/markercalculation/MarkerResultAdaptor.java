package com.kaushal.cutorderplan.markercalculation;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kaushal.cutorderplan.MarkerResultActivity;
import com.kaushal.cutorderplan.MarkerResultDetailActivity;
import com.kaushal.cutorderplan.R;



public class MarkerResultAdaptor extends ArrayAdapter<MarkerResult> implements OnItemClickListener {
	
	private final LayoutInflater inflater;
	private final MarkerResultActivity main;
	
	
	public MarkerResultAdaptor(MarkerResultActivity context){
		super(context, R.layout.marker_data, R.id.marker_name);
		inflater = LayoutInflater.from(context);
		this.main = context;
		
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Holder holder;
		if (convertView == null){
			convertView = inflater.inflate(R.layout.marker_data, null);
			holder = new Holder();
			holder.marker_name = (TextView)convertView.findViewById(R.id.marker_name);
			holder.plies_num = (TextView)convertView.findViewById(R.id.plies_num);
			holder.mark_break = (TextView)convertView.findViewById(R.id.mark_break);
			convertView.setTag(holder);
		}else {
			holder = (Holder)convertView.getTag();
		}
		
		MarkerResult showRes = MarkerResult.LAST_CALCULATED_RESULT;
		
		holder.marker_name.setText("Marker : " + (position + 1));
		holder.plies_num.setText(showRes.layPlies.get(position) + " Plies");
		holder.mark_break.setText("BreakUp " + showRes.layBreak.get(position));
		
		return convertView;
	}
	
	private class Holder {
		TextView marker_name;
		TextView plies_num;
		TextView mark_break;
	}
	
	
	public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
		//int x = MarkerResult.LAST_CALCULATED_RESULT.soluNum.get(position);
		Log.v("Testing", position + "");
		showStyle(position);
		
	}
	
	private void showStyle(int posi) {
		Intent intent = new Intent(main, MarkerResultDetailActivity.class);
		intent.putExtra("select_posi", posi);
		main.startActivity(intent);
	}
}
