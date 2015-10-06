package thefinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neopterix.uploadgallery.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Neopterix on 19/08/15.
 */
public class ProfileActivity extends Activity {

    private static int RESULT_LOAD_IMG=1;
    String imgDecodableString;


    private String path= Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappprofilepictures/";
    boolean success = false;

    TextView newvideo = null;
    TextView settings = null;
    TextView controllername = null;
    TextView controlleroccupation = null;
    EditText inputSearch=null;
    ImageView logout=null;

    ImageView helpicon = null;

    ImageView fullwidthpicture=null;
    ImageView profilepicture=null;
    SQLiteDatabase db;

    //ListView list;
    String[] name;
    String[] location;
    String[] date;
    String[] length;
    Integer[] edited;
    Integer[] identification;

    //***********freshly added code ************************
    ListView list;
    ListViewAdapter adapter;
    ArrayList<WorldPopulation> arrayList = new ArrayList<WorldPopulation>();


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Integer id=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //TextView newvideo = (TextView) findViewById(R.id.newvid);
        //TextView choicevid = (TextView) findViewById(R.id.choicevid);
        SetupVariables();
        final Intent intent = getIntent();
        id = intent.getIntExtra("userid",-1);


        System.out.println("**************this is the id of the user : "+id);
        //************Shared Preferences for Session control remembering ***************


        //pref=getApplicationContext().getSharedPreferences("MyPref", 0);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        editor.putInt("userid",id);
        editor.commit();


