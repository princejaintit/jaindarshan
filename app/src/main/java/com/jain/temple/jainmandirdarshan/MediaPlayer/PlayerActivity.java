package com.jain.temple.jainmandirdarshan.MediaPlayer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.activity.BaseActivity;
import com.jain.temple.jainmandirdarshan.helper.CustomProgressBar;
import com.jain.temple.jainmandirdarshan.helper.InputStreamVolleyRequest;
import com.jain.temple.jainmandirdarshan.model.AudioModel;
import com.jain.temple.jainmandirdarshan.roomORM.database.AppDatabase;
import com.jain.temple.jainmandirdarshan.roomORM.entity.MyFavourateTempleEntity;
import com.jain.temple.jainmandirdarshan.util.AppController;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.jain.temple.jainmandirdarshan.util.AppConfig.AUDIO_CAT;

/**
 * Created by admin on 8/29/2017.
 */

public class PlayerActivity extends BaseActivity implements MediaPlayer.OnPreparedListener {

    private final String TAG = "PlayerActivity";
    private ImageView media_play, media_pause;
    private MediaPlayer mMediaPlayer;
    public TextView sogn_rem_dura_tv, total_time_tv, detail_tv;
    private Context context;
    private long timeElapsed = 0, finalTime = 0;

    private int forwardTime = 2000, backwardTime = 2000;
    public final int NOTIFICATION_ID = 412;
    private Handler durationHandler = new Handler();
    //  public  NotificationManagerCompat mNotificationManager;

    private SeekBar seekbar;
    private ImageView iamge_default;

    private StorageReference mStorageRef;
    private CustomProgressBar customProgressBar;
    private String fileUrl;
    private AudioModel audioModel;
    // private DialogBoxHelper dialogBoxHelper;
    private ProgressDialog progressDialog;
    private File mydir;
    private String fileName;
    private boolean isFavorite;
    private Menu menu;
    private  boolean fromDownload;
    private AppDatabase appDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        context = getApplicationContext();


        initView();

    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.sets
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar()/*.setDisplayHomeAsUpEnabled(true)*/;
        actionBar.setDisplayHomeAsUpEnabled(true);

        appDatabase= AppDatabase.getAppDatabase(this);

        media_play = findViewById(R.id.media_play);
        media_pause = findViewById(R.id.media_pause);
        sogn_rem_dura_tv = (TextView) findViewById(R.id.sogn_rem_dura_tv);
        detail_tv = findViewById(R.id.detail_tv);
        total_time_tv = findViewById(R.id.total_time_tv);
        seekbar = (SeekBar) findViewById(R.id.seekBar);

        //  mediaPlayer = MediaPlayer.create(this, R.raw.stotra);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        customProgressBar = new CustomProgressBar();
        iamge_default = findViewById(R.id.iamge_default);


        showInterstitial();



        Intent intent = getIntent();
        audioModel = intent.getParcelableExtra("audioModel");

        fromDownload = intent.getBooleanExtra("fromDownload", false);

