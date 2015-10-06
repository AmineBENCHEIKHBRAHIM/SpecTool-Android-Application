package thefinal;

/**
 * Created by Neopterix on 24/08/15.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.neopterix.uploadgallery.R;

import java.io.IOException;
import java.util.Random;

/**
 * Created by Neopterix on 20/08/15.
 */
public class VideoRecordingActivity extends Activity {

    private Camera myCamera;
    private MyCameraSurfaceView myCameraSurfaceView;
    private MediaRecorder mediaRecorder;
    private Integer userid=null;
    private String videoname = null;
    private String location = null;
    private String reference = null;
    private Integer secretnumber = null;
    private String description = null;
    private Integer creationday=null;
    private Integer creationmonth=null;
    private Integer creationyear=null;
    //private String path="/mnt/sdcard/specapprepository/specappvids/";
    private String path= Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappvids/";
    boolean success = false;
    Button myButton;
    SurfaceHolder surfaceHolder;
    boolean recording;
    SQLiteDatabase db;


    int min=10;
    int max=20000;
    Random r=new Random();
    int i1=r.nextInt(max - min + 1)+min;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videorecording);
        SetupVariables();

        /*
        File folder = new File(Environment.getExternalStorageDirectory()+"/newspecapprepository");
        if(!folder.exists()){
            success = folder.mkdir();
        }
        if(success)
        {
            System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository" + " - Created with success");
            File folder2 = new File(Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappvids");
            if(!folder2.exists()){
                success = folder2.mkdir();
            }
            if(success)
            {
                System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappvids" + " - Created with success");
            }
            else{
                System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappvids/" + " - Creation failure");
            }
        }
        else{
            System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository" + " - Creation failure");
        }

        */
        db = openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);

        Intent intent = getIntent();
        final Integer id = intent.getIntExtra("userid",-1);
        userid=id;
        System.out.println("this is the id : " + id);
        videoname = intent.getStringExtra("videoname");
        location = intent.getStringExtra("location");
        reference = intent.getStringExtra("reference");
        secretnumber = intent.getIntExtra("secretnumber", 0);
        description = intent.getStringExtra("description").replace("'","''");
        creationday = intent.getIntExtra("creationday", 31);
        creationmonth = intent.getIntExtra("creationmonth",12);
        creationyear = intent.getIntExtra("creationyear",1900);
        System.out.println("video name = "+videoname);
        System.out.println("location = "+location);
        System.out.println("reference = "+reference);
        System.out.println("secret number = "+secretnumber);
        System.out.println("description = "+description);
        System.out.println("creation day = "+creationday);
        System.out.println("creation month = "+creationmonth);
        System.out.println("creation year = "+creationyear);


        recording = false;
        //Get Camera for preview
        myCamera = getCameraInstance();
        if(myCamera == null){
            Toast.makeText(VideoRecordingActivity.this,
                    "Fail to get Camera",
                    Toast.LENGTH_LONG).show();
        }

        myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
        FrameLayout myCameraPreview = (FrameLayout)findViewById(R.id.videoview);
        myCameraPreview.addView(myCameraSurfaceView);

        myButton = (Button)findViewById(R.id.mybutton);
        myButton.setOnClickListener(myButtonOnClickListener);



    }

    Button.OnClickListener myButtonOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(recording){
                // stop recording and release camera
                mediaRecorder.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object

                //Exit after saved
                //finish();
                //***********get the recorded video length
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                //retriever.setDataSource(path+videoname+".mp4");
                retriever.setDataSource(path+"user"+userid+videoname+i1+".mp4");
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInmillisec = Long.parseLong(time);
                long duration = timeInmillisec/1000;
                long hours = duration/3600;
                long minutes = (duration - hours*3600)/60;
                long seconds = duration - (hours*3600+minutes*60);
                System.out.println("duration = "+hours+":"+minutes+":"+seconds);
                //***********************************************
                //***********DataBase Video Adding Transaction *******************
                db.execSQL("CREATE TABLE IF NOT EXISTS video(id INTEGER PRIMARY KEY AUTOINCREMENT,creationday INTEGER,creationmonth INTEGER," +
                        "creationyear INTEGER,lengthhours INTEGER,lengthminutes INTEGER,lengthseconds INTEGER,edited INTEGER,name TEXT,location TEXT,reference TEXT,secretnumber INTEGER," +
                        "description TEXT,reviews INTEGER,lasteditday INTEGER,lasteditmonth INTEGER,lastedityear INTEGER,path TEXT,userid INTEGER,FOREIGN KEY(userid) REFERENCES user(id)); ");




                db.execSQL("INSERT INTO video(creationday,creationmonth,creationyear,lengthhours,lengthminutes,lengthseconds,edited,name,location,reference,secretnumber,description,reviews,lasteditday,lasteditmonth,lastedityear,path,userid) " +
                        "VALUES("+creationday+","+creationmonth+","+creationyear+","+hours+","+minutes+","+seconds+","+0+",'"+videoname+"','"+location+"','"+reference+"',"+secretnumber+",'"+description+"',"+0+","+null+","+null+","+null+",'"+path+"user"+userid+videoname+i1+".mp4',"+userid+");" );
                //************************************************
                Intent intent= new Intent(VideoRecordingActivity.this,ProfileActivity.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }else{

                //Release Camera before MediaRecorder start
                releaseCamera();

                if(!prepareMediaRecorder()){
                    Toast.makeText(VideoRecordingActivity.this,
                            "Fail in prepareMediaRecorder()!\n - Ended -",
                            Toast.LENGTH_LONG).show();
                    finish();
                }

                mediaRecorder.start();
                recording = true;
                myButton.setText("STOP");
            }
        }};

    private Camera getCameraInstance(){
        // TODO Auto-generated method stub
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private boolean prepareMediaRecorder(){
        myCamera = getCameraInstance();
        mediaRecorder = new MediaRecorder();

        myCamera.unlock();
        mediaRecorder.setCamera(myCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        /*
        mediaRecorder.setVideoSize(640,480);
        mediaRecorder.setVideoFrameRate(16);
        mediaRecorder.setVideoEncodingBitRate(3000000);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        */

        //mediaRecorder.setOutputFile(path+videoname+".mp4");
        mediaRecorder.setOutputFile(path+"user"+userid+videoname+i1+".mp4");
        //mediaRecorder.setMaxDuration(6000); // Set max duration 60 sec.
        //mediaRecorder.setMaxFileSize(500000); // Set max file size 5M

        mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            myCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (myCamera != null){
            myCamera.release();        // release the camera for other applications
            myCamera = null;
        }
    }

    public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
        private SurfaceHolder mHolder;
        private Camera mCamera;

        public MyCameraSurfaceView(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int weight,
                                   int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // make any resize, rotate or reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
            }
        }


        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }
    }



    private void SetupVariables()
    {

    }
}
