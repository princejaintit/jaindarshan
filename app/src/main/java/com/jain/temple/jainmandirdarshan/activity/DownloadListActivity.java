package com.jain.temple.jainmandirdarshan.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Interface.FragmentDrawerListener;
import com.jain.temple.jainmandirdarshan.MediaPlayer.PlayerActivity;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.adaptor.DownloadAdaptor;
import com.jain.temple.jainmandirdarshan.model.AudioModel;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by admin on 9/14/2017.
 */

public class DownloadListActivity extends BaseActivity implements FragmentDrawerListener {
    private RecyclerView recy_view;

    private ArrayList<AudioModel> audioModelArrayList;
    private LinearLayout no_record_layout;
    private TextView search_tv;
    private  DownloadAdaptor downloadAdaptor;
    private UiUtils uiUtils;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_download);

        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.sets
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar()/*.setDisplayHomeAsUpEnabled(true)*/;
        actionBar.setDisplayHomeAsUpEnabled(true);

        recy_view = (RecyclerView) findViewById(R.id.recy_view);
        no_record_layout = findViewById(R.id.no_record_layout);
        search_tv = findViewById(R.id.search_tv);


        recy_view.setLayoutManager(new LinearLayoutManager(this));

        audioModelArrayList = new ArrayList<>();
         downloadAdaptor = new DownloadAdaptor(audioModelArrayList, this);
        recy_view.setAdapter(downloadAdaptor);

         uiUtils = new UiUtils();

        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DownloadListActivity.this, AudioListActivity.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onResume() {
        super.onResume();


        getDownloadListData();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        AudioModel audioModel = audioModelArrayList.get(position);
        switch (view.getId()) {
            case R.id.play_iv:
                Intent intent = new Intent(this, PlayerActivity.class);
                intent.putExtra("audioModel", audioModel);
                intent.putExtra("fromDownload", true);
                startActivity(intent);
                break;
            case R.id.delete_tv:
                AlertDialog alertDialog=  createDeleteDialog(audioModel,position,getString(R.string.app_name),getString(R.string.delete_msg));
                alertDialog.show();
                break;
        }

    }

    private AlertDialog createDeleteDialog(final AudioModel audioModel, final int position, String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(this).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                File dir = getDir("audio", Context.MODE_PRIVATE);
                File file = new File(dir,audioModel.getTitle_h()+".mp3");
                boolean deleted = file.delete();
                if (true) {
                    audioModelArrayList.remove(position);
                    downloadAdaptor.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(DownloadListActivity.this, "There is some problem ! please try again", Toast.LENGTH_SHORT).show();
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

    public void getDownloadListData() {
        audioModelArrayList.clear();
        audioModelArrayList = uiUtils.getDownloadListData(this, audioModelArrayList);

        downloadAdaptor.notifyDataSetChanged();

        if (audioModelArrayList.size() > 0) {
            no_record_layout.setVisibility(View.GONE);
            recy_view.setVisibility(View.VISIBLE);
        } else {
            no_record_layout.setVisibility(View.VISIBLE);
            recy_view.setVisibility(View.GONE);
        }

    }
}
