package thefinal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.example.neopterix.uploadgallery.R;
import com.serenegiant.service.ScreenRecorderService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

/**
 * Created by Neopterix on 23/07/15.
 */
public class VideoActivity2 extends Activity {
    private static final String DEBUG_TAG = "GestureFunActivity";


    private static final boolean DEBUG = false;
    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_SCREEN_CAPTURE = 1;

    private ToggleButton mRecordButton;
    private ToggleButton mPauseButton;
    private MyBroadcastReceiver mReceiver;



    private boolean inRecordingMode = false;
    ImageView upbar = null;
    ImageView downbar=null;
    TextView redtext=null;


    List<Integer> checkpoints = new ArrayList<Integer>();

    Context mContext;


    VideoView vid = null;
    TextView starttime = null;
    TextView endtime = null;
    long duration;
    private ViewGroup mRrootLayout;
    ImageButton pause = null;
    ImageButton backward = null;
    ImageButton forward = null;
    ImageButton exit = null;
    ImageButton add = null;


    int commentlength = 2000;

    ImageView helpicon = null;
    ImageView helpicon2 = null;

    RelativeLayout blueguider=null;

    Button increase = null;
    Button decrease = null;
    Button deletecircle=null;

    int reviewsnumber=0;

    boolean alreadyeditedthistime=false;

    int paused = 0;
    SeekBar sb_volume = null;
    SeekBar sb_time = null;
    AudioManager audioManager = null;
    ImageView volumeicon = null;
    int mode = 1;
    int hh;
    int mm;
    int ss;
    int Count=0;
    private int _xDelta;
    private int _yDelta;
    int selected=0;
    int numcircles=0;
    int numcomments=100;

    ArrayList<Circle> circles = new ArrayList<Circle>();
    ArrayList<Comment> comments = new ArrayList<Comment>();


    Integer userid=null;
    Integer videoid=null;
    String path=null;
    SQLiteDatabase db;
    Cursor c=null;
    Cursor c1=null;
    Cursor c2=null;
    Cursor c3=null;
    Cursor c4=null;
    Cursor c5=null;
    Cursor c6=null;
    Cursor c7=null;

    private final Handler handler = new Handler();

    private final Runnable getCommentsPositions = new Runnable() {
        @Override
        public void run() {
            checkpoints=null;
            checkpoints = new ArrayList<Integer>();
            blueguider.removeAllViews();
            System.out.println(" blue guider = "+blueguider.getWidth());
            int bluegiderwidth=blueguider.getWidth();
            int vidduration=vid.getDuration();
            c6 = db.rawQuery("SELECT * FROM circle where videoid="+videoid, null);
            while(c6.moveToNext())
            {
                int x = c6.getInt(1);
                checkpoints.add(x);
                System.out.println("x="+x);
                System.out.println("vid duration="+vid.getDuration());


                int percentage =( x * bluegiderwidth )/ vidduration ;

                System.out.println("percentage="+percentage);
                RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ImageView img = new ImageView(VideoActivity2.this);
                img.setImageResource(R.drawable.marker);
                lparams.leftMargin=lparams.leftMargin+percentage;
                img.setLayoutParams(lparams);

                blueguider.addView(img);

            }
            c6.close();
        }
    };