        if (fromDownload) {
            try {
                customProgressBar.show(this, "Loading..");
                mMediaPlayer.setDataSource(audioModel.getUrl_refrence());
                mMediaPlayer.setOnPreparedListener(PlayerActivity.this);

                mMediaPlayer.prepare();

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {


            UiUtils uiUtils = new UiUtils();
            if (uiUtils.isNetworkAvailable(this)) {

                customProgressBar.show(this, "Loading..");
            } else {
                showMessage(this, getString(R.string.app_name), getString(R.string.network_not_available), true, media_play, true);
                // Toast.makeText(this,getString(R.string.network_not_available), Toast.LENGTH_LONG).show();
            }

            mStorageRef = FirebaseStorage.getInstance().getReference();


            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference storageRef = storage.getReference();
            StorageReference songsRef = storageRef.child(audioModel.getCategory() + "/" + audioModel.getUrl_refrence());

            fetchAudioUrlFromFirebase(songsRef);
        }


        //actionBar.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_volume));
        actionBar.setTitle(audioModel.getTitle_h());
        String detail = audioModel.getDetail().trim();
        if (detail.equalsIgnoreCase("")) {
            iamge_default.setVisibility(View.VISIBLE);
            detail_tv.setVisibility(View.GONE);
        } else {
            iamge_default.setVisibility(View.GONE);
            detail_tv.setVisibility(View.VISIBLE);
            detail_tv.setText(Html.fromHtml(detail));
        }


    }


    // play mp3 song
    public void play(View view) {
        play();
    }

    private void play() {


        mMediaPlayer.start();
        timeElapsed = mMediaPlayer.getCurrentPosition();
        seekbar.setProgress((int) timeElapsed);
        durationHandler.postDelayed(updateSeekBarTime, 100);

        media_play.setVisibility(View.GONE);
        media_pause.setVisibility(View.VISIBLE);
    }

    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            if (mMediaPlayer != null) {
                //get current position
                timeElapsed = mMediaPlayer.getCurrentPosition();
                //set seekbar progress
                seekbar.setProgress((int) timeElapsed);
                //set time remaing
                double timeRemaining = timeElapsed;

                sogn_rem_dura_tv.setText(String.format("%02d:%02d ", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
                //repeat yourself that again in 100 miliseconds
                durationHandler.postDelayed(this, 100);
            }
        }
    };

    // pause mp3 song
    public void pause(View view) {
        pause();
    }

    private void pause() {
        mMediaPlayer.pause();

        media_play.setVisibility(View.VISIBLE);
        media_pause.setVisibility(View.GONE);
    }

    // go forward at forwardTime seconds
    public void forward(View view) {
        //check if we can go forward at forwardTime seconds before song endes
        if ((timeElapsed + forwardTime) <= finalTime) {
            timeElapsed = timeElapsed + forwardTime;

            //seek to the exact second of the track
            mMediaPlayer.seekTo((int) timeElapsed);
        }
    }

    // go backwards at backwardTime seconds
    public void rewind(View view) {
        //check if we can go back at backwardTime seconds after song starts
        if ((timeElapsed - backwardTime) > 0) {
            timeElapsed = timeElapsed - backwardTime;

            //seek to the exact second of the track
            mMediaPlayer.seekTo((int) timeElapsed);
        }
    }


    @Override
    public void onPrepared(final MediaPlayer mediaPlayer) {

        finalTime = mediaPlayer.getDuration();


        seekbar.setMax((int) finalTime);
        seekbar.setClickable(false);


        timeElapsed = mediaPlayer.getCurrentPosition();
        //set seekbar progress
        seekbar.setProgress((int) timeElapsed);
        //set time remaing
        double timeRemaining = finalTime - timeElapsed;

        total_time_tv.setText(String.format("%02d:%02d ", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
        //repeat yourself that again in 100 miliseconds


        //        mNotificationManager = NotificationManagerCompat.from(this);*/

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Tranverse Change:", Integer.toString(progress));

                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
                mMediaPlayer.release();
                mMediaPlayer = null;
                finish();
            }
        });

        play();

        customProgressBar.getDialog().dismiss();




    }


