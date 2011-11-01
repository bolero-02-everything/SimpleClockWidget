package jp.gr.java_conf.mi.app.simpleclock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

public class ClockWidgetProvider extends AppWidgetProvider
{
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		context.startService(ClockUpdateService.makeUpdateIntent(context));
	}

	@Override
	public void onDisabled(Context context)
	{
		super.onDisabled(context);
		context.stopService(ClockUpdateService.makeUpdateIntent(context));
	}
}
