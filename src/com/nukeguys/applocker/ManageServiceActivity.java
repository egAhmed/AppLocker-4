package com.nukeguys.applocker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ManageServiceActivity extends Activity implements OnClickListener
{
	private Button	btnStart;
	private Button	btnStop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_service);
		
		btnStart = (Button) findViewById(R.id.btn_start);
		btnStop = (Button) findViewById(R.id.btn_stop);

		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.btn_start:
			startService(new Intent(this, AppLockService.class));
			// startService(new Intent("com.nukeguys.applocker.lockerService"));
			break;
		case R.id.btn_stop:
			stopService(new Intent(this, AppLockService.class));
			// stopService(new Intent("com.nukeguys.applocker.lockerService"));
			break;
		}
	}
}