    private void fetchAudioUrlFromFirebase(StorageReference storageRefUrl) {


        storageRefUrl.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {

                    // Download url of file
                    fileUrl = uri.toString();
                    mMediaPlayer.setDataSource(fileUrl);

                    mMediaPlayer.setOnPreparedListener(PlayerActivity.this);

                    mMediaPlayer.prepare();


                    //  mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("TAG", e.getMessage());
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.player_menu, menu);//Menu Resource, Menu
        if (fromDownload){
            menu.findItem(R.id.action_wishList).setVisible(false);
        }
        getFavourite();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_download:
                AlertDialog alertDialog = createDownloadDialog(getString(R.string.download_dialog), getString(R.string.app_name));
                alertDialog.show();
                break;
            case R.id.action_wishList:
                if (isFavorite) {
                    deleteFavorite();
                } else {
                    saveFavourite();
                }

                break;

        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteFavorite() {
      //  DataBaseHelper db = new DataBaseHelper(this);
        appDatabase.favouriteDao().deleteFavoriteAudio(audioModel.getTitle_h());

     //   db.deleteFavoriteAudio(audioModel.getTitle_h());

        isFavorite = false;
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
    }

    private void saveFavourite() {
        MyFavourateTempleEntity myFavourateTempleEntity=new MyFavourateTempleEntity();
        myFavourateTempleEntity.setTITLE(audioModel.getTitle_h());
        myFavourateTempleEntity.setDESCRIPTION(audioModel.getDetail());
        myFavourateTempleEntity.setCATEGORY(AUDIO_CAT);
        myFavourateTempleEntity.setCONTENT_CATEGORY(audioModel.getCategory());
        myFavourateTempleEntity.setURL_REFRENCE(audioModel.getUrl_refrence());


        appDatabase.favouriteDao().AddFavouriteAudio(myFavourateTempleEntity);
    //    DataBaseHelper db = new DataBaseHelper(this);
     //   db.AddFavouriteAudio(audioModel);

        isFavorite = true;
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp));
    }

    public void getFavourite() {
       MyFavourateTempleEntity myFavourateTempleEntity= appDatabase.favouriteDao().isFavouriteAudio(audioModel.getTitle_h());

        if (myFavourateTempleEntity!=null) {
            isFavorite = true;
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp));
        } else {
            isFavorite = false;
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
        }
     /*   DataBaseHelper db = new DataBaseHelper(this);
        // FavouriteModel favouriteModel = db.getFavourite(nearPlaceLatLngModel.getPlaceId());
        boolean isFavourite = db.isFavouriteAudio(audioModel.getTitle_h());

        if (isFavourite) {
            isFavorite = true;
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp));
        } else {
            isFavorite = false;
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
        }*/
    }


    private void downloadFile() {

        fileName = audioModel.getTitle_h().trim() + ".mp3";

        mydir = context.getDir("audio", Context.MODE_PRIVATE); //Creating an internal dir;

        if (mydir.exists()) {

        } else {
            mydir.mkdir();
        }


        if (!isFilePresent(mydir, fileName)) {
            UiUtils uiUtils = new UiUtils();
            if (uiUtils.isNetworkAvailable(this)) {
                progressDialog = new ProgressDialog(PlayerActivity.this);
                progressDialog.setTitle(getString(R.string.app_name));
                progressDialog.setMessage("Downloading in Progress...");
                progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.setMax(100);
                progressDialog.setProgress(0);
                progressDialog.show();

                makeDowloadFileRequest();
            } else {
                uiUtils.showAlertDialog(this, getString(R.string.network_not_available), getString(R.string.app_name));
            }
        }
    }

    private void makeDowloadFileRequest() {

        customProgressBar.show(this, "Loading..");

        HashMap<String, String> params = new HashMap<>();
      /*  dialogBoxHelper = new DialogBoxHelper(this);
        dialogBoxHelper.setTitle(getString(R.string.downloading_audio));
        dialogBoxHelper.showProgressDialog();*/
        AppController.getInstance().addToRequestQueue(new InputStreamVolleyRequest(Request.Method.GET,
                fileUrl, dataResponse, errorListener, params), "tag_get_APP_id_req");
    }

    private Response.Listener<byte[]> dataResponse = new Response.Listener<byte[]>() {
        @Override
        public void onResponse(byte[] response) {

            customProgressBar.getDialog().dismiss();

            Log.d("responsedddddddddd", response.toString());
            int count;
            long progress;

            if (response != null) {

                try {
                    long lenghtOfFile = response.length;

                    InputStream input = new ByteArrayInputStream(response);
                    File fileWithinMyDir = new File(mydir, fileName);

                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(fileWithinMyDir));
                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);
                        progress = total * 100 / lenghtOfFile;
                        Log.e("download", progress + "");
                        progressDialog.setProgress((int) progress);
                    }

                    output.flush();

                    output.close();
                    input.close();
                    Toast.makeText(PlayerActivity.this, "Download complete.", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();

                }

                    /*    File fileWithinMyDir = new File(mydir, name);
                        outputStream = new FileOutputStream(fileWithinMyDir);
                        outputStream.write(response);
                        outputStream.close();
                        Toast.makeText(PlayerActivity.this, "Download complete.", Toast.LENGTH_LONG).show();*/
            }


            //    dialogBoxHelper.hideProgressDialog();

            progressDialog.dismiss();
        }

    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("response", error.toString());

            if (error.networkResponse != null) {

                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);

                    Toast.makeText(PlayerActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    //Handle a malformed json response
                } catch (UnsupportedEncodingException e) {

                }
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // mNotificationManager.cancel(NOTIFICATION_ID);



        if(mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
            finish();
        }

    }

    private void showInterstitial() {

    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private AlertDialog createDownloadDialog(String message, String title) {
        AlertDialog dialog = new AlertDialog.Builder(this).setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {

                        UiUtils uiUtils = new UiUtils();
                        boolean isExit = uiUtils.checkFileInDirectory(PlayerActivity.this, audioModel.getTitle_h()+ ".mp3");
                        if (isExit) {
                            Toast.makeText(PlayerActivity.this, getString(R.string.already_downlad_file), Toast.LENGTH_SHORT).show();
                        } else {
                            downloadFile();
                        }


                    }
                })/*.setNegativeButton(getString(R.string.dialog_your_feedback), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                openFeedback(MainActivity.this);

            }
        })*/.setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
            }
        }).setMessage(message).setTitle(title).create();
        return dialog;
    }


}
