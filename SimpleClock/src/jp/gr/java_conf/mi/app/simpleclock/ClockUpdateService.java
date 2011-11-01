package jp.gr.java_conf.mi.app.simpleclock;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ClockUpdateService extends Service
{
	private static final String Tag = ClockUpdateService.class.getSimpleName();

	private ScreenReceiver screenReceiver;
	private UpdateReceiver updateReceiver;
	private HandlerThread thread;
	private MyHandler handler;

	public static Intent makeUpdateIntent(Context context)
	{
		Intent it = new Intent(context, ClockUpdateService.class);
		return it;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		thread = new HandlerThread(ClockUpdateService.class.getSimpleName());
		thread.start();
		handler = new MyHandler(thread.getLooper());
		screenReceiver = new ScreenReceiver();
		screenReceiver.register(this);
		updateReceiver = new UpdateReceiver();
		updateReceiver.register(this);
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
		handler.obtainMessage(0, intent).sendToTarget();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
        onStart(intent, startId);
        return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		screenReceiver.unregister(this);
		screenReceiver = null;
		updateReceiver.unregister(this);
		updateReceiver = null;
		handler = null;
		thread.quit();
		thread = null;
	}

	private void onHandleIntent(Intent intent)
	{
		AppWidgetManager mgr = AppWidgetManager.getInstance(this);
		int[] ids = null;
		try {
			ids = mgr.getAppWidgetIds(new ComponentName(this, ClockWidgetProvider.class));
		} catch(Exception ex) {
			Log.w(Tag, "onHandleIntent", ex);
			return;
		}
		if(ids == null || ids.length <= 0)
		{
			stopSelf();
			return;
		}

		ClockUtils.updateClock(this, mgr, ids);

		Log.d(Tag, "onHandleIntent");
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	private class MyHandler extends Handler
	{
		public MyHandler(Looper looper)
		{
			super(looper);
		}

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			onHandleIntent((Intent)msg.obj);
		}
	}

	private class ScreenReceiver extends BroadcastReceiver
	{
		public void register(Context context)
		{
			try
			{
				IntentFilter fi = new IntentFilter(Intent.ACTION_SCREEN_ON);
				fi.addAction(Intent.ACTION_SCREEN_OFF);
				context.registerReceiver(this, fi);
			}
			catch(Exception ex)
			{
				Log.w(ScreenReceiver.class.getSimpleName() , "register", ex);
			}
		}

		public void unregister(Context context)
		{
			try
			{
				context.unregisterReceiver(this);
			}
			catch(Exception ex)
			{
				Log.w(ScreenReceiver.class.getSimpleName() , "unregister", ex);
			}
		}

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String act = intent.getAction();
			if(Intent.ACTION_SCREEN_ON.equals(act))
			{
				if(!updateReceiver.isRegistered())
				{
					updateReceiver.register(context);
					context.startService(ClockUpdateService.makeUpdateIntent(context));
				}
			}
			else if(Intent.ACTION_SCREEN_OFF.equals(act))
			{
				if(updateReceiver.isRegistered())
					updateReceiver.unregister(context);
			}
		}
	}

	private static class UpdateReceiver extends BroadcastReceiver
	{
		private boolean registered = false;

		public boolean isRegistered()
		{
			return registered;
		}

		public void register(Context context)
		{
			try
			{
				IntentFilter fi = new IntentFilter(Intent.ACTION_DATE_CHANGED);
				fi.addAction(Intent.ACTION_TIME_CHANGED);
				fi.addAction(Intent.ACTION_TIMEZONE_CHANGED);
				fi.addAction(Intent.ACTION_TIME_TICK);
				context.registerReceiver(this, fi);
				registered = true;
			}
			catch(Exception ex)
			{
				Log.w(UpdateReceiver.class.getSimpleName(), "register", ex);
			}
		}

		public void unregister(Context context)
		{
			try
			{
				context.unregisterReceiver(this);
				registered = false;
			}
			catch(Exception ex)
			{
				Log.w(UpdateReceiver.class.getSimpleName() , "unregister", ex);
			}
		}

		@Override
		public void onReceive(Context context, Intent intent)
		{
			if(intent == null)
				return;

			String act = intent.getAction();
			if(
					Intent.ACTION_DATE_CHANGED.equals(act)
					|| Intent.ACTION_TIME_CHANGED.equals(act)
					|| Intent.ACTION_TIMEZONE_CHANGED.equals(act)
					|| Intent.ACTION_TIME_TICK.equals(act)
			)
			{
				context.startService(ClockUpdateService.makeUpdateIntent(context));
			}
		}
	}
}