    private final Runnable onEvrySecond = new Runnable() {
        @Override
        public void run() {
            if(sb_time!=null){
                //************diplay circles and comments
                c = db.rawQuery("SELECT * FROM circle where videoid="+videoid, null);
                while(c.moveToNext())
                {
                    if(vid.getCurrentPosition()>= c.getInt(1) &&  vid.getCurrentPosition()<= c.getInt(2))
                    {
                        int x = c.getInt(0);
                        ImageView v = (ImageView) findViewById(x);
                        if(v!=null)
                        v.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        int x = c.getInt(0);
                        ImageView v = (ImageView) findViewById(x);
                        if(v!=null)
                        v.setVisibility(View.INVISIBLE);
                    }
                }
                c.close();

                c1=db.rawQuery("SELECT * FROM comment where videoid="+videoid,null);
                while(c1.moveToNext())
                {
                    if(vid.getCurrentPosition()>= c1.getInt(1) &&  vid.getCurrentPosition()<= c1.getInt(2))
                    {
                        int x = c1.getInt(0)+1000;
                        EditText v1 = (EditText) findViewById(x);
                        if(v1!=null)
                        v1.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        int x = c1.getInt(0)+1000;
                        EditText v1 = (EditText) findViewById(x);
                        if(v1!=null)
                        v1.setVisibility(View.INVISIBLE);
                    }
                }
                c1.close();



                //***********************************************
                sb_time.setProgress(vid.getCurrentPosition());
                duration = vid.getDuration();
                int x= (int) (duration-vid.getCurrentPosition());
                hh =(int) (vid.getCurrentPosition()/1000)/3600;
                mm =(int) ((vid.getCurrentPosition()/1000)-hh*3600)/60;
                ss =(int) (vid.getCurrentPosition()/1000)-hh*3600-mm*60;
                System.out.print("hh = " + hh + "mm =" + mm + "ss = " + ss);
                if(ss>=10)
                {
                    starttime.setText(mm+":"+ss);
                }
                else starttime.setText(mm+":0"+ss);
                hh =(int) (x/1000)/3600;
                mm =(int) ((x/1000)-hh*3600)/60;
                ss =(int) (x/1000)-hh*3600-mm*60;
                if(ss>=10)
                {
                    endtime.setText(mm+":"+ss);
                }
                else endtime.setText(mm+":0"+ss);

            }

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if(vid.isPlaying()){
                sb_time.postDelayed(onEvrySecond,50);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_video2);
        SetupVariables();


        mRecordButton = (ToggleButton)findViewById(R.id.record_button);
        mRecordButton.setText("Record Sequence");
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            mRecordButton.setVisibility(View.INVISIBLE);
        }
        mPauseButton = (ToggleButton)findViewById(R.id.pause_button);
        mPauseButton.setVisibility(View.INVISIBLE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {


        updateRecording(false, false);
        if (mReceiver == null)
            mReceiver = new MyBroadcastReceiver(this);

        }






        Intent intent = getIntent();
        userid = intent.getIntExtra("userid",-1);
        videoid = intent.getIntExtra("videoid",-1);
        path = intent.getStringExtra("path");
        reviewsnumber = intent.getIntExtra("reviewsnumber",-10);

        System.out.println("These are my values :");
        System.out.println("userid = "+userid);
        System.out.println("videoid = "+videoid);
        System.out.println("path = "+path);

        db = openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE, null);
        Cursor cforcommentlength = db.rawQuery("SELECT * FROM video where id="+videoid, null);
        if(cforcommentlength.moveToNext())
        {
            commentlength = cforcommentlength.getInt(11)*1000;
        }
        cforcommentlength.close();
        RecreateScene();


        //File ext = Environment.getDataDirectory().

        //vid.setVideoPath("/mnt/sdcard/amine/vid.mp4");
        vid.setVideoPath(path);



        vid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                /*duration = vid.getDuration();
                System.out.println("duration========"+duration);*/
                sb_time.setMax(vid.getDuration());
                duration = vid.getDuration();
                hh =(int) (duration/1000)/3600;
                mm =(int) ((duration/1000)-hh*3600)/60;
                ss =(int) (duration/1000)-hh*3600-mm*60;
                System.out.print("hh = "+hh+"mm ="+mm+"ss = "+ss);
                starttime.setText("0:00");
                if(ss>=10)
                {
                    endtime.setText(mm+":"+ss);
                }
                else endtime.setText(mm+":0"+ss);
                System.out.println("////////////this is my duration ===: " + duration);
                sb_time.postDelayed(onEvrySecond, 50);


                blueguider.post(getCommentsPositions);
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastjump = 0;
                int cur = vid.getCurrentPosition();
                float min = vid.getDuration();
                float min2 = min;
                for(int i=0;i<checkpoints.size();i++)
                {
                    if(checkpoints.get(i)<cur)
                    {
                        min2 = cur - checkpoints.get(i);
                        if(min2<min)
                        {
                            min = min2;
                            lastjump=checkpoints.get(i);
                        }
                    }
                }


                if(lastjump!=0)
                vid.seekTo(lastjump);
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int lastjump = vid.getDuration();;
                int cur = vid.getCurrentPosition();
                float min = lastjump;
                float min2 = min;
                for (int i = 0; i < checkpoints.size(); i++) {
                    if (checkpoints.get(i) > cur) {
                        min2 = checkpoints.get(i) - cur;
                        if (min2 < min) {
                            min = min2;
                            lastjump = checkpoints.get(i);
                        }
                    }
                }


                if(lastjump!=vid.getDuration())
                vid.seekTo(lastjump);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity2.this);
                builder.setTitle("Exit");
                builder.setMessage("Would you like to Exit?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(VideoActivity2.this, ProfileActivity.class);
                        intent.putExtra("userid", userid);
                        reviewsnumber++;
                        db.execSQL("UPDATE video SET reviews=" + reviewsnumber + " WHERE id=" + videoid + ";");
                        System.out.println("**********this is the intent am passing : " + userid + "*********");
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("*****Database content for circles and comments*****");
                        c4 = db.rawQuery("SELECT * FROM circle where videoid=" + videoid, null);
                        while (c4.moveToNext()) {
                            System.out.println("circleid=" + c4.getInt(0) + ",appeartime=" + c4.getInt(1) + ",desappeartime=" + c4.getInt(2) + ",leftmargin=" + c4.getInt(3) +
                                    ",rightmargin=" + c4.getInt(4) + ",topmargin=" + c4.getInt(5) + ",bottommargin=" + c4.getInt(6) + ",height=" + c4.getInt(7) + ",width=" + c4.getInt(8) + ",videoid=" + c4.getInt(9));
                        }
                        c4.close();
                        c5=db.rawQuery("SELECT * FROM comment where videoid="+videoid,null);
                        while(c5.moveToNext())
                        {
                            System.out.println("commentid=" + c5.getInt(0) + ",appeartime=" + c5.getInt(1) + ",desappeartime=" + c5.getInt(2) + ",text="+c5.getString(3)+",leftmargin=" + c5.getInt(4) +
                                    ",rightmargin=" + c5.getInt(5) + ",topmargin=" + c5.getInt(6) + ",bottommargin=" + c5.getInt(7) +  ",videoid=" + c5.getInt(8));
                        }
                        c5.close();
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!alreadyeditedthistime) {
                    Calendar c = Calendar.getInstance();
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH) + 1;
                    int year = c.get(Calendar.YEAR);
                    db.execSQL("UPDATE video SET edited=" + 1 + ",lasteditday=" + day + ",lasteditmonth=" + month + ",lastedityear=" + year + " WHERE id=" + videoid + ";");
                    alreadyeditedthistime=true;
                }
                addViews(v);
            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (selected > 0) {
                    ImageView tmp = (ImageView) findViewById(selected);
                    int newwidth = tmp.getWidth() + 5;
                    int newheight = tmp.getHeight() + 5;
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tmp.getLayoutParams();
                    RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(newwidth, newheight);
                    layoutParams1.leftMargin = layoutParams.leftMargin;
                    layoutParams1.topMargin = layoutParams.topMargin;
                    layoutParams1.bottomMargin = layoutParams.bottomMargin;
                    layoutParams1.rightMargin = layoutParams.rightMargin;
                    tmp.setLayoutParams(layoutParams1);
                    System.out.println("Voilà les nouvelles valeurs de la cercle sélectionnée : ");

                    db.execSQL("UPDATE circle SET leftmargin=" + layoutParams1.leftMargin + ",rightmargin=" + layoutParams1.rightMargin + ",topmargin=" + layoutParams1.topMargin + ",bottommargin=" +
                            layoutParams1.bottomMargin + ",height=" + newheight + ",width=" + newwidth + " WHERE id=" + selected + ";");

                    if(!alreadyeditedthistime) {
                        Calendar c = Calendar.getInstance();
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int month = c.get(Calendar.MONTH) + 1;
                        int year = c.get(Calendar.YEAR);
                        db.execSQL("UPDATE video SET edited=" + 1 + ",lasteditday=" + day + ",lasteditmonth=" + month + ",lastedityear=" + year + " WHERE id=" + videoid + ";");
                        alreadyeditedthistime=true;
                    }



                }
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selected > 0) {
                    ImageView tmp = (ImageView) findViewById(selected);
                    int newwidth = tmp.getWidth() - 5;
                    int newheight = tmp.getHeight() - 5;
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tmp.getLayoutParams();
                    RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(newwidth, newheight);
                    layoutParams1.leftMargin = layoutParams.leftMargin;
                    layoutParams1.topMargin = layoutParams.topMargin;
                    layoutParams1.bottomMargin = layoutParams.bottomMargin;
                    layoutParams1.rightMargin = layoutParams.rightMargin;
                    tmp.setLayoutParams(layoutParams1);
                    System.out.println("Voilà les nouvelles valeurs de la cercle sélectionnée : ");

                    db.execSQL("UPDATE circle SET leftmargin=" + layoutParams1.leftMargin + ",rightmargin=" + layoutParams1.rightMargin + ",topmargin=" + layoutParams1.topMargin + ",bottommargin=" +
                            layoutParams1.bottomMargin + ",height=" + newheight + ",width=" + newwidth + " WHERE id=" + selected + ";");


                    if(!alreadyeditedthistime) {
                        Calendar c = Calendar.getInstance();
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int month = c.get(Calendar.MONTH) + 1;
                        int year = c.get(Calendar.YEAR);
                        db.execSQL("UPDATE video SET edited=" + 1 + ",lasteditday=" + day + ",lasteditmonth=" + month + ",lastedityear=" + year + " WHERE id=" + videoid + ";");
                        alreadyeditedthistime=true;
                    }

                }
            }
        });


        deletecircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected > 0) {
                    ImageView tmp = (ImageView) findViewById(selected);
                    tmp.setVisibility(View.INVISIBLE);
                    //DataBaseDisplayer d=new DataBaseDisplayer(mContext);
                    //d.showalltables();
                    System.out.println("selected to delete="+selected);



                    db.execSQL("DELETE FROM circle WHERE id=" + selected + ";");

                    blueguider.post(getCommentsPositions);


                    //DataBaseDisplayer d2=new DataBaseDisplayer(mContext);
                    //d2.showalltables();
                }
            }
        });

        helpicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity2.this);
                final View recipientLayout = getLayoutInflater().inflate(R.layout.helpdialoglayout,null);
                final TextView recipientTextView = (TextView) recipientLayout.findViewById(R.id.profilehelptext);
                /*recipientTextView.setText("This is your video editing seen :\nYou can perform multiple actions here :\n\n  1.Play/pause video:\nClick on the play/pause button in order to play or pause the video.\n\n" +
                "  2.Forward Video:\nAdvance video by 4 seconds.\n\n"+"  3.Backward:\nMove video back by 4 seconds.\n\n"+"  4.Adding a comment circle:\nOnly when the video is paused, Click on the + circle in order to add a comment circle to the scene. " +
                        "The comment will appear at the exact moment you added it in the video. It will be displayed for 2 seconds.\n\n" +
                "  5.Adding a comment text:\nOnly when the video is paused, While touching a circle with your index, click on the scence with your middle finger in orfer to add a comment text.\n\n"+"6.Drag and Drop:\nYou can drag and drop comment circles and texts to where you want them tobe displayed.\n\n" +
                        "  7.Delete comment circle:\nClick on a comment circle then click the delete circle button in order to delete it.\n\n"+"  8.Edit comment text:\nOnly when the video is paused, While touching a comment text with your index, click on the scence with your middle finger in orfer to pop up a window where you can insert your new text.\n\n" +
                        "  8.Delete comment text:\nIn order to delete comment text, simply in edit comment text mode, delete all characters. The comment will desappear.\n\n" +
                        "  9.Increase/Decrease comment circle size:\nClick on a comment circle then click the increase size/decrease size button in order to change the comment circle size.\n\n" +
                        "  10.Export the scene to a video file:\nIn order to export the scene to a video file, simply click on the record sequence button and allow permissions. The video will restart over and " +
                        "a scene export will take place. At the end, a success message will be displayed and you can check your video in the ScreenRecSample Directory under newspecapprepository. This feature is only available for Android 5.0 and above.\n\n" +
                        "  11.Exit:\nClick on exit button to exit edit mode.\n\n  12.Change sound volume:\nMove the green slider in order to change the video playing sound. This action will have no effect over the exported video files.\n\n" +
                        "  12.Navigate video:\nMove the blue slider in order to navigate in the video timeline.\n");
                        */
                recipientTextView.setText(getResources().getString(R.string.activity_video2_helptext));
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

                AlertDialog alert = builder.create();
                alert.show();
            }
        });






        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paused == 0) {
                    vid.pause();
                    pause.setImageResource(R.drawable.playbutton);
                    System.out.println("video paused");
                    paused = 1;
                    System.out.println("paused devient" + paused);
                } else {
                    if (paused == 1) {
                        vid.start();
                        pause.setImageResource(R.drawable.pausebutton);
                        System.out.println("video resumed");
                        paused = 0;
                        System.out.println("paused devient " + paused);
                        ///the Special Line ************************
                        sb_time.postDelayed(onEvrySecond, 200);
                        ///The special Line ************************
                    } else {
                        pause.setImageResource(R.drawable.playbutton);
                        System.out.println("video restarted");
                        vid.start();
                        vid.requestFocus();
                        paused = 0;
                        pause.setImageResource(R.drawable.pausebutton);
                        ///the Special Line ************************
                        sb_time.postDelayed(onEvrySecond,200);
                        ///The special Line ************************
                    }
                }
            }
        });
        vid.start();
        vid.requestFocus();
        vid.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                paused = 2;
                System.out.println("paused devient " + paused);
                pause.setImageResource(R.drawable.playbutton);


                vid.setVideoPath(path);
                if(inRecordingMode==true)
                {

                    mRecordButton.setChecked(false);
                    inRecordingMode=false;
                }
            }
        });


        if(sb_volume!=null)
        {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            sb_volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            System.out.println("************"+audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            //sb_volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            sb_volume.setProgress(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            sb_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                    if(progress <= 11 && progress >=1)
                    {
                        volumeicon.setImageResource(R.drawable.volumemid);
                    }
                    else
                    {
                        if(progress==0) volumeicon.setImageResource(R.drawable.volumezero);
                        else volumeicon.setImageResource(R.drawable.volumemax);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        if(sb_time!=null)
        {
            //sb_time.setMax(22803);
            System.out.println("*****duration *******" + duration);
            //sb_time.setProgress(0);

            sb_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if(fromUser)
                    {
                        vid.seekTo(progress);
                        duration = vid.getDuration();
                        int x= (int) (duration-progress);
                        hh =(int) (progress/1000)/3600;
                        mm =(int) ((progress/1000)-hh*3600)/60;
                        ss =(int) (progress/1000)-hh*3600-mm*60;
                        System.out.print("hh = " + hh + "mm =" + mm + "ss = " + ss);
                        if(ss>=10)
                        {
                            starttime.setText(mm+":"+ss);
                        }
                        else starttime.setText(mm+":0"+ss);

                        hh =(int) (x/1000)/3600;
                        mm =(int) ((x/1000)-hh*3600)/60;
                        ss =(int) (x/1000)-hh*3600-mm*60;
                        if(ss>=10)
                        {
                            endtime.setText(mm+":"+ss);
                        }
                        else endtime.setText(mm+":0"+ss);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }





    }





    public void addViews(View view) {


        if(paused==1)
        {
            RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ImageView img = new ImageView(this);
            img.setLayoutParams(lparams);
            img.setImageResource(R.drawable.comm4);
            numcircles++;
            System.out.println("nombre de cercles = " + numcircles);

            int desappeartime = vid.getCurrentPosition()+commentlength;
            db.execSQL("INSERT INTO circle(appeartime,desappeartime,leftmargin,rightmargin,topmargin,bottommargin,height,width,videoid) VALUES(" + vid.getCurrentPosition()+
            ","+desappeartime+","+lparams.leftMargin+","+lparams.rightMargin+","+lparams.topMargin+","+lparams.bottomMargin+","+73+","+72+","+videoid+");");

            c2 = db.rawQuery("SELECT * FROM circle ORDER BY id DESC LIMIT 1", null);
            int lasttoadd=0;
            if(c2.moveToNext())
            {
                lasttoadd=c2.getInt(0);
            }
            c2.close();


            img.setTag(lasttoadd);
            img.setId(lasttoadd);



            img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {


                    if (paused == 1) {

                        final int X = (int) event.getRawX();
                        final int Y = (int) event.getRawY();
                        if (mode == 1) {

                            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                case MotionEvent.ACTION_DOWN:
                                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                                    _xDelta = X - lParams.leftMargin;
                                    _yDelta = Y - lParams.topMargin;
                                    //selected = (int) view.getTag();

                                    selected = (int) view.getId();
                                    System.out.println("the selected object is of index " + selected);
                                    System.out.println("view id = " + view.getId());
                                    //start.set(event.getX(),event.getY());
                                    //mode=1;
                                    view.setBackgroundResource(R.drawable.comm4glow4);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    view.setBackgroundResource(R.drawable.comm4);
                                    System.out.println("*********finally the action has ended and I can store my values for circle " + selected + "*********");

                                    //*********Mise à jour après un drag ***************************
                                    System.out.println("mise a jour après drag");

                                    ImageView tmp = (ImageView) findViewById(selected);
                                    int newwidth = tmp.getWidth();
                                    int newheight = tmp.getHeight();
                                    RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) tmp.getLayoutParams();
                                    RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(newwidth, newheight);
                                    layoutParams1.leftMargin = layoutParams2.leftMargin;
                                    layoutParams1.topMargin = layoutParams2.topMargin;
                                    layoutParams1.bottomMargin = layoutParams2.bottomMargin;
                                    layoutParams1.rightMargin = layoutParams2.rightMargin;
                                    tmp.setLayoutParams(layoutParams1);
                                    System.out.println("Voilà les nouvelles valeurs de la cercle sélectionnée : ");


                                    db.execSQL("UPDATE circle SET leftmargin=" + layoutParams1.leftMargin + ",rightmargin=" + layoutParams1.rightMargin + ",topmargin=" + layoutParams1.topMargin+",bottommargin="+
                                            layoutParams1.bottomMargin+",height="+newheight+",width="+newwidth+" WHERE id="+tmp.getId()+";");

                                    if(!alreadyeditedthistime) {
                                        Calendar c = Calendar.getInstance();
                                        int day = c.get(Calendar.DAY_OF_MONTH);
                                        int month = c.get(Calendar.MONTH) + 1;
                                        int year = c.get(Calendar.YEAR);
                                        db.execSQL("UPDATE video SET edited=" + 1 + ",lasteditday=" + day + ",lasteditmonth=" + month + ",lastedityear=" + year + " WHERE id=" + videoid + ";");
                                        alreadyeditedthistime=true;
                                    }


                                    ///////////////////////////////////////////////////////////////////////////////////


                                    break;
                                case MotionEvent.ACTION_POINTER_DOWN:
                                    System.out.println("two fingers0000000000*******");
                                    Count = 0;
                                    addcomment(view);



                                    break;
                                case MotionEvent.ACTION_POINTER_UP:
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    //if(mode==1){

                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                            .getLayoutParams();
                                    layoutParams.leftMargin = X - _xDelta;
                                    layoutParams.topMargin = Y - _yDelta;
                                    layoutParams.rightMargin = -250;
                                    layoutParams.bottomMargin = -250;
                                    view.setLayoutParams(layoutParams);



                                    break;
                            }
                        } else {
                            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                case MotionEvent.ACTION_DOWN:

                                    break;
                                case MotionEvent.ACTION_UP:
                                    addcomment(view);
                                    break;
                                case MotionEvent.ACTION_POINTER_DOWN:

                                    break;
                                case MotionEvent.ACTION_POINTER_UP:
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    break;
                            }
                        }

                        mRrootLayout.invalidate();
                    }
                    return true;

                }
            });


            mRrootLayout.addView(img);
            blueguider.post(getCommentsPositions);

        }else{
            Toast.makeText(this, "Pause the video to make comments!", Toast.LENGTH_SHORT).show();
        }
    }


    public void addcomment(View view)
    {


        if(paused==1) {
            RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final EditText text = new EditText(this);
            text.setImeOptions(EditorInfo.IME_ACTION_DONE);
            text.setLayoutParams(lparams);
            text.setFocusableInTouchMode(true);
            text.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            text.setText("Comment");
            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                text.setBackgroundDrawable(getResources().getDrawable(R.drawable.linearlayout_bgcomment));
            }else{
                text.setBackground(getResources().getDrawable(R.drawable.linearlayout_bgcomment));
            }
            //****************************************************************************************
            int desappeartime = vid.getCurrentPosition()+commentlength;
            db.execSQL("INSERT INTO comment(appeartime,desappeartime,text,leftmargin,rightmargin,topmargin,bottommargin,videoid) VALUES(" + vid.getCurrentPosition()+
                    ","+desappeartime+",'"+text.getText()+"',"+lparams.leftMargin+","+lparams.rightMargin+","+lparams.topMargin+","+lparams.bottomMargin+","+videoid+");");


            if(!alreadyeditedthistime) {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH) + 1;
                int year = c.get(Calendar.YEAR);
                db.execSQL("UPDATE video SET edited=" + 1 + ",lasteditday=" + day + ",lasteditmonth=" + month + ",lastedityear=" + year + " WHERE id=" + videoid + ";");
                alreadyeditedthistime=true;
            }

            c3 = db.rawQuery("SELECT * FROM comment ORDER BY id DESC LIMIT 1", null);
            int lasttoaddcomment=0;
            if(c3.moveToNext())
            {
                lasttoaddcomment=c3.getInt(0);
                System.out.println("lasttoaddcomment = "+lasttoaddcomment);
            }
            c3.close();

            text.setTag(lasttoaddcomment+1000);
            text.setId(lasttoaddcomment+1000);




            text.setCursorVisible(false);

            text.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(paused==1) {

                        final int X = (int) event.getRawX();
                        final int Y = (int) event.getRawY();
                        final EditText tt = (EditText) v;
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                                _xDelta = X - lParams.leftMargin;
                                _yDelta = Y - lParams.topMargin;
                                Count++;
                                System.out.println("Count : " + Count);
                                if (Count == 2) {



                                    Count = 0;

                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                System.out.println("*********finally the action has ended and I can store my values for comment " + tt.getId() + "*********");
                                //*********Mise à jour après un drag ***************************
                                System.out.println("mise a jour après drag");
                                RelativeLayout.LayoutParams lParamscomment = (RelativeLayout.LayoutParams) v.getLayoutParams();

                                System.out.println("Voilà les nouvelles valeurs du comment sélectionné : ");
                                String enteredname0=tt.getText().toString().replace("'", "''");
                                int tmpstoreid=tt.getId()-1000;
                                db.execSQL("UPDATE comment SET leftmargin=" + lParamscomment.leftMargin + ",rightmargin=" + lParamscomment.rightMargin + ",topmargin=" + lParamscomment.topMargin + ",bottommargin=" +
                                        lParamscomment.bottomMargin + ",text='"+enteredname0+ "' WHERE id=" + tmpstoreid + ";");

                                if(!alreadyeditedthistime) {
                                    Calendar c = Calendar.getInstance();
                                    int day = c.get(Calendar.DAY_OF_MONTH);
                                    int month = c.get(Calendar.MONTH) + 1;
                                    int year = c.get(Calendar.YEAR);
                                    db.execSQL("UPDATE video SET edited=" + 1 + ",lasteditday=" + day + ",lasteditmonth=" + month + ",lastedityear=" + year + " WHERE id=" + videoid + ";");
                                    alreadyeditedthistime=true;
                                }



                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:


                                final EditText tmpcomm = new EditText(VideoActivity2.this);
                                tmpcomm.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                                tmpcomm.setText(text.getText());


                                AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity2.this);
                                builder.setTitle("Modify Comment");
                                builder.setView(tmpcomm);
                                builder.setMessage("Please Enter your new Comment:");
                                //builder.setCancelable(false);
                                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Intent intent = new Intent(VideoActivity.this, MainActivity.class);
                                        //startActivity(intent);

                                        String enteredname = tmpcomm.getText().toString();
                                        if(enteredname.trim().length()==0)
                                        {
                                            System.out.println("enteredname.trim().length()==0");
                                            tt.setVisibility(View.INVISIBLE);
                                            //*********Mise à jour après un textedit ***************************

                                            System.out.println("Voilà les nouvelles valeurs du comment sélectionné : ");
                                            int tmpstoreid = text.getId() - 1000;
                                            db.execSQL("DELETE FROM comment WHERE id=" + tmpstoreid + ";");
                                            System.out.println("comment deleted on length=0");
                                        }
                                        else {
                                            System.out.println("!enteredname.trim().length()==0");
                                            tt.setText(enteredname);
                                            String enteredname2 = enteredname.replace("'", "''");

                                            //*********Mise à jour après un textedit ***************************

                                            System.out.println("Voilà les nouvelles valeurs du comment sélectionné : ");
                                            int tmpstoreid = text.getId() - 1000;
                                            db.execSQL("UPDATE comment SET text='" + enteredname2 + "' WHERE id=" + tmpstoreid + ";");
                                            System.out.println("comment updated on else of length=0");
                                        }
                                        if(!alreadyeditedthistime) {
                                            Calendar c = Calendar.getInstance();
                                            int day = c.get(Calendar.DAY_OF_MONTH);
                                            int month = c.get(Calendar.MONTH) + 1;
                                            int year = c.get(Calendar.YEAR);
                                            db.execSQL("UPDATE video SET edited=" + 1 + ",lasteditday=" + day + ",lasteditmonth=" + month + ",lastedityear=" + year + " WHERE id=" + videoid + ";");
                                            alreadyeditedthistime=true;
                                        }


                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();


                                System.out.println("two fingers0000000000*******");
                                break;
                            case MotionEvent.ACTION_POINTER_UP:
                                break;
                            case MotionEvent.ACTION_MOVE:


                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v
                                        .getLayoutParams();
                                layoutParams.leftMargin = X - _xDelta;
                                layoutParams.topMargin = Y - _yDelta;
                                layoutParams.rightMargin = -250;
                                layoutParams.bottomMargin = -250;
                                v.setLayoutParams(layoutParams);
                                break;
                        }

                        mRrootLayout.invalidate();
                    }
                    return true;
                }

            });
            mRrootLayout.addView(text);
        }
        else{
            Toast.makeText(this, "Pause the video to make comments!", Toast.LENGTH_SHORT).show();
        }
    }


    private static ArrayList<View> getViewsByTag(ViewGroup root, String tag)
    {
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for(int i = 0;i<childCount;i++){
            final View child = root.getChildAt(i);
            if(child instanceof ViewGroup){
                views.addAll(getViewsByTag((ViewGroup)child,tag));
            }
            final Object tagObj = child.getTag();
            if(tagObj != null && tagObj.equals(tag)){
                views.add(child);
            }
        }
        return views;
    }


    public class Circle
    {
        private int id;
        private Integer appeartime;
        private Integer desappeartime;
        //public RelativeLayout.LayoutParams positionning;
        private Integer width;
        private Integer height;
        private Integer leftmargin;
        private Integer rightmargin;
        private Integer topmargin;
        private Integer bottommargin;


        public Circle(int id, Integer appeartime, Integer desappeartime, Integer width, Integer height, Integer leftmargin, Integer rightmargin, Integer topmargin, Integer bottommargin) {
            this.id = id;
            this.appeartime = appeartime;
            this.desappeartime = desappeartime;
            this.width = width;
            this.height = height;
            this.leftmargin = leftmargin;
            this.rightmargin = rightmargin;
            this.topmargin = topmargin;
            this.bottommargin = bottommargin;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Integer getAppeartime() {
            return appeartime;
        }

        public void setAppeartime(Integer appeartime) {
            this.appeartime = appeartime;
        }

        public Integer getDesappeartime() {
            return desappeartime;
        }

        public void setDesappeartime(Integer desappeartime) {
            this.desappeartime = desappeartime;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Integer getLeftmargin() {
            return leftmargin;
        }

        public void setLeftmargin(Integer leftmargin) {
            this.leftmargin = leftmargin;
        }

        public Integer getRightmargin() {
            return rightmargin;
        }

        public void setRightmargin(Integer rightmargin) {
            this.rightmargin = rightmargin;
        }

        public Integer getTopmargin() {
            return topmargin;
        }

        public void setTopmargin(Integer topmargin) {
            this.topmargin = topmargin;
        }

        public Integer getBottommargin() {
            return bottommargin;
        }

        public void setBottommargin(Integer bottommargin) {
            this.bottommargin = bottommargin;
        }

        @Override
        public String toString() {
            return "Circle{" +
                    "id=" + id +
                    ", appeartime=" + appeartime +
                    ", desappeartime=" + desappeartime +
                    ", width=" + width +
                    ", height=" + height +
                    ", leftmargin=" + leftmargin +
                    ", rightmargin=" + rightmargin +
                    ", topmargin=" + topmargin +
                    ", bottommargin=" + bottommargin +
                    '}';
        }
    }

    public class Comment
    {
        private int id;
        private Integer appeartime;
        private Integer desappeartime;
        //public RelativeLayout.LayoutParams positionning;
        private String text;
        private Integer leftmargin;
        private Integer rightmargin;
        private Integer topmargin;
        private Integer bottommargin;


        public Comment(int id, Integer appeartime, Integer desappeartime, String text, Integer leftmargin, Integer rightmargin, Integer topmargin, Integer bottommargin) {
            this.id = id;
            this.appeartime = appeartime;
            this.desappeartime = desappeartime;
            this.text = text;
            this.leftmargin = leftmargin;
            this.rightmargin = rightmargin;
            this.topmargin = topmargin;
            this.bottommargin = bottommargin;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Integer getAppeartime() {
            return appeartime;
        }

        public void setAppeartime(Integer appeartime) {
            this.appeartime = appeartime;
        }

        public Integer getDesappeartime() {
            return desappeartime;
        }

        public void setDesappeartime(Integer desappeartime) {
            this.desappeartime = desappeartime;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getLeftmargin() {
            return leftmargin;
        }

        public void setLeftmargin(Integer leftmargin) {
            this.leftmargin = leftmargin;
        }

        public Integer getRightmargin() {
            return rightmargin;
        }

        public void setRightmargin(Integer rightmargin) {
            this.rightmargin = rightmargin;
        }

        public Integer getTopmargin() {
            return topmargin;
        }

        public void setTopmargin(Integer topmargin) {
            this.topmargin = topmargin;
        }

        public Integer getBottommargin() {
            return bottommargin;
        }

        public void setBottommargin(Integer bottommargin) {
            this.bottommargin = bottommargin;
        }

        @Override
        public String toString() {
            return "Comment{" +
                    "id=" + id +
                    ", appeartime=" + appeartime +
                    ", desappeartime=" + desappeartime +
                    ", text='" + text + '\'' +
                    ", leftmargin=" + leftmargin +
                    ", rightmargin=" + rightmargin +
                    ", topmargin=" + topmargin +
                    ", bottommargin=" + bottommargin +
                    '}';
        }
    }


    private void RecreateScene() {

        c6 = db.rawQuery("SELECT * FROM circle where videoid="+videoid, null);
        while(c6.moveToNext())
        {
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(c6.getInt(8), c6.getInt(7));
            ImageView img = new ImageView(this);
            img.setImageResource(R.drawable.comm4);
            layoutParams1.leftMargin = c6.getInt(3);
            layoutParams1.topMargin = c6.getInt(5);
            layoutParams1.bottomMargin = c6.getInt(6);
            layoutParams1.rightMargin = c6.getInt(4);
            img.setLayoutParams(layoutParams1);
            img.setTag(c6.getInt(0));
            img.setId(c6.getInt(0));
            img.setVisibility(View.INVISIBLE);
            img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (paused == 1) {
                        final int X = (int) event.getRawX();
                        final int Y = (int) event.getRawY();
                        if (mode == 1) {
                            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                case MotionEvent.ACTION_DOWN:
                                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                                    _xDelta = X - lParams.leftMargin;
                                    _yDelta = Y - lParams.topMargin;
                                    selected = (int) view.getId();
                                    System.out.println("the selected object is of index " + selected);
                                    System.out.println("view id = " + view.getId());
                                    view.setBackgroundResource(R.drawable.comm4glow4);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    view.setBackgroundResource(R.drawable.comm4);
                                    System.out.println("*********finally the action has ended and I can store my values for circle " + selected + "*********");

                                    //*********Mise à jour après un drag ***************************
                                    System.out.println("mise a jour après drag");

                                    ImageView tmp = (ImageView) findViewById(selected);
                                    int newwidth = tmp.getWidth();
                                    int newheight = tmp.getHeight();
                                    RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) tmp.getLayoutParams();
                                    RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(newwidth, newheight);
                                    layoutParams1.leftMargin = layoutParams2.leftMargin;
                                    layoutParams1.topMargin = layoutParams2.topMargin;
                                    layoutParams1.bottomMargin = layoutParams2.bottomMargin;
                                    layoutParams1.rightMargin = layoutParams2.rightMargin;
                                    tmp.setLayoutParams(layoutParams1);
                                    System.out.println("Voilà les nouvelles valeurs de la cercle sélectionnée : ");


                                    db.execSQL("UPDATE circle SET leftmargin=" + layoutParams1.leftMargin + ",rightmargin=" + layoutParams1.rightMargin + ",topmargin=" + layoutParams1.topMargin + ",bottommargin=" +
                                            layoutParams1.bottomMargin + ",height=" + newheight + ",width=" + newwidth + " WHERE id=" + tmp.getId() + ";");

                                    if(!alreadyeditedthistime) {
                                        Calendar c = Calendar.getInstance();
                                        int day = c.get(Calendar.DAY_OF_MONTH);
                                        int month = c.get(Calendar.MONTH) + 1;
                                        int year = c.get(Calendar.YEAR);
                                        db.execSQL("UPDATE video SET edited=" + 1 + ",lasteditday=" + day + ",lasteditmonth=" + month + ",lastedityear=" + year + " WHERE id=" + videoid + ";");
                                        alreadyeditedthistime=true;
                                    }

                                    break;
                                case MotionEvent.ACTION_POINTER_DOWN:
                                    System.out.println("two fingers0000000000*******");
                                    Count = 0;
                                    addcomment(view);

                                    break;
                                case MotionEvent.ACTION_POINTER_UP:
                                    break;
                                case MotionEvent.ACTION_MOVE:

                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                            .getLayoutParams();
                                    layoutParams.leftMargin = X - _xDelta;
                                    layoutParams.topMargin = Y - _yDelta;
                                    layoutParams.rightMargin = -250;
                                    layoutParams.bottomMargin = -250;
                                    view.setLayoutParams(layoutParams);


                                    break;
                            }
                        } else {
                            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                case MotionEvent.ACTION_DOWN:

                                    break;
                                case MotionEvent.ACTION_UP:
                                    addcomment(view);
                                    break;
                                case MotionEvent.ACTION_POINTER_DOWN:

                                    break;
                                case MotionEvent.ACTION_POINTER_UP:
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    break;
                            }
                        }

                        mRrootLayout.invalidate();
                    }
                    return true;

                }
            });

            mRrootLayout.invalidate();
            mRrootLayout.addView(img);
        }
        c6.close();



        c7=db.rawQuery("SELECT * FROM comment where videoid="+videoid,null);
        while(c7.moveToNext())
        {
            RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final EditText text = new EditText(this);
            text.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            text.setText(c7.getString(3));
            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                text.setBackgroundDrawable(getResources().getDrawable(R.drawable.linearlayout_bgcomment));
            }else{
                text.setBackground(getResources().getDrawable(R.drawable.linearlayout_bgcomment));
            }
            text.setImeOptions(EditorInfo.IME_ACTION_DONE);
            lparams.leftMargin=c7.getInt(4);
            lparams.topMargin=c7.getInt(6);
            lparams.bottomMargin=c7.getInt(7);
            lparams.rightMargin=c7.getInt(5);
            text.setLayoutParams(lparams);
            text.setFocusableInTouchMode(true);
            text.setTag(c7.getInt(0) + 1000);
            text.setId(c7.getInt(0) + 1000);
            text.setVisibility(View.INVISIBLE);

            text.setCursorVisible(false);

            text.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (paused == 1) {

                        final int X = (int) event.getRawX();
                        final int Y = (int) event.getRawY();
                        final EditText tt = (EditText) v;
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                                _xDelta = X - lParams.leftMargin;
                                _yDelta = Y - lParams.topMargin;
                                Count++;
                                System.out.println("Count : " + Count);
                                if (Count == 2) {



                                    Count = 0;

                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                System.out.println("*********finally the action has ended and I can store my values for comment " + tt.getId() + "*********");
                                //*********Mise à jour après un drag ***************************
                                System.out.println("mise a jour après drag");
                                RelativeLayout.LayoutParams lParamscomment = (RelativeLayout.LayoutParams) v.getLayoutParams();

                                System.out.println("Voilà les nouvelles valeurs du comment sélectionné : ");
                                String enteredname0 = tt.getText().toString().replace("'", "''");
                                int tmpstoreid = tt.getId() - 1000;
                                db.execSQL("UPDATE comment SET leftmargin=" + lParamscomment.leftMargin + ",rightmargin=" + lParamscomment.rightMargin + ",topmargin=" + lParamscomment.topMargin + ",bottommargin=" +
                                        lParamscomment.bottomMargin + ",text='" + enteredname0 + "' WHERE id=" + tmpstoreid + ";");

                                if(!alreadyeditedthistime) {
                                    Calendar c = Calendar.getInstance();
                                    int day = c.get(Calendar.DAY_OF_MONTH);
                                    int month = c.get(Calendar.MONTH) + 1;
                                    int year = c.get(Calendar.YEAR);
                                    db.execSQL("UPDATE video SET edited=" + 1 + ",lasteditday=" + day + ",lasteditmonth=" + month + ",lastedityear=" + year + " WHERE id=" + videoid + ";");
                                    alreadyeditedthistime=true;
                                }




                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:


                                final EditText tmpcomm = new EditText(VideoActivity2.this);
                                tmpcomm.setText(text.getText());


                                AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity2.this);
                                builder.setTitle("Modify Comment");
                                builder.setView(tmpcomm);
                                builder.setMessage("Please Enter your new Comment:");
                                //builder.setCancelable(false);
                                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        String enteredname = tmpcomm.getText().toString();
                                        if(enteredname.trim().length()==0)
                                        {
                                            System.out.println("enteredname.trim().length()==0");
                                            tt.setVisibility(View.INVISIBLE);
                                            //*********Mise à jour après un textedit ***************************

                                            System.out.println("Voilà les nouvelles valeurs du comment sélectionné : ");
                                            int tmpstoreid = text.getId() - 1000;
                                            db.execSQL("DELETE FROM comment WHERE id=" + tmpstoreid + ";");
                                            System.out.println("comment deleted on length=0");
                                        }
                                        else {
                                            System.out.println("!enteredname.trim().length()==0");
                                            tt.setText(enteredname);
                                            String enteredname2 = enteredname.replace("'", "''");

                                            //*********Mise à jour après un textedit ***************************

                                            System.out.println("Voilà les nouvelles valeurs du comment sélectionné : ");
                                            int tmpstoreid = text.getId() - 1000;
                                            db.execSQL("UPDATE comment SET text='" + enteredname2 + "' WHERE id=" + tmpstoreid + ";");
                                            System.out.println("comment updated on else of length=0");
                                        }



                                        if(!alreadyeditedthistime) {
                                            Calendar c = Calendar.getInstance();
                                            int day = c.get(Calendar.DAY_OF_MONTH);
                                            int month = c.get(Calendar.MONTH) + 1;
                                            int year = c.get(Calendar.YEAR);
                                            db.execSQL("UPDATE video SET edited=" + 1 + ",lasteditday=" + day + ",lasteditmonth=" + month + ",lastedityear=" + year + " WHERE id=" + videoid + ";");
                                            alreadyeditedthistime=true;
                                        }

                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();


                                System.out.println("two fingers0000000000*******");
                                break;
                            case MotionEvent.ACTION_POINTER_UP:
                                break;
                            case MotionEvent.ACTION_MOVE:


                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v
                                        .getLayoutParams();
                                layoutParams.leftMargin = X - _xDelta;
                                layoutParams.topMargin = Y - _yDelta;
                                layoutParams.rightMargin = -250;
                                layoutParams.bottomMargin = -250;
                                v.setLayoutParams(layoutParams);
                                break;
                        }

                        mRrootLayout.invalidate();
                    }
                    return true;
                }

            });


            mRrootLayout.invalidate();
            mRrootLayout.addView(text);

        }
        c7.close();

    }



    private void SetupVariables() {
        vid = (VideoView) findViewById(R.id.videoView);
        pause = (ImageButton) findViewById(R.id.pauseimageButton);
        sb_volume = (SeekBar) findViewById(R.id.volumebar);
        volumeicon = (ImageView) findViewById(R.id.volumeicon);
        sb_time = (SeekBar) findViewById(R.id.timeBar);
        starttime = (TextView) findViewById(R.id.starttime);
        endtime = (TextView) findViewById(R.id.endtime);
        backward = (ImageButton) findViewById(R.id.backwardimageButton);
        forward = (ImageButton) findViewById(R.id.forwardimageButton);
        exit = (ImageButton) findViewById(R.id.exit);
        add = (ImageButton) findViewById(R.id.add);
        mRrootLayout = (ViewGroup) findViewById(R.id.graphics_holder);
        increase = (Button) findViewById(R.id.increase);
        decrease = (Button) findViewById(R.id.decrease);
        deletecircle = (Button) findViewById(R.id.deletecircle);
        upbar = (ImageView) findViewById(R.id.upbar);
        downbar = (ImageView) findViewById(R.id.imageView3);
        redtext = (TextView) findViewById(R.id.textView1);
        redtext.setVisibility(View.INVISIBLE);
        helpicon = (ImageView) findViewById(R.id.searchimageprofile2);
        helpicon2 = (ImageView) findViewById(R.id.searchimageprofile);
        blueguider = (RelativeLayout) findViewById(R.id.blueguider);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG) Log.v(TAG, "onResume:");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ScreenRecorderService.ACTION_QUERY_STATUS_RESULT);
            registerReceiver(mReceiver, intentFilter);
            queryRecordingStatus();
        }
    }

    @Override
    protected void onPause() {
        if (DEBUG) Log.v(TAG, "onPause:");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            unregisterReceiver(mReceiver);
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (DEBUG) Log.v(TAG, "onActivityResult:resultCode=" + resultCode + ",data=" + data);
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_SCREEN_CAPTURE == requestCode) {
            if (resultCode != Activity.RESULT_OK) {
                // when no permission
                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                return;
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startScreenRecorder(resultCode, data);
            }
        }
    }

    private final CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                switch (buttonView.getId()) {
                    case R.id.record_button:
                        if (isChecked) {
                            //*************Hide everything Action just before record is declenched*******************
                            setupviewforrecord();
                            inRecordingMode = true;
                            mRecordButton.setVisibility(View.INVISIBLE);
                            mPauseButton.setVisibility(View.INVISIBLE);


                            //******************************************************************
                            final MediaProjectionManager manager
                                    = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                            final Intent permissionIntent = manager.createScreenCaptureIntent();
                            Toast.makeText(VideoActivity2.this, "Do not touch or exit until recording ends", Toast.LENGTH_SHORT).show();
                            vid.seekTo(0);
                            vid.pause();
                            startActivityForResult(permissionIntent, REQUEST_CODE_SCREEN_CAPTURE);
                            //Toast.makeText(VideoActivity2.this,"Recording started",Toast.LENGTH_LONG).show();


                        } else {
                            final Intent intent = new Intent(VideoActivity2.this, ScreenRecorderService.class);
                            intent.setAction(ScreenRecorderService.ACTION_STOP);
                            startService(intent);
                            //Toast.makeText(VideoActivity2.this,"Recording ended",Toast.LENGTH_LONG).show();
                            setupviewafterstop();
                            Toast.makeText(VideoActivity2.this, "Recording ended successfully", Toast.LENGTH_LONG).show();
                            mRecordButton.setText("Export Sequence");

                        }
                        break;
                    case R.id.pause_button:
                        if (isChecked) {
                            final Intent intent = new Intent(VideoActivity2.this, ScreenRecorderService.class);
                            intent.setAction(ScreenRecorderService.ACTION_PAUSE);
                            startService(intent);
                            //Toast.makeText(VideoActivity2.this,"Recording paused",Toast.LENGTH_LONG).show();
                            setupviewafterstop();
                        } else {
                            final Intent intent = new Intent(VideoActivity2.this, ScreenRecorderService.class);
                            intent.setAction(ScreenRecorderService.ACTION_RESUME);
                            startService(intent);
                            //Toast.makeText(VideoActivity2.this,"Recording resumed",Toast.LENGTH_LONG).show();
                            setupviewforrecord();
                        }
                        break;
                }
            }
        }
    };

    private void queryRecordingStatus() {
        if (DEBUG) Log.v(TAG, "queryRecording:");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Intent intent = new Intent(this, ScreenRecorderService.class);
            intent.setAction(ScreenRecorderService.ACTION_QUERY_STATUS);
            startService(intent);
        }
    }

    private void startScreenRecorder(final int resultCode, final Intent data) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vid.setVideoPath(path);
            //vid.seekTo(0);
            vid.start();
            vid.requestFocus();

            final Intent intent = new Intent(this, ScreenRecorderService.class);
            intent.setAction(ScreenRecorderService.ACTION_START);
            intent.putExtra(ScreenRecorderService.EXTRA_RESULT_CODE, resultCode);
            intent.putExtras(data);
            startService(intent);
        }
    }

    private void updateRecording(final boolean isRecording, final boolean isPausing) {
        if (DEBUG) Log.v(TAG, "updateRecording:isRecording=" + isRecording + ",isPausing=" + isPausing);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mRecordButton.setOnCheckedChangeListener(null);
            mPauseButton.setOnCheckedChangeListener(null);
            try {
                mRecordButton.setChecked(isRecording);
                mPauseButton.setEnabled(isRecording);
                mPauseButton.setChecked(isPausing);
            } finally {
                mRecordButton.setOnCheckedChangeListener(mOnCheckedChangeListener);
                mPauseButton.setOnCheckedChangeListener(mOnCheckedChangeListener);
            }
        }
    }

    private static final class MyBroadcastReceiver extends BroadcastReceiver {

        private final WeakReference<VideoActivity2> mWeakParent;
        public MyBroadcastReceiver(final VideoActivity2 parent) {
                mWeakParent = new WeakReference<VideoActivity2>(parent);
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (DEBUG) Log.v(TAG, "onReceive:" + intent);
                final String action = intent.getAction();
                if (ScreenRecorderService.ACTION_QUERY_STATUS_RESULT.equals(action)) {
                    final boolean isRecording = intent.getBooleanExtra(ScreenRecorderService.EXTRA_QUERY_RESULT_RECORDING, false);
                    final boolean isPausing = intent.getBooleanExtra(ScreenRecorderService.EXTRA_QUERY_RESULT_PAUSING, false);
                    final VideoActivity2 parent = mWeakParent.get();
                    if (parent != null) {
                        parent.updateRecording(isRecording, isPausing);
                    }
                }
            }
        }
    }

    private void setupviewforrecord() {
        //vid = (VideoView) findViewById(R.id.videoView);
        pause.setVisibility(View.INVISIBLE);
        sb_volume.setVisibility(View.INVISIBLE);
        volumeicon.setVisibility(View.INVISIBLE);
        sb_time.setVisibility(View.INVISIBLE);
        starttime.setVisibility(View.INVISIBLE);
        endtime.setVisibility(View.INVISIBLE);
        backward.setVisibility(View.INVISIBLE);
        forward.setVisibility(View.INVISIBLE);
        exit.setVisibility(View.INVISIBLE);
        add.setVisibility(View.INVISIBLE);
        //mRrootLayout = (ViewGroup) findViewById(R.id.graphics_holder);
        increase.setVisibility(View.INVISIBLE);
        decrease.setVisibility(View.INVISIBLE);
        deletecircle.setVisibility(View.INVISIBLE);
        upbar.setVisibility(View.INVISIBLE);
        downbar.setVisibility(View.INVISIBLE);
        redtext.setVisibility(View.INVISIBLE);
        mRecordButton.setVisibility(View.INVISIBLE);
        mPauseButton.setVisibility(View.INVISIBLE);
        helpicon.setVisibility(View.INVISIBLE);
        helpicon2.setVisibility(View.INVISIBLE);

        blueguider.setVisibility(View.INVISIBLE);
    }
        private void setupviewafterstop(){
            //vid = (VideoView) findViewById(R.id.videoView);
            pause.setVisibility(View.VISIBLE);
            sb_volume.setVisibility(View.VISIBLE);
            volumeicon.setVisibility(View.VISIBLE);
            sb_time.setVisibility(View.VISIBLE);
            starttime.setVisibility(View.VISIBLE);
            endtime.setVisibility(View.VISIBLE);
            backward.setVisibility(View.VISIBLE);
            forward.setVisibility(View.VISIBLE);
            exit.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
            //mRrootLayout = (ViewGroup) findViewById(R.id.graphics_holder);
            increase.setVisibility(View.VISIBLE);
            decrease.setVisibility(View.VISIBLE);
            deletecircle.setVisibility(View.VISIBLE);
            upbar.setVisibility(View.VISIBLE);
            downbar.setVisibility(View.VISIBLE);
            //redtext.setVisibility(View.VISIBLE);
            mRecordButton.setChecked(false);
            mRecordButton.setVisibility(View.VISIBLE);
            helpicon.setVisibility(View.VISIBLE);
            helpicon2.setVisibility(View.VISIBLE);

            blueguider.setVisibility(View.VISIBLE);

            //mPauseButton.setVisibility(View.INVISIBLE);


    }

}
