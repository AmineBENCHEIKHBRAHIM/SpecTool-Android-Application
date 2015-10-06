package thefinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neopterix.uploadgallery.R;

/**
 * Created by Neopterix on 31/08/15.
 */
public class ProfileSettings extends AppCompatActivity {

    EditText logintext=null;
    EditText passwordtext=null;
    EditText nametext=null;
    EditText occupationtext=null;

    Button updatebutton=null;
    Button cancelbutton=null;

    ImageView helpicon = null;

    SQLiteDatabase db;
    Integer id=null;

@Override
    public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profilesettings);
    SetupVariables();

    Intent intent = getIntent();
    id = intent.getIntExtra("userid", -1);
    //Toast.makeText(ProfileSettings.this,"the id = "+id,Toast.LENGTH_LONG).show();

    db = openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE, null);
    Cursor c = db.rawQuery("SELECT * FROM user where id=" + id, null);
    if(c.moveToNext())
    {
        logintext.setText(c.getString(1));
        passwordtext.setText(c.getString(2));
        nametext.setText(c.getString(3));
        occupationtext.setText(c.getString(4));
    }
    c.close();

    updatebutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (logintext.getText().toString().trim().length() == 0 || passwordtext.getText().toString().trim().length() == 0 || nametext.getText().toString().trim().length() == 0
                    || occupationtext.getText().toString().trim().length() == 0) {
                Toast.makeText(ProfileSettings.this, "You should enter all values", Toast.LENGTH_LONG).show();
            } else {
                db.execSQL("UPDATE user SET login='" + logintext.getText().toString() + "',password='" + passwordtext.getText().toString() + "',name='" + nametext.getText().toString() + "',occupation='" + occupationtext.getText().toString() +
                        "' WHERE id=" + id + ";");
                Intent intent = new Intent(ProfileSettings.this, ProfileActivity.class);
                intent.putExtra("userid", id);
                startActivity(intent);
            }
        }
    });

    cancelbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ProfileSettings.this,ProfileActivity.class);
            intent.putExtra("userid",id);
            startActivity(intent);
        }
    });

    helpicon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSettings.this);
            final View recipientLayout = getLayoutInflater().inflate(R.layout.helpdialoglayout,null);
            final TextView recipientTextView = (TextView) recipientLayout.findViewById(R.id.profilehelptext);
            /*recipientTextView.setText("This is the profile settings page :\nYou should fill all fields below :\n\n  1.Login:\nEnter your new login.\n\n" +
                    "  2.Password:\nEnter your new password.\n\n"+"  3.Name:\nEnter your name.\n\n"+"  4.Occupation:\nEnter your occupation.e.g, Senior Airport Controller.");
                    */
            recipientTextView.setText(getResources().getString(R.string.profile_settings_helptext));
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

    private void SetupVariables() {
        logintext = (EditText) findViewById(R.id.logineditTextsettings);
        passwordtext = (EditText) findViewById(R.id.passwordeditTextsettings);
        nametext = (EditText) findViewById(R.id.namededitTextsettings);
        occupationtext = (EditText) findViewById(R.id.occupationeditTextsettings);
        updatebutton = (Button) findViewById(R.id.updatebuttonsettings);
        cancelbutton = (Button) findViewById(R.id.cancelbuttonsettings);

        helpicon = (ImageView) findViewById(R.id.searchimageprofile2);

    }

}
