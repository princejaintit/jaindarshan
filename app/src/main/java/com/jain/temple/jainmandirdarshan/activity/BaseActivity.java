package com.jain.temple.jainmandirdarshan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.helper.DialogBoxHelper;

import java.io.File;

public class BaseActivity extends AppCompatActivity {
	private DialogBoxHelper dialogBoxHelper;
	
	/*@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
	       
		 
        setSupportActionBar(mToolbar);
	}
	*/
	/**
	 * This function is used to show an alert to the user.
	 * 
	 * @param context
	 *            - Context of the activity in which dialog is to be shown
	 * @param title
	 *            - Title to be displayed in the alert dialog
	 * @param message
	 *            - Message to be shown in the alter dialog
	 * @param isCancelable
	 * @param view
	 *            - view to be passed if the focus is required on dismiss of the
	 *            alert. (e.g., setting cursor/focus in EditText)
	 * @param isFinished
	 *            - Whether the calling Activity needs to be finished on
	 *            dismissal of the dialog?
	 */
	public void showMessage(final Context context, final String title,
                            final String message, final boolean isCancelable, final View view,
                            final boolean isFinished) {
		final Builder builder = new Builder(context);
		final AlertDialog alertDialog = builder.create();
		if (!title.equals("")) {
			alertDialog.setTitle(title);
		}
		alertDialog.setMessage(message);
		alertDialog.setCancelable(isCancelable);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(R.string.ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (view != null) {
							final Animation shake = AnimationUtils
									.loadAnimation(context, R.anim.shake);
							view.requestFocus();
							view.startAnimation(shake);
						}
					}
				});

		alertDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (isFinished) {
					((Activity) context).finish();
				}
			}
		});
		alertDialog.show();
	}
	
	
	/**
	 * To start the activity
	 * @param activity 
	 * 
	 * @param className
	 *            Activity class to be start
	 */
	
	public void startActivityCustom(Activity activity, Class className) {
		startActivity(new Intent(activity, className));
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
	}*/


	public boolean isFilePresent(File fileDir, String fileName) {
		String path = fileDir.getAbsolutePath() + "/" + fileName;
		File file = new File(path);
		boolean isExit = file.exists();
		return isExit;
	}


	public  void openAppInPlayStore(Context paramContext) {
       /* paramContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/")));*/
		Uri marketUri = Uri.parse("market://details?id=" + paramContext.getPackageName());
		Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
		startActivity(marketIntent);
	}



	public  void openMyOtherApp() {
       /* paramContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/")));*/
		Uri marketUri = Uri.parse("https://play.google.com/store/apps/developer?id=Prince+Jain");
		Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
		startActivity(marketIntent);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				break;
		}

		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_out_from_left, R.anim.slide_in_to_left);
	}

}
