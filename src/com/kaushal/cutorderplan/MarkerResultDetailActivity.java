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
import android.widget.TextView;

import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdRegistration;
import com.kaushal.cutorderplan.markercalculation.MarkerResult;
import com.kaushal.cutorderplan.markercalculation.MarkerResultSizeAdaptor;
import com.kaushal.cutorderplan.markercalculation.Row;

public class MarkerResultDetailActivity extends Activity {

	public static int selection;
	TextView mkr_plies;
	TextView mkr_break;
	TextView mkr_maxplies;
	
	public MarkerResultSizeAdaptor adaptor;
	private AdLayout adView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.marker_detail);
	    
	    AdRegistration.setAppKey(getResources().getString(R.string.appKeyAmazon));
		this.adView = (AdLayout) findViewById(R.id.adview);
        this.adView.loadAd();
        
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			
		    getActionBar().setHomeButtonEnabled(true);
		    
		}
	    
	    int selectPosi = getIntent().getExtras().getInt("select_posi");
	    selection = selectPosi;
	    setTitle("Marker " + (selection + 1));
	    mkr_plies = (TextView)findViewById(R.id.mkr_plies);
	    mkr_break = (TextView)findViewById(R.id.mkr_break);
	    mkr_maxplies = (TextView)findViewById(R.id.mkr_maxplies);
	    MarkerResult fetchSol = MarkerResult.LAST_CALCULATED_RESULT;
	    mkr_plies.setText(fetchSol.layPlies.get(selection).toString());
	    mkr_break.setText(fetchSol.layBreak.get(selection));
	    mkr_maxplies.setText(fetchSol.maxPlies + "");
	    
	    adaptor = new MarkerResultSizeAdaptor(this);
	    
	    ListView mkr_sizes = (ListView)findViewById(R.id.mkr_sizes);
	    mkr_sizes.setAdapter(adaptor);
	    Row temp = new Row(MarkerResult.LAST_CALCULATED_RESULT.count);
	    temp = MarkerResult.LAST_CALCULATED_RESULT.Result.get(selection);
	    for(int i = 0; i < MarkerResult.LAST_CALCULATED_RESULT.count; i++){
	    	adaptor.add(temp);
	    }
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