        //******************************************************************************
        System.out.println("this is the id : "+id);
        db = openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);
        Cursor c = db.rawQuery("SELECT * FROM user where id="+id, null);
        if(c.moveToNext())
        {
            controllername.setText(c.getString(3));
            controlleroccupation.setText(c.getString(4));
            if(c.getString(5)!=null)
            {
                fullwidthpicture.setBackgroundResource(R.drawable.profilepart2trans);
                profilepicture.setImageBitmap(BitmapFactory.decodeFile(c.getString(5)));
            }
        }
        c.close();
        Cursor c1 = db.rawQuery("SELECT * FROM video where userid=" + id, null);
        int numbervideos = c1.getCount();
        int counter=0;
        identification = new Integer[numbervideos];
        name = new String[numbervideos];
        location = new String[numbervideos];
        date = new String[numbervideos];
        length = new String[numbervideos];
        edited = new Integer[numbervideos];

        while(c1.moveToNext())
        {
            identification[counter]=c1.getInt(0);
            name[counter]=c1.getString(8);
            location[counter]=c1.getString(9);
            if(c1.getInt(1)<10 && c1.getInt(2)>=10) {
                date[counter] = "0"+c1.getInt(1) + " - " + c1.getInt(2) + " - " + c1.getInt(3);
            } else if(c1.getInt(1)>=10 && c1.getInt(2)<10){
                date[counter] = c1.getInt(1) + " - 0" + c1.getInt(2) + " - " + c1.getInt(3);
            } else if(c1.getInt(1)<10 && c1.getInt(2)<10){
                date[counter] = "0"+c1.getInt(1) + " - 0" + c1.getInt(2) + " - " + c1.getInt(3);
            } else {
                date[counter] = c1.getInt(1) + " - " + c1.getInt(2) + " - " + c1.getInt(3);
            }

            if(c1.getInt(5)<10 && c1.getInt(6)>=10) {
                length[counter] = c1.getInt(4) + ":0" + c1.getInt(5) + ":" + c1.getInt(6);
            } else if(c1.getInt(5)>=10 && c1.getInt(6)<10){
                length[counter] = c1.getInt(4) + ":" + c1.getInt(5) + ":0" + c1.getInt(6);
            } else if(c1.getInt(5)<10 && c1.getInt(6)<10){
                length[counter] = c1.getInt(4) + ":0" + c1.getInt(5) + ":0" + c1.getInt(6);
            } else {
                length[counter] = c1.getInt(4) + ":" + c1.getInt(5) + ":" + c1.getInt(6);
            }

            edited[counter]=c1.getInt(7);


            counter++;



            System.out.println("id = "+c1.getInt(0));
            System.out.println("creation day = "+c1.getInt(1));
            System.out.println("creation month = "+c1.getInt(2));
            System.out.println("creation year = "+c1.getInt(3));
            System.out.println("length hours = "+c1.getInt(4));
            System.out.println("length minutes = "+c1.getInt(5));
            System.out.println("length seconds = "+c1.getInt(6));
            System.out.println("edited = "+c1.getInt(7));
            System.out.println("videoname= "+c1.getString(8));
            System.out.println("location= "+c1.getString(9));
            System.out.println("reference= "+c1.getString(10));
            System.out.println("secret number = "+c1.getInt(11));
            System.out.println("description= "+c1.getString(12));
            System.out.println("reviews number = "+c1.getInt(13));
            System.out.println("last edit day = "+c1.getInt(14));
            System.out.println("last edit month = "+c1.getInt(15));
            System.out.println("last edit year = "+c1.getInt(16));
            System.out.println("path= "+c1.getString(17));
            System.out.println("userid = "+c1.getInt(18));
        }
        c1.close();

        newvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newvid = new Intent(ProfileActivity.this, NewVideoActivity.class);
                newvid.putExtra("userid", id);
                startActivity(newvid);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profilesettings = new Intent(ProfileActivity.this, ProfileSettings.class);
                profilesettings.putExtra("userid", id);
                startActivity(profilesettings);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loggingout = new Intent(ProfileActivity.this,MainActivity.class);
                loggingout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loggingout);
                //finish();
            }
        });

        helpicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                final View recipientLayout = getLayoutInflater().inflate(R.layout.helpdialoglayout,null);
                final TextView recipientTextView = (TextView) recipientLayout.findViewById(R.id.profilehelptext);
                /*recipientTextView.setText("This is your profile page :\nYou can perform multiple actions here :\n\n  1.Changing your profile picture:\nBy clicking on your profile picture, " +
                        "a new window will pop up where you can select the profile picture you want to display from the gallery.\n\n  2.Creating a new video:\nIn order to create a new video, simply click " +
                        "on the NEW VIDEO button.\n\n  3.Changing your profile settings:\nIf you would like to change your profile settings such as your login, your password, your name or your occupation, " +
                        "simply click on the settings button.\n\n  4.Searching and displaying a previously created video:\nIn order to search a video by name, simply make a single click on the input where " +
                        "Search videos.. is displayed. Once you find the video you're searching for, a simple click will display it. The information displayed for each video are in order : Video name, localisation, creation date and video length.\n\n" +
                        "  5.Displaying the help menu:\nIn order to display the help menu, simply click on the blue button on the up right.\n\n  6.Logging out:\nIn order to log out, simply click on the blue button " +
                        "on the up left.\n");
                */
                recipientTextView.setText(getResources().getString(R.string.activity_profile_helptext));
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

        //*****************List View Manipulation ****************************************
        /*final CustomList adapter = new CustomList(ProfileActivity.this,name,location,date,length,edited);
        list = (ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);
        list.setTextFilterEnabled(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ProfileActivity.this,"You Clicked at"+name[+position],Toast.LENGTH_SHORT).show();
            }
        });


        //*********************************************************************************

        //**************************filtering on search *********************
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

*/
        //*****************************************************************************

        list = (ListView) findViewById(R.id.listView);
        for(int i=0;i<name.length;i++)
        {
            WorldPopulation wp = new WorldPopulation(identification[i],name[i],location[i],date[i],length[i],edited[i]);
            arrayList.add(wp);
        }
        adapter = new ListViewAdapter(this,arrayList);
        list.setAdapter(adapter);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }
        });
        /*
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ProfileActivity.this, "You Clicked at " + name[+position] +" of id = "+identification[+position], Toast.LENGTH_SHORT).show();
            }
        });
*/


    }

    public void LoadImageFromGallery(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode==RESULT_LOAD_IMG && resultCode==RESULT_OK && null!=data){
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                System.out.println("imgDecodableString ="+imgDecodableString);
                cursor.close();
                //ImageView imgView = (ImageView) findViewById(R.id.imgView);
                //imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                fullwidthpicture.setBackgroundResource(R.drawable.profilepart2trans);
                profilepicture.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                //************ Creation of a folder for the profile pictures******************************

                /*
                File folder = new File(Environment.getExternalStorageDirectory()+"/newspecapprepository");
                if(!folder.exists()){
                    success = folder.mkdir();
                }
                if(success)
                {
                    System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository" + " - Created with success");
                    File folder2 = new File(Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappprofilepictures");
                    if(!folder2.exists()){
                        success = folder2.mkdir();
                    }
                    if(success)
                    {
                        System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappprofilepictures" + " - Created with success");
                    }
                    else{
                        System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappprofilepictures/" + " - Creation failure");
                    }
                }
                else{
                    System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository" + " - Creation failure");
                }
                */

                String destinationImagePath=null;

                try{
                    File sd = Environment.getExternalStorageDirectory();
                    File data2 = Environment.getDataDirectory();
                    if(sd.canWrite()){
                        destinationImagePath=path+"user"+id+".png";
                        File source = new File(imgDecodableString);
                        File destination = new File(destinationImagePath);
                        if(source.exists()){
                            FileChannel src = new FileInputStream(source).getChannel();
                            FileChannel dst = new FileOutputStream(destination).getChannel();
                            dst.transferFrom(src,0,src.size());
                            src.close();
                            dst.close();
                        }
                        else{Toast.makeText(this,"file non existing",Toast.LENGTH_LONG).show();}
                    }
                } catch (Exception e){}


                //*****************************************************************************************
                db = openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);
                db.execSQL("UPDATE user SET picturepath='" + destinationImagePath + "' WHERE id=" + id + ";");

                System.out.println("picturepath=" + destinationImagePath + " for user of id=" + id);
                db.close();
                //fullwidthpicture.setBackground(getResources().getDrawable(R.drawable.profilepart2trans));
                //fullwidthpicture.setImageResource(R.drawable.profilepart2trans);
                // /mnt/sdcard/Thales/THALES-ControllerHMI/content/img/tools.png
            }
            else{
                Toast.makeText(this,"You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show();
        }
    }


    private void SetupVariables()
    {
        newvideo = (TextView)findViewById(R.id.newvideo);
        settings = (TextView)findViewById(R.id.settings);
        controllername = (TextView)findViewById(R.id.controllername);
        controlleroccupation = (TextView)findViewById(R.id.occupation);
        inputSearch = (EditText)findViewById(R.id.inputSearch);
        logout = (ImageView)findViewById(R.id.threelinesprofile2);
        fullwidthpicture = (ImageView) findViewById(R.id.fullwidthprofileactivityimage);
        profilepicture = (ImageView) findViewById(R.id.imgView);
        helpicon = (ImageView) findViewById(R.id.searchimageprofile2);
    }
}
