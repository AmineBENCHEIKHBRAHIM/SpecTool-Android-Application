package thefinal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.neopterix.uploadgallery.R;

import java.io.File;


public class MainActivity extends Activity {
    EditText login=null;
    EditText password=null;
    Button log= null;
    SQLiteDatabase db;

    private String path= Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappvids/";
    boolean success = false;

    boolean applicationdirectorycreated=false;
    boolean applicationvidsdirectorycreated=false;
    boolean applicationprofilepicturesdirectorycreated=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitialiseApplicationDirectory();
        Initilisevidsdirectory();
        Initialiseprofilepicturesdirectory();

        SetupVariables();
        InitialiseDatabase();
        System.out.println(Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappvids/");
        //**************************************************************************************************
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);
                if (login.getText().toString().trim().length() == 0 || password.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Enter all values", Toast.LENGTH_LONG).show();
                } else {
                    Cursor c = db.rawQuery("SELECT * FROM user where login='" + login.getText() + "' and password='" + password.getText() + "'", null);
                    if (c.moveToNext()) {
                        Toast.makeText(MainActivity.this, "Access granted", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        intent.putExtra("userid", c.getInt(0));
                        startActivity(intent);
                    } else {
                        Cursor c1=db.rawQuery("SELECT * FROM admin where login='" + login.getText() + "' and password='" + password.getText() + "'", null);
                        if (c1.moveToNext()) {
                            Toast.makeText(MainActivity.this, "Access granted For Admin", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, AdminProfileActivity.class);
                            //intent.putExtra("userid", c.getInt(0));
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Wrong login and password", Toast.LENGTH_LONG).show();
                        }
                        c1.close();
                    }
                    c.close();
                }
            }
        });
        //**************************************************************************************************************
        /*
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login.getText().toString().trim().length() == 0 || password.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Enter all values", Toast.LENGTH_LONG).show();
                } else {
                    Cursor c = db.rawQuery("SELECT * FROM user where login='" + login.getText() + "' and password='" + password.getText() + "'", null);
                    if (c.moveToNext()) {
                        Toast.makeText(MainActivity.this, "Access granted", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        intent.putExtra("userid", c.getInt(0));
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong login and password", Toast.LENGTH_LONG).show();
                    }
                    c.close();
                }
            }
        });
         */

    }

    private void Initialiseprofilepicturesdirectory() {

        success=false;
        File folder3 = new File(Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappprofilepictures");
        if(!folder3.exists()){
            success = folder3.mkdir();
        }
        else{System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappprofilepictures" + " - Already exists");
            applicationprofilepicturesdirectorycreated=true;}
        if(success)
        {
            System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappprofilepictures" + " - Created with success");
            applicationprofilepicturesdirectorycreated=true;
        }
        else{
            System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappprofilepictures/" + " - Creation failure");
        }
    }

    private void Initilisevidsdirectory() {
        success=false;
        File folder2 = new File(Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappvids");
        if(!folder2.exists()){
            success = folder2.mkdir();
        }
        else{System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappvids" + " - Already exists");
            applicationvidsdirectorycreated=true;}
        if(success)
        {
            System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappvids" + " - Created with success");
            applicationvidsdirectorycreated=true;
        }
        else{
            System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository/newspecappvids/" + " - Creation failure");
        }

    }

    private void InitialiseApplicationDirectory() {
        success=false;
        File folder = new File(Environment.getExternalStorageDirectory()+"/newspecapprepository");
        if(!folder.exists()){
            success = folder.mkdir();
        }
        else{System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository" + " - Already exists");
        applicationdirectorycreated=true;}
        if(success)
        {
            System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository" + " - Created with success");
            applicationdirectorycreated=true;
        }
        else{
            System.out.println("folder : " + Environment.getExternalStorageDirectory()+"/newspecapprepository" + " - Creation failure");
        }

    }


    private void SetupVariables() {
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        log = (Button) findViewById(R.id.loginBtn);
    }


    private void InitialiseDatabase() {
        db = openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);

        //db.execSQL("DROP TABLE comment");
        //db.execSQL("DROP TABLE circle");

        //db.execSQL("DROP TABLE video");
        //db.execSQL("DROP TABLE user");

        //db.execSQL("DROP TABLE admin");

        db.execSQL("CREATE TABLE IF NOT EXISTS user(id INTEGER PRIMARY KEY AUTOINCREMENT,login TEXT,password TEXT,name TEXT,occupation TEXT,picturepath TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS admin(id INTEGER PRIMARY KEY AUTOINCREMENT,login TEXT,password TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS video(id INTEGER PRIMARY KEY AUTOINCREMENT,creationday INTEGER,creationmonth INTEGER," +
                "creationyear INTEGER,lengthhours INTEGER,lengthminutes INTEGER,lengthseconds INTEGER,edited INTEGER,name TEXT,location TEXT,reference TEXT,secretnumber INTEGER," +
                "description TEXT,reviews INTEGER,lasteditday INTEGER,lasteditmonth INTEGER,lastedityear INTEGER,path TEXT,userid INTEGER,FOREIGN KEY(userid) REFERENCES user(id)); ");


        db.execSQL("CREATE TABLE IF NOT EXISTS circle(id INTEGER PRIMARY KEY AUTOINCREMENT,appeartime INTEGER,desappeartime INTEGER,leftmargin INTEGER,rightmargin INTEGER,topmargin INTEGER,bottommargin INTEGER," +
                "height INTEGER,width INTEGER,videoid INTEGER,FOREIGN KEY(videoid) REFERENCES video(id));");

        db.execSQL("CREATE TABLE IF NOT EXISTS comment(id INTEGER PRIMARY KEY AUTOINCREMENT,appeartime INTEGER,desappeartime INTEGER,text TEXT,leftmargin INTEGER,rightmargin INTEGER,topmargin INTEGER,bottommargin INTEGER," +
                "videoid INTEGER,FOREIGN KEY(videoid) REFERENCES video(id));");

        //db.execSQL("INSERT INTO user(login,password,name,occupation) VALUES('" + "admin" + "','" + "admin" + "','Samantha Ayrton','Senior Airport Controller');");

        Cursor c1=db.rawQuery("SELECT * FROM admin", null);
        if(c1.getCount()==0)
        {
            db.execSQL("INSERT INTO admin(login,password) VALUES('" + "neopterix" + "','" + "batman" + "');");
            System.out.println("new admin inserted");
        }
        c1.close();
        //db.execSQL("INSERT INTO admin(login,password) VALUES('" + "neopterix" + "','" + "batman" + "');");

        db.close();
    }
}
