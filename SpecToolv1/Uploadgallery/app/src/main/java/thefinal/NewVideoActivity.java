package thefinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neopterix.uploadgallery.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * Created by Neopterix on 20/08/15.
 */
public class NewVideoActivity extends AppCompatActivity {

    EditText videoname = null;
    EditText location = null;
    EditText reference = null;
    EditText secretnumber = null;
    EditText description = null;
    DatePicker datePicker = null;
    Button startrec = null;
    Button uploadvid=null;

    int userid=0;
    SQLiteDatabase db;

    String extension=null;

    ImageView helpicon = null;

    int min=10;
    int max=20000;
    Random r=new Random();
    int i1=r.nextInt(max - min + 1)+min;

    private static int RESULT_LOAD_IMG=1;
    String imgDecodableString;


    private String path= Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappvids/";
    boolean success = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newvideo);
        SetupVariables();
        Intent intent = getIntent();
        final Integer id = intent.getIntExtra("userid",-1);
        System.out.println("this is the id : " + id);
        userid=id;
        startrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoname.getText().toString().trim().length() == 0 || location.getText().toString().trim().length() == 0 || reference.getText().toString().trim().length() == 0
                        || secretnumber.getText().toString().trim().length() == 0 || description.getText().toString().trim().length() == 0) {
                    Toast.makeText(NewVideoActivity.this, "Enter all values", Toast.LENGTH_LONG).show();
                } else {

                    if(Integer.parseInt(secretnumber.getText().toString())<1 || Integer.parseInt(secretnumber.getText().toString())>10)
                    {
                        Toast.makeText(NewVideoActivity.this, "Enter comment length value between 1 and 10", Toast.LENGTH_LONG).show();
                    }
                    else {

                        datePicker.getMonth();
                        datePicker.getYear();
                        Intent intent = new Intent(NewVideoActivity.this, VideoRecordingActivity.class);
                        //intent.putExtra("username",login.getText().toString());
                        intent.putExtra("userid", id);
                        intent.putExtra("videoname", videoname.getText().toString());
                        intent.putExtra("location", location.getText().toString());
                        intent.putExtra("reference", reference.getText().toString());
                        intent.putExtra("secretnumber", Integer.parseInt(secretnumber.getText().toString()));
                        //secret number here is a connotation for the comment length value
                        intent.putExtra("description", description.getText().toString());

                        intent.putExtra("creationday", datePicker.getDayOfMonth());
                        intent.putExtra("creationmonth", datePicker.getMonth() + 1);
                        intent.putExtra("creationyear", datePicker.getYear());

                        startActivity(intent);
                    }

                }
            }
        });

        uploadvid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoname.getText().toString().trim().length() == 0 || location.getText().toString().trim().length() == 0 || reference.getText().toString().trim().length() == 0
                        || secretnumber.getText().toString().trim().length() == 0 || description.getText().toString().trim().length() == 0) {
                    Toast.makeText(NewVideoActivity.this, "Enter all values", Toast.LENGTH_LONG).show();
                } else {

                    LoadImageFromGallery(uploadvid);
/*
                    Intent intent = new Intent(NewVideoActivity.this, VideoRecordingActivity.class);
                    //intent.putExtra("username",login.getText().toString());
                    intent.putExtra("userid", id);
                    intent.putExtra("videoname", videoname.getText().toString());
                    intent.putExtra("location", location.getText().toString());
                    intent.putExtra("reference", reference.getText().toString());
                    intent.putExtra("secretnumber", Integer.parseInt(secretnumber.getText().toString()));
                    intent.putExtra("description", description.getText().toString());

                    intent.putExtra("creationday", datePicker.getDayOfMonth());
                    intent.putExtra("creationmonth", datePicker.getMonth() + 1);
                    intent.putExtra("creationyear", datePicker.getYear());

                    startActivity(intent);

*/
                }
            }
        });

        helpicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewVideoActivity.this);
                final View recipientLayout = getLayoutInflater().inflate(R.layout.helpdialoglayout,null);
                final TextView recipientTextView = (TextView) recipientLayout.findViewById(R.id.profilehelptext);
                /*recipientTextView.setText("This is the new video creation page :\nYou should fill all fields below :\n\n  1.Video Name:\nEnter the name you want to give to your video.\n\n" +
                        "  2.Location:\nEnter the location the video was taken.\n\n"+"  3.Date:\nSelect the date the video was taken.\n\n"+"  4.Reference:\nEnter the video reference.Any value is accepted at this stage of the application.\n\n"+
                        "  5.Secret number:\nEnter the secret number. Any value is accepted at this stage of the application.\n\n"+"  6.Video description:\nEnter the description of the video. A maximum of 200 letters is applicable.\n\n"+
                "Click on START RECORDING if you want to record a video using the camera or click on UPLOAD VIDEO if you want to upload a video from the SD CARD.\n");
                */
                recipientTextView.setText(getResources().getString(R.string.activity_newvideo_helptext));
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


    public void LoadImageFromGallery(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode==RESULT_LOAD_IMG && resultCode==RESULT_OK && null!=data){
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Video.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                System.out.println("imgDecodableString ="+imgDecodableString);
                cursor.close();
                //ImageView imgView = (ImageView) findViewById(R.id.imgView);
                //imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                //fullwidthpicture.setBackgroundResource(R.drawable.profilepart2trans);
                //profilepicture.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                //************ Creation of a folder for the profile pictures******************************


                String destinationImagePath=null;

                try{
                    File sd = Environment.getExternalStorageDirectory();
                    //File data2 = Environment.getDataDirectory();
                    if(sd.canWrite()){

                        File source = new File(imgDecodableString);
                        extension = imgDecodableString.substring(imgDecodableString.lastIndexOf('.')+1);
                        destinationImagePath=path+"user"+userid+videoname.getText().toString()+i1+"."+extension;
                        File destination = new File(destinationImagePath);
                        if(source.exists()){
                            FileChannel src = new FileInputStream(source).getChannel();
                            FileChannel dst = new FileOutputStream(destination).getChannel();
                            dst.transferFrom(src,0,src.size());
                            src.close();
                            dst.close();
                        }
                        else{Toast.makeText(this,"file non existing",Toast.LENGTH_LONG).show();}

                        /*
                        InputStream in = new FileInputStream(imgDecodableString);
                        extension = imgDecodableString.substring(imgDecodableString.lastIndexOf('.')+1);
                        System.out.println("the extension is ="+extension);
                        destinationImagePath=path+"user"+userid+videoname.getText().toString()+i1+"."+extension;
                        System.out.println("path="+path+"user"+userid+videoname.getText().toString()+i1+"."+extension);
                        File destination = new File(destinationImagePath);
                        OutputStream out = new FileOutputStream(destination);
                        byte[] buf = new byte[1024];
                        int len;
                        while((len=in.read(buf))>0){
                            out.write(buf,0,len);
                        }
                        in.close();
                        out.close();*/
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this,"Didnot copy",Toast.LENGTH_LONG).show();}


                //*****************************************************************************************
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                //retriever.setDataSource(path+videoname+".mp4");
                retriever.setDataSource(destinationImagePath);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInmillisec = Long.parseLong(time);
                long duration = timeInmillisec/1000;
                long hours = duration/3600;
                long minutes = (duration - hours*3600)/60;
                long seconds = duration - (hours*3600+minutes*60);
                System.out.println("duration = "+hours + ":" + minutes + ":" + seconds);

                int creationday=datePicker.getDayOfMonth();
                int creationmonth=datePicker.getMonth() + 1;
                int creationyear=datePicker.getYear();
                db = openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);
                //db.execSQL("UPDATE user SET picturepath='" + destinationImagePath + "' WHERE id=" + id + ";");


                db.execSQL("INSERT INTO video(creationday,creationmonth,creationyear,lengthhours,lengthminutes,lengthseconds,edited,name,location,reference,secretnumber,description,reviews,lasteditday,lasteditmonth,lastedityear,path,userid) " +
                        "VALUES("+creationday+","+creationmonth+","+creationyear+","+hours+","+minutes+","+seconds+","+0+",'"+videoname.getText().toString()+"','"+location.getText().toString()+"','"+reference.getText().toString()+"',"+Integer.parseInt(secretnumber.getText().toString())+",'"+description.getText().toString()+"',"+0+","+null+","+null+","+null+",'"+path+"user"+userid+videoname.getText().toString()+i1+"."+extension+"',"+userid+");" );
                //************************************************
                Intent intent= new Intent(NewVideoActivity.this,ProfileActivity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);

                System.out.println("videopath=" + destinationImagePath + " for user of id=" + userid);
                db.close();
                //fullwidthpicture.setBackground(getResources().getDrawable(R.drawable.profilepart2trans));
                //fullwidthpicture.setImageResource(R.drawable.profilepart2trans);
                // /mnt/sdcard/Thales/THALES-ControllerHMI/content/img/tools.png
            }
            else{
                Toast.makeText(this,"You haven't picked Video",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show();
        }
    }



    private void SetupVariables()
    {
        videoname = (EditText)findViewById(R.id.nameedittext);
        location = (EditText)findViewById(R.id.locationedittext);
        reference = (EditText)findViewById(R.id.referenceedittext);
        secretnumber=(EditText)findViewById(R.id.secretnumberedittext);
        description = (EditText)findViewById(R.id.descriptionedittext);
        datePicker = (DatePicker)findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());
        startrec = (Button)findViewById(R.id.startrecordingbutton);
        uploadvid = (Button)findViewById(R.id.uploadvideonew);

        helpicon = (ImageView) findViewById(R.id.searchimageprofile2);
    }
}
