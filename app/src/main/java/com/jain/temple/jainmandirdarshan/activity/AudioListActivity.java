package com.jain.temple.jainmandirdarshan.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jain.temple.jainmandirdarshan.Fragment.FragmentDrawer;
import com.jain.temple.jainmandirdarshan.Interface.AdaptorClickInterface;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.adaptor.AudioListAdaptor;
import com.jain.temple.jainmandirdarshan.helper.CustomProgressBar;
import com.jain.temple.jainmandirdarshan.helper.DialogBoxHelper;
import com.jain.temple.jainmandirdarshan.helper.InputStreamVolleyRequest;
import com.jain.temple.jainmandirdarshan.model.AudioMainModel;
import com.jain.temple.jainmandirdarshan.model.AudioModel;
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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 9/6/2017.
 */

public class AudioListActivity extends BaseActivity implements AdaptorClickInterface {

  // private DatabaseReference mDatabase;
 private  DialogBoxHelper dialogBoxHelper;
    private RecyclerView recy_view;
    public ArrayList<AudioMainModel>  mainModelArrayList;
    private AudioListAdaptor audioListAdaptor;
    private int position;

    private boolean strotraBoo,stutiBoo,bhaktiBoo,chalisaBoo,artiBoo,bhajanBoo;
    private AudioModel audioModel;
    private  ProgressDialog  progressDialog;
    private File mydir;
    private String fileName;
    private String fileUrl;
    private CustomProgressBar customProgressBar;
    private ActionBar actionBar;
    private int audioCount=0;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_audio_list);

        initView();
    }

    private void initView() {
        dialogBoxHelper =new DialogBoxHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.sets
        setSupportActionBar(toolbar);
         actionBar= getSupportActionBar()/*.setDisplayHomeAsUpEnabled(true)*/;
        actionBar.setDisplayHomeAsUpEnabled(true);

        recy_view= (RecyclerView) findViewById(R.id.recy_view);



        recy_view.setLayoutManager(new LinearLayoutManager(this));
        mainModelArrayList=new ArrayList<>();
        audioListAdaptor=new AudioListAdaptor(this,mainModelArrayList,this);
        recy_view.setAdapter(audioListAdaptor);

        UiUtils uiUtils=new UiUtils();
        if (uiUtils.isNetworkAvailable(this)){

            dialogBoxHelper.showProgressDialog();
        }

        else {
            showMessage(this,getString(R.string.app_name),getString(R.string.network_not_available),true,recy_view,true);
        }


        DatabaseReference mDatabase =  FirebaseDatabase.getInstance().getReference();

        DatabaseReference databaseReference= mDatabase.child("strotra");
        final AudioMainModel audioMainModelStrotra=new AudioMainModel();
        audioMainModelStrotra.setTitle(getString(R.string.strotra));
        ArrayList<AudioModel> modelArrayList=new ArrayList<>();
        final ArrayList<AudioModel> finalModelArrayList = modelArrayList;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                  Log.d("ttttttt" , noteSnapshot.getValue().toString());
                    AudioModel audioModel= noteSnapshot.getValue(AudioModel.class);

                    audioCount++;

                    finalModelArrayList.add(audioModel);

                }

                audioMainModelStrotra.setAudioModelArrayList(finalModelArrayList);

                mainModelArrayList.add(audioMainModelStrotra);

                strotraBoo=true;
                chechFulldata();
              //  audioListAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ttttttt", databaseError.getMessage());

                dialogBoxHelper.hideProgressDialog();
            }
        });

        DatabaseReference databaseReferenceChalisa= mDatabase.child("chalisa");
        final AudioMainModel audioMainModelchalisa=new AudioMainModel();
        audioMainModelchalisa.setTitle(getString(R.string.chalisa));
        final ArrayList<AudioModel>  modelArrayListChalisa=new ArrayList<>();
        databaseReferenceChalisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    Log.d("ttttttt" , noteSnapshot.getValue().toString());
                    AudioModel audioModel= noteSnapshot.getValue(AudioModel.class);
                    audioCount++;
                    modelArrayListChalisa.add(audioModel);
                }

                audioMainModelchalisa.setAudioModelArrayList(modelArrayListChalisa);

                mainModelArrayList.add(audioMainModelchalisa);

                chalisaBoo=true;
                chechFulldata();

           //     audioListAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ttttttt", databaseError.getMessage());

                dialogBoxHelper.hideProgressDialog();
            }
        });


        DatabaseReference databaseReferenceBhakti= mDatabase.child("bhakti");
        final AudioMainModel audioMainModelBhakti=new AudioMainModel();
        audioMainModelBhakti.setTitle(getString(R.string.bhavna));
        final ArrayList<AudioModel> modelArrayListBhakti=new ArrayList<>();
        databaseReferenceBhakti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    Log.d("ttttttt" , noteSnapshot.getValue().toString());
                    audioCount++;
                    AudioModel audioModel= noteSnapshot.getValue(AudioModel.class);

                    modelArrayListBhakti.add(audioModel);

                }

                audioMainModelBhakti.setAudioModelArrayList(modelArrayListBhakti);

                mainModelArrayList.add(audioMainModelBhakti);
                bhaktiBoo=true;
                chechFulldata();
              //  audioListAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ttttttt", databaseError.getMessage());

               dialogBoxHelper.hideProgressDialog();
            }
        });


        DatabaseReference databaseReferenceStuti= mDatabase.child("stuti");
        final AudioMainModel audioMainModelStuti=new AudioMainModel();
        audioMainModelStuti.setTitle(getString(R.string.stuti));
        final ArrayList<AudioModel> modelArrayListStuti=new ArrayList<>();
        databaseReferenceStuti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    Log.d("ttttttt" , noteSnapshot.getValue().toString());
                    AudioModel audioModel= noteSnapshot.getValue(AudioModel.class);
                    audioCount++;
                    modelArrayListStuti.add(audioModel);

                }

                audioMainModelStuti.setAudioModelArrayList(modelArrayListStuti);

                mainModelArrayList.add(audioMainModelStuti);

                stutiBoo=true;
                chechFulldata();

               // audioListAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ttttttt", databaseError.getMessage());

               dialogBoxHelper.hideProgressDialog();
            }
        });



        DatabaseReference databaseReferenceBhajan= mDatabase.child("bhajan");
        final AudioMainModel audioMainModelBhajan=new AudioMainModel();
        audioMainModelBhajan.setTitle(getString(R.string.bhajan));
        final ArrayList<AudioModel> modelArrayListBhajan=new ArrayList<>();
        databaseReferenceBhajan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    Log.d("ttttttt" , noteSnapshot.getValue().toString());
                    AudioModel audioModel= noteSnapshot.getValue(AudioModel.class);
                    audioCount++;
                    modelArrayListBhajan.add(audioModel);

                }

                audioMainModelBhajan.setAudioModelArrayList(modelArrayListBhajan);

                mainModelArrayList.add(audioMainModelBhajan);

                bhajanBoo=true;
                chechFulldata();

                // audioListAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ttttttt", databaseError.getMessage());

                dialogBoxHelper.hideProgressDialog();
            }
        });

        DatabaseReference databaseReferenceAarti= mDatabase.child("aarti");
        final AudioMainModel audioMainModelAarti=new AudioMainModel();
        audioMainModelAarti.setTitle(getString(R.string.aarti));
        final ArrayList<AudioModel>  modelArrayListAarti=new ArrayList<>();
        databaseReferenceAarti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    Log.d("ttttttt" , noteSnapshot.getValue().toString());
                    AudioModel audioModel= noteSnapshot.getValue(AudioModel.class);
                    audioCount++;
                    modelArrayListAarti.add(audioModel);

                }

                audioMainModelAarti.setAudioModelArrayList(modelArrayListAarti);

                mainModelArrayList.add(audioMainModelAarti);

                artiBoo=true;
                chechFulldata();
               /* audioListAdaptor.notifyDataSetChanged();

                dialogBoxHelper.hideProgressDialog();*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ttttttt", databaseError.getMessage());

               dialogBoxHelper.hideProgressDialog();
            }
        });

        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                }
            }
        });


        recy_view.addOnItemTouchListener(new FragmentDrawer.RecyclerTouchListener(
                this, recy_view, new FragmentDrawer.ClickListener() {
            @Override
            public void onClick(View view, int position) {
               /* AudioModel audioModel= modelArrayList.get(position);
                startActivity(new Intent(AudioListActivity.this, PlayerActivity.class).putExtra("audioModel",audioModel));*/

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void chechFulldata() {

        if ( strotraBoo && stutiBoo && bhaktiBoo && chalisaBoo && artiBoo && bhajanBoo) {
            actionBar.setTitle(getString(R.string.audio)+"  ("+audioCount+")");

            audioListAdaptor.notifyDataSetChanged();

            dialogBoxHelper.hideProgressDialog();
        }
    }


    private AlertDialog createDownloadDialog(String message, String title) {
        AlertDialog dialog = new AlertDialog.Builder(this).setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {

                        UiUtils uiUtils = new UiUtils();
                        boolean isExit = uiUtils.checkFileInDirectory(AudioListActivity.this,audioModel.getTitle_h()+".mp3");
                        if (isExit) {
                            Toast.makeText(AudioListActivity.this,getString(R.string.already_downlad_file), Toast.LENGTH_SHORT).show();
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



    private void downloadFile() {


        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();
        StorageReference songsRef = storageRef.child(audioModel.getCategory() + "/" + audioModel.getUrl_refrence());

        fetchAudioUrlFromFirebase(songsRef);


    }



    private void makeDowloadFileRequest() {


        customProgressBar = new CustomProgressBar();

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

                try{
                    long lenghtOfFile = response.length;

                    InputStream input = new ByteArrayInputStream(response);
                    File fileWithinMyDir = new File(mydir, fileName);

                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(fileWithinMyDir));
                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);
                        progress =total*100/lenghtOfFile;
                        Log.e("download",progress+"");
                        progressDialog.setProgress((int) progress);
                    }

                    output.flush();

                    output.close();
                    input.close();
                    Toast.makeText(AudioListActivity.this, "Download complete.", Toast.LENGTH_LONG).show();

                }catch(IOException e){
                    e.printStackTrace();

                }

                    /*    File fileWithinMyDir = new File(mydir, name);
                        outputStream = new FileOutputStream(fileWithinMyDir);
                        outputStream.write(response);
                        outputStream.close();
                       */
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

                    Toast.makeText(AudioListActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    //Handle a malformed json response
                } catch (UnsupportedEncodingException e) {

                }
            }
        }
    };


    private void fetchAudioUrlFromFirebase(StorageReference storageRefUrl) {


        storageRefUrl.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                    // Download url of file
                    fileUrl = uri.toString();


                fileName = audioModel.getTitle_h().trim() + ".mp3";

                mydir = getDir("audio", Context.MODE_PRIVATE); //Creating an internal dir;

                if (mydir.exists()) {

                } else {
                    mydir.mkdir();
                }


                if (!isFilePresent(mydir, fileName)) {
                    UiUtils uiUtils = new UiUtils();
                    if (uiUtils.isNetworkAvailable(AudioListActivity.this)) {
                        progressDialog = new ProgressDialog(AudioListActivity.this);
                        progressDialog.setTitle(getString(R.string.app_name));
                        progressDialog.setMessage("Downloading in Progress...");
                        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
                        progressDialog.setCancelable(false);
                        progressDialog.setMax(100);
                        progressDialog.setProgress(0);
                        progressDialog.show();

                        makeDowloadFileRequest();
                    } else {
                        uiUtils.showAlertDialog(AudioListActivity.this, getString(R.string.network_not_available), getString(R.string.app_name));
                    }
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
    public void getitemClickPosition(Object o) {
         audioModel= (AudioModel) o;




        AlertDialog alertDialog=   createDownloadDialog(getString(R.string.download_dialog),getString(R.string.app_name));
        alertDialog.show();

    }
}
