//package jp.gr.java_conf.mi.app.simpleclock;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.util.Log;
//
//public class ClockReceiver extends BroadcastReceiver
//{
//	private static final String Tag = ClockReceiver.class.getSimpleName();
//
//	public void register(Context context)
//	{
//		try
//		{
//			IntentFilter fi = new IntentFilter(Intent.ACTION_SCREEN_ON);
//			fi.addAction(Intent.ACTION_SCREEN_OFF);
//			fi.addAction(Intent.ACTION_DATE_CHANGED);
//			fi.addAction(Intent.ACTION_TIME_CHANGED);
//			fi.addAction(Intent.ACTION_TIMEZONE_CHANGED);
//			fi.addAction(Intent.ACTION_TIME_TICK);
//			context.registerReceiver(this, fi);
//		}
//		catch(Exception ex)
//		{
//			Log.w(Tag , "register", ex);
//		}
//	}
//
//	public void unregister(Context context)
//	{
//		try
//		{
//			context.unregisterReceiver(this);
//		}
//		catch(Exception ex)
//		{
//			Log.w(Tag , "unregister", ex);
//		}
//	}
//
//	@Override
//	public void onReceive(Context context, Intent intent)
//	{
//		if(intent == null)
//			return;
//
//		String act = intent.getAction();
//		if(Intent.ACTION_SCREEN_ON.equals(act))
//		{
//			context.startService(ClockUpdateService.makeScreenStateIntent(context, true));
//		}
//		else if(Intent.ACTION_SCREEN_OFF.equals(act))
//		{
//			context.startService(ClockUpdateService.makeScreenStateIntent(context, false));
//		}
//		else if(
//				Intent.ACTION_DATE_CHANGED.equals(act)
//				|| Intent.ACTION_TIME_CHANGED.equals(act)
//				|| Intent.ACTION_TIMEZONE_CHANGED.equals(act)
//				|| Intent.ACTION_TIME_TICK.equals(act)
//		)
//		{
//			context.startService(ClockUpdateService.makeUpdateIntent(context));
//		}
//	}
//}
