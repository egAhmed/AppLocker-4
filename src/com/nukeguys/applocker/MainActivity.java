package com.nukeguys.applocker;

import java.util.ArrayList;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class MainActivity extends Activity implements OnClickListener
{
	private Button btnLogin;
	
	private AppLockerDbAdapter dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnLogin = (Button)findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(this);
		
		ArrayList<String> excludeProcNames = new ArrayList<String>();
		excludeProcNames.add(getPackageName());
		excludeProcNames.add("com.sec.android.app.launcher");
		excludeProcNames.add("com.android.contacts");
		excludeProcNames.add("com.android.mms");
		
		SharedPreferences preference = PrefUtil.getPref(this);
		PrefUtil.put(preference, "exclusive_processes", JSONUtil.makeJsonString(excludeProcNames));
		
//		dbAdapter = new AppLockerDbAdapter(this);
//		dbAdapter.open();
//		dbAdapter.addExclusiveProcess(getPackageName());
//		dbAdapter.addExclusiveProcess("com.sec.android.app.launcher");
//		dbAdapter.addExclusiveProcess("com.android.contacts");
//		dbAdapter.addExclusiveProcess("com.android.mms");
//		dbAdapter.close();
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.btn_login:
			AppLockerRestClient.get("", null, responseHandler);
			break;
		}
	}
	
	private AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler()
	{
		@Override
        public void onStart() 
		{
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] response) 
        {
        	Toast.makeText(MainActivity.this, "Login Sueccess!!",  Toast.LENGTH_SHORT).show();
//        	Log.i("ALL LOCKER", "res: " + new String(response));
        	Intent intent = new Intent(MainActivity.this, ManageServiceActivity.class);
        	startActivity(intent);
        	finish();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) 
        {
        }
	};
}
