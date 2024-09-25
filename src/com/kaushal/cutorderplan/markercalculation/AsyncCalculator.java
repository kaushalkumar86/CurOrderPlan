package com.kaushal.cutorderplan.markercalculation;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.kaushal.cutorderplan.MainApp;
import com.kaushal.cutorderplan.MarkerResultActivity;
import com.kaushal.cutorderplan.size.StyleData;

public class AsyncCalculator extends AsyncTask<String, Void, MarkerResult> implements OnCancelListener {

	private final ProgressDialog dialog;
	private final Context context;
	
	public AsyncCalculator(Context context, String style) {
		this.context = context;
		dialog = ProgressDialog.show(context, "Calculating Result", "Please wait. It may take upto a few minutes.",
			true, true, this);
		execute(style);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		this.cancel(true);
	}

	@Override
	protected MarkerResult doInBackground(String... params) {
		
		String styleName = params[0];
		StyleData styleData = MainApp.getDB().getStyleData(styleName);
		
		MarkerResult result = new MarkerResult();
		result.styleName = styleData.name;
		result.contigency = styleData.contigency;
		result.maxGarment = styleData.maxGarment;
		result.maxPlies = styleData.maxPlies;
		result.name = new ArrayList<String>();
		result.quant = new ArrayList<Integer>();
		result.position = new ArrayList<Integer>();
		int count = styleData.sizes.size();
		for(int i = 0; i< count; i++){
			result.name.add(styleData.sizes.get(i).size);
			result.quant.add(styleData.sizes.get(i).getCuttable());
			result.position.add(i);
			if (isCancelled()) {
				Log.v("Testing", "Task Cancelled");
				return null;
			}
		}
		result.beginCalc();
		// Keep this check in the lowest level of for loops to exit as soon as it is cancelled.
		if (isCancelled()) {
			Log.v("Testing", "Task Cancelled");
			return null;
		}
		
		return result;
	}

	@Override
	protected void onPostExecute(MarkerResult result) {
		MarkerResult.LAST_CALCULATED_RESULT = result;
		Intent resultIntent = new Intent(context, MarkerResultActivity.class);
		context.startActivity(resultIntent);
		dialog.dismiss();
	}


}
