package com.nukeguys.applocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver 
{
	public BootBroadcastReceiver() 
	{
	}

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		String action = intent.getAction();
		
		if (action.equals("android.intent.action.BOOT_COMPLETED"))
		{
			context.startService(new Intent(context, AppLockService.class));
		}
		else if(action.equals("android.intent.action.ACTION_SHUTDOWN"))
		{
			context.stopService(new Intent(context, AppLockService.class));
		}
	}
}