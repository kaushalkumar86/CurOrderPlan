package com.kaushal.cutorderplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdRegistration;
import com.kaushal.cutorderplan.markercalculation.MarkerResult;
import com.kaushal.cutorderplan.markercalculation.MarkerResultAdaptor;
import com.kaushal.cutorderplan.pdfgenerator.AsyncPdfGenerator;

public class MarkerResultActivity extends Activity implements android.view.View.OnClickListener {
	
	
	public MarkerResultAdaptor adaptor;
	private AdLayout adView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.marker_result);
	    
	    AdRegistration.setAppKey(getResources().getString(R.string.appKeyAmazon));
		this.adView = (AdLayout) findViewById(R.id.adview);
        this.adView.loadAd();
	    
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			
		    getActionBar().setHomeButtonEnabled(true);
		    
		}

	    setTitle("Marker Solution");
	    
	    findViewById(R.id.pdf_gen).setOnClickListener(this);
	    
	    
	    adaptor = new MarkerResultAdaptor(this);
	    ListView result_data = (ListView)findViewById(R.id.results);
	    result_data.setAdapter(adaptor);
	    result_data.setOnItemClickListener(adaptor);
	    int resuNum = MarkerResult.LAST_CALCULATED_RESULT.layPlies.size();
	    for(int i = 0; i < resuNum; i++){
	    	adaptor.add(MarkerResult.LAST_CALCULATED_RESULT);
	    }
	    
	    
	    //MarkerResult.LAST_CALCULATED_RESULT.Result;
	    
	}

	@Override
	public void onClick(View v) {
		
		new AsyncPdfGenerator(this, "name");
		
	}
	
	//menu options
    public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater mi = getMenuInflater();
	    mi.inflate(R.menu.rest_menu, menu);
	    return true;

    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
		    case android.R.id.home:
	            Log.v("Testing", "home button");
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	            return true;
	            
	    	case R.id.menu_about:
	        	AlertDialog.Builder abtbuilder;
	        	AlertDialog alertDialog;
	        	View about;
	        	Context mContext = this;        	
	        	
	        	about = LayoutInflater.from(mContext).inflate(R.layout.about, null);
	       
	        	abtbuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Dialog_Fullscreen));
	        	abtbuilder.setView(about);
	        	abtbuilder.setCancelable(true);
	        	
	        	
	        	alertDialog = abtbuilder.create();
	        	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	            lp.copyFrom(alertDialog.getWindow().getAttributes());
	            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
	            
	        	
	        	try{
	        		alertDialog.show();
	        		alertDialog.getWindow().setAttributes(lp);
	        		
	        	}catch(Exception e) {
	        		Log.v("Testing", e.toString());
	        	}
	        	
	        	
	        	
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
