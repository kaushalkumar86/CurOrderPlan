package com.kaushal.cutorderplan;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class OpenPDFActivity extends Activity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    String path = getIntent().getExtras().getString("path");
	    
	    Intent intent = new Intent();
	    intent.setAction(android.content.Intent.ACTION_VIEW);
	    File file = new File(path);
	    intent.setDataAndType(Uri.fromFile(file), "application/pdf"); 
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    startActivity(intent);
	    finish();
	    // TODO Auto-generated method stub
	}

}
