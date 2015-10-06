package thefinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.neopterix.uploadgallery.R;


/**
 * Created by Neopterix on 24/08/15.
 */
public class VideoPresentationActivity extends Activity {
    SharedPreferences pref ;
    Integer userid=null;
    Integer videoid=null;
    TextView newvideo = null;
    TextView editvideo = null;
    VideoView video = null;
    ImageView logout=null;

    ImageView profilepicture=null;

    ImageView helpicon = null;

    int reviewsnumber=0;


    String date=null;
    String length=null;
    String name=null;
    String description=null;
    String location=null;
    Integer reviews=null;
    String lastedit=null;
    String path=null;

    Integer edited=0;

    TextView controllername=null;
    TextView videodescription=null;
    TextView videodate=null;
    TextView videolength=null;
    TextView videolocation=null;
    TextView videoreviews=null;
    TextView videolastedit=null;


    SQLiteDatabase db;

    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videopresentation);

        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        //pref.getInt("userid", 0);
        //Toast.makeText(VideoPresentationActivity.this, "this is the user id" + pref.getInt("userid", 0), Toast.LENGTH_LONG).show();
        if(mediaControls==null) mediaControls = new MediaController(VideoPresentationActivity.this);
        SetupVariables();




        Intent intent = getIntent();
        //userid= intent.getIntExtra("userid",-1);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        userid=settings.getInt("userid",-2);
        System.out.println("///////////the shared pref value="+userid);
        videoid=intent.getIntExtra("identification",-1);
        date=intent.getStringExtra("date");
        length=intent.getStringExtra("length");

        System.out.println("this is my searched userid value = "+userid);


        db = openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM user where id="+userid, null);
        if(c.moveToNext())
        {
            controllername.setText(c.getString(3));
            if(c.getString(5)!=null)
            {
                profilepicture.setImageBitmap(BitmapFactory.decodeFile(c.getString(5)));
            }
        }
        c.close();


        Cursor c1 = db.rawQuery("SELECT * FROM video where id="+videoid, null);
        if(c1.moveToNext())
        {
            videodescription.setText(c1.getString(12));
            videodate.setText(date);
            videolength.setText(length);
            videolocation.setText(c1.getString(9));
            //////////////////////
            reviewsnumber=c1.getInt(13);
            //////////////////////
            videoreviews.setText("" + c1.getInt(13));
            edited=c1.getInt(7);
            if(edited==0)
            {
                videoreviews.setText("Not reviewed");
                videolastedit.setText("Not edited");
            } else {
                if (c1.getInt(14) < 10 && c1.getInt(15) >= 10) {
                    videolastedit.setText("0" + c1.getInt(14) + " - " + c1.getInt(15) + " - " + c1.getInt(16));
                } else if (c1.getInt(14) >= 10 && c1.getInt(15) < 10) {
                    videolastedit.setText(c1.getInt(14) + " - 0" + c1.getInt(15) + " - " + c1.getInt(16));
                } else if (c1.getInt(14) < 10 && c1.getInt(15) < 10) {
                    videolastedit.setText("0" + c1.getInt(14) + " - 0" + c1.getInt(15) + " - " + c1.getInt(16));
                } else {
                    videolastedit.setText(c1.getInt(14) + " - " + c1.getInt(15) + " - " + c1.getInt(16));
                }
            }
       path=c1.getString(17);
        }
        c1.close();







        progressDialog = new ProgressDialog(VideoPresentationActivity.this);
        progressDialog.setTitle("Thales Spec Application");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try{
            video.setMediaController(mediaControls);
            video.setVideoPath(path);
        } catch(Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        video.requestFocus();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                video.seekTo(position);
                if(position==0){
                    video.start();
                } else{
                    video.pause();
                }
            }
        });



        //video.start();


        newvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newvid = new Intent(VideoPresentationActivity.this, NewVideoActivity.class);
                newvid.putExtra("userid", userid);
                startActivity(newvid);
            }
        });

        editvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editvid = new Intent(VideoPresentationActivity.this,VideoActivity2.class);
                editvid.putExtra("userid",userid);
                editvid.putExtra("videoid",videoid);
                editvid.putExtra("path",path);
                editvid.putExtra("reviewsnumber", reviewsnumber);
                startActivity(editvid);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loggingout = new Intent(VideoPresentationActivity.this,MainActivity.class);
                loggingout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loggingout);

            }
        });

        helpicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoPresentationActivity.this);
                final View recipientLayout = getLayoutInflater().inflate(R.layout.helpdialoglayout,null);
                final TextView recipientTextView = (TextView) recipientLayout.findViewById(R.id.profilehelptext);
                /*recipientTextView.setText("This is your video presentation page :\nYou can perform multiple actions here :\n\n  1.Creating a new video:\nIn order to create a new video, simply click " +
                        "on the NEW VIDEO button.\n\n  2.Edit your video by adding comments over it:\nIf you would like to edit your video, simply click on the EDIT VIDEO button.\n\n  " +
                        "This video presentation page allows you to visualize your selected video. It also shows you some elementary information of it such as video description, length, location, number of reviews and the date of the last edit.\n\n");
                        */
                recipientTextView.setText(getResources().getString(R.string.activity_videopresentation_helptext));
                builder.setView(recipientLayout);
                builder.setTitle("Help");
                //builder.setMessage("Would you like to Exit?");
                //builder.setCancelable(false);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                /*
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
                */
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }


    private void SetupVariables()
    {
        newvideo = (TextView)findViewById(R.id.newvideo);
        editvideo = (TextView) findViewById(R.id.editvideo);
        video = (VideoView) findViewById(R.id.videoView);

        controllername=(TextView)findViewById(R.id.videocontrollernametext);
        videodescription=(TextView)findViewById(R.id.videodescriptiontext);
        videodate=(TextView)findViewById(R.id.videodatetext);
        videolength=(TextView)findViewById(R.id.videolengthtextvalue);
        videolocation=(TextView)findViewById(R.id.videolocationtextvalue);
        videoreviews=(TextView)findViewById(R.id.videoreviewstextvalue);
        videolastedit=(TextView)findViewById(R.id.videolastedittextvalue);

        logout = (ImageView)findViewById(R.id.threelinesprofile2);

        profilepicture = (ImageView) findViewById(R.id.presentationactualpicture);

        helpicon = (ImageView) findViewById(R.id.searchimageprofile2);
    }
}

