package com.android.systemui.tranqtoggles;



import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SyncStatusObserver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.android.systemui.R;
import com.android.systemui.tranqtoggles.TranqGpsButton.GpsObserver;


public class TranqSyncButton extends TranqToggleButton {
	
	private View mIndicator;
	private View mIcon;
	private View mDivider;
	private BroadcastReceiver mBroadcastReciver;
	private Cursor syncCursor;
	Handler mHandler = new Handler();

	
	
	private SyncStatusObserver syncStatusObserver = new SyncStatusObserver() {

	    @Override
	    public void onStatusChanged(int which) {
            mHandler.post(new Runnable() {
                public void run() {
                	updateResources();;
                }
            });
	    	
	    	
	    }
	};

	
	
	
	
	public TranqSyncButton(Context context, AttributeSet attrs) {
		super(context, attrs);

	}
	
	

	
	
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		
		mIndicator = (View) getRootView().findViewById(R.id.indicator_8);
		mIcon = (View) getRootView().findViewById(R.id.sync_icon);	
		mDivider = (View) getRootView().findViewById(R.id.divider_8);
		
		ContentResolver.addStatusChangeListener(ContentResolver.SYNC_OBSERVER_TYPE_SETTINGS , syncStatusObserver);
		
		updateResources();
	}

	
	protected void onDetachedFromWindow(){
		
		ContentResolver.removeStatusChangeListener(syncStatusObserver);
		
	}


	@Override
	protected boolean getStatusOn(){

		return ContentResolver.getMasterSyncAutomatically();

	}

	@Override
	void updateResources() {
		
		
		if (ContentResolver.getMasterSyncAutomatically()) {
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_sync_on);
			mIndicator.setBackgroundColor(TranqToggleView.mToggleIndOnColor);
			setTextColor(TranqToggleView.mToggleTextOnColor);
			
		} else {
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_sync_off);
			mIndicator.setBackgroundColor(TranqToggleView.mToggleIndOffColor);
			setTextColor(TranqToggleView.mToggleTextOffColor);
		}
		
		mDivider.setBackgroundColor(TranqToggleView.mToggleDivColor);
	}



	@Override
	void toggleOn() {

		ContentResolver.setMasterSyncAutomatically(true);
		updateResources();
		
	}


	@Override
	void toggleOff() {

		ContentResolver.setMasterSyncAutomatically(false);
		updateResources();
	}


	@Override
	void showSettings() {

	    Intent i = new Intent();
	    i.setAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    getContext().sendBroadcast(i);
	    i.setAction("android.settings.SYNC_SETTINGS");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
	}





}  // 
