package com.kaushal.cutorderplan.pdfgenerator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import com.kaushal.cutorderplan.R;
import com.kaushal.cutorderplan.OpenPDFActivity;
import com.kaushal.cutorderplan.markercalculation.MarkerResult;


public class AsyncPdfGenerator extends AsyncTask<String, Void, PDFgenerator> {
	
	private final ProgressDialog dialog;
	private final Context context;
	private static int intentnum = 0;
	private static String path = Environment.getExternalStorageDirectory().toString() + "/CutOrderPlan";

	
	public AsyncPdfGenerator(Context context, String style){
		this.context = context;
		dialog = ProgressDialog.show(context, "Generating Report", "Please wait. It may take upto few minutes.", true, true);
		execute(style);
	}
	
	public void onCancel(DialogInterface dialog) {
		this.cancel(true);
	}

	@Override
	protected PDFgenerator doInBackground(String... params) {
		PDFgenerator gen = new PDFgenerator(context);	
		return gen;
	}
	
	protected void onPostExecute(PDFgenerator gen) {
		
		MarkerResult pdftoopen = MarkerResult.LAST_CALCULATED_RESULT;
		String nwPath = path + "/" + pdftoopen.styleName + ".pdf";
		
		String tag = pdftoopen.styleName + intentnum + "";
		
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(ns);
		
		CharSequence tickerText = pdftoopen.styleName + " Report Generated";
		long when = System.currentTimeMillis();
		CharSequence contentTitle = pdftoopen.styleName + " CutOrderPlan Created";
		CharSequence contentText = "At " + nwPath;
		
		Intent notificationIntent = new Intent(context, OpenPDFActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, intentnum,
				notificationIntent.putExtra("path", nwPath), PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.cutplan)
		        .setTicker(tickerText)
		        .setWhen(when)
		        .setContentTitle(contentTitle)
		        .setContentText(contentText)
		        .setContentIntent(contentIntent)
		        .setAutoCancel(true)
		        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
		
		mNotificationManager.notify(tag, intentnum, mBuilder.build());
		
		intentnum += 1;
		
		dialog.dismiss();
	}


}
