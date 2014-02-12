package com.nukeguys.applocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppLockBroadcastReceiver extends BroadcastReceiver 
{
	public AppLockBroadcastReceiver() 
	{
	}

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		String action = intent.getAction();
		
		if (action.equals("android.intent.action.BOOT_COMPLETED"))
		{
//			ComponentName component = new ComponentName(context.getPackageName(), AppLockService.class.getName());
			context.startService(new Intent(context, AppLockService.class));
		}
	}
}
