package com.kaushal.cutorderplan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdRegistration;
import com.kaushal.cutorderplan.markercalculation.AsyncCalculator;
import com.kaushal.cutorderplan.size.SizeData;
import com.kaushal.cutorderplan.size.SizeDataAdaptor;
import com.kaushal.cutorderplan.size.SizeDataDialog;
import com.kaushal.cutorderplan.size.StyleData;

public class EditStyleActivity extends Activity implements OnFocusChangeListener, OnClickListener {

	SeekBar contigency_select;
	TextView total_val;
	EditText max_plies;
	EditText max_garment;

	public SizeDataAdaptor adaptor;
	public StyleData styleData;

	private AdLayout adView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.style_detail);
		
		AdRegistration.setAppKey(getResources().getString(R.string.appKeyAmazon));
		this.adView = (AdLayout) findViewById(R.id.adview);
        this.adView.loadAd();
		
		//enable home icon
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			
		    getActionBar().setHomeButtonEnabled(true);
		    
		}

		String styleName = getIntent().getExtras().getString("styleName");
		setTitle(styleName);

		styleData = MainApp.getDB().getStyleData(styleName);
		
		findViewById(R.id.calc).setOnClickListener(this);

		final TextView contigency_val = (TextView)findViewById(R.id.contigency_val); 
		contigency_select = (SeekBar)findViewById(R.id.contigency_select);
		contigency_select.setProgress(styleData.contigency);
		contigency_val.setText(styleData.contigency + " %");
		contigency_select.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				contigency_val.setText(arg1 + " %");

			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int contigency = seekBar.getProgress();
				for (int i = adaptor.getCount() - 1; i >=0; i--) {
					adaptor.getItem(i).setContigency(contigency);
				}
				adaptor.notifyDataSetChanged();
				styleData.contigency = contigency;
				setTotal();
				MainApp.getDB().updateStyleData(styleData);
			}
		});

		adaptor = new SizeDataAdaptor(this);
		ListView size_data_list = (ListView)findViewById(R.id.size_data_list);
		size_data_list.setAdapter(adaptor);
		size_data_list.setOnItemClickListener(adaptor);
		int x = styleData.sizes.size();
		for(int i = 0; i < x; i++){
			adaptor.add(styleData.sizes.get(i));
		}
		

		Button add_size = (Button)findViewById(R.id.add_size);
		add_size.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SizeData size = new SizeData();
				size.setContigency(contigency_select.getProgress());
				SizeDataDialog ae_size = new SizeDataDialog(size, EditStyleActivity.this, false);
				ae_size.show();

			}
		});

		total_val = (TextView)findViewById(R.id.total_val);
		setTotal();
		
		max_plies = (EditText)findViewById(R.id.max_plies);
		max_plies.setText(styleData.maxPlies + "");
		max_plies.setOnFocusChangeListener(this);
		
		max_garment = (EditText)findViewById(R.id.max_garment);
		max_garment.setText(styleData.maxGarment + "");
		max_garment.setOnFocusChangeListener(this);


	}
	public void setTotal(){
		int tottal_cuttable = 0;
		for (int i = adaptor.getCount() - 1; i >=0; i--) {
			tottal_cuttable += adaptor.getItem(i).getCuttable();
		}
		total_val.setText(tottal_cuttable + " Pieces");
	}
	
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		updateStyleData();		
	}
	
	
	private void updateStyleData(){
		styleData.maxPlies = parseInt(max_plies);
		styleData.maxGarment = parseInt(max_garment);
		MainApp.getDB().updateStyleData(styleData);
	}
	
	private int parseInt(EditText input) {
		try {
			int val = Integer.parseInt(input.getText().toString());
			return val > 0 ? val : 0;
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	//handling menu clicks
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater mi = getMenuInflater();
	    mi.inflate(R.menu.activity_main, menu);
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
	    	
	        case R.id.menu_delete:
	        	
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	builder.setMessage("Are you sure you want to delete style?")
	        			.setTitle("Confirm")
	        	       .setCancelable(false)
	        	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	        	   MainApp.getDB().deleteStyleData(styleData);
	        		           finish();
	        		           return;
	        	           }
	        	       })
	        	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	                dialog.cancel();
	        	                return;
	        	           }
	        	       });
	        	AlertDialog alert = builder.create();
	        	alert.show();
	        	
	            
	            return true;
	        case R.id.menu_rename:
	        	final EditText input = new EditText(this);
	        	new AlertDialog.Builder(this)
				.setTitle("New Style Name")
				.setView(input)
				.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String newName = input.getText().toString();
						if (newName.equals("")) {
							Toast.makeText(getApplicationContext(), "Name cannot be empty.", Toast.LENGTH_SHORT).show();
							return;
						}
						Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
						Matcher m = p.matcher(newName);
						boolean b = m.find();
						if(b){
							Toast.makeText(getApplicationContext(), "Invalid Characters.", Toast.LENGTH_SHORT).show();
							return;
						}
						if ( Character.isDigit(newName.charAt(0)) )
						{
							Toast.makeText(getApplicationContext(), "Name should start with an alphabet.", Toast.LENGTH_SHORT).show();
						    return;
						}
						MainApp.getDB().renameStyledata(styleData, newName);
						styleData.name = newName;
						setTitle(styleData.name);
					}
				}).create().show();
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

	@Override
	public void onClick(View v) {
		updateStyleData();
		StyleData checkreason = MainApp.getDB().getStyleData(styleData.name);
		boolean chk = false;
		if(checkreason.maxGarment == 0){
			chk = true;
		}
		if(checkreason.maxPlies == 0){
			chk = true;
		}
		if(checkreason.sizes.size() == 0){
			chk = true;
		}
		if(chk){
			Toast.makeText(getApplicationContext(), "Insufficient Data.", Toast.LENGTH_SHORT).show();
		}
		else{
			new AsyncCalculator(this, styleData.name);
		}
		
		
	}
	
}
