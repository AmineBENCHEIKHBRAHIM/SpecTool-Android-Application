package thefinal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.neopterix.uploadgallery.R;

/**
 * Created by Neopterix on 01/09/15.
 */
public class AdminSettingsFragement extends Fragment {

    EditText logintext=null;
    EditText passwordtext=null;


    Button updatebutton=null;
    Button cancelbutton=null;

    SQLiteDatabase db;
    Integer id=null;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_adminsettings,container,false);
        db = getActivity().openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE, null);

        logintext = (EditText) rootView.findViewById(R.id.logineditTextadminsettings);
        passwordtext = (EditText) rootView.findViewById(R.id.passwordeditTextadminsettings);

        updatebutton = (Button) rootView.findViewById(R.id.updatebuttonadminsettings);

        Cursor c = db.rawQuery("SELECT * FROM admin", null);
        if(c.moveToNext())
        {
            id=c.getInt(0);
            logintext.setText(c.getString(1));
            passwordtext.setText(c.getString(2));
        }
        c.close();

        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.execSQL("UPDATE admin SET login='" + logintext.getText().toString() + "',password='" + passwordtext.getText().toString() +
                        "' WHERE id=" + id + ";");
                Toast.makeText(getActivity(), "Admin Settings Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
}
