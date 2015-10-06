package thefinal;

import android.content.Context;
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
 * Created by Neopterix on 02/09/15.
 */
public class NewUserFragement extends Fragment {
    EditText logintext=null;
    EditText passwordtext=null;
    EditText nametext=null;
    EditText occupationtext=null;

    Button updatebutton=null;

    SQLiteDatabase db;
    Integer id=null;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_newuser,container,false);

        db = getActivity().openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE, null);

            logintext = (EditText) rootView.findViewById(R.id.logineditTextadminnewuser);
            passwordtext = (EditText) rootView.findViewById(R.id.passwordeditTextadminnewuser);
            nametext = (EditText) rootView.findViewById(R.id.namededitTextadminnewuser);
            occupationtext = (EditText) rootView.findViewById(R.id.occupationeditTextadminnewuser);
            updatebutton = (Button) rootView.findViewById(R.id.updatebuttonadminnewuser);


        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logintext.getText().toString().trim().length() == 0 || passwordtext.getText().toString().trim().length() == 0 || nametext.getText().toString().trim().length() == 0
                        || occupationtext.getText().toString().trim().length() == 0 ) {
                    Toast.makeText(getActivity(), "Enter all values", Toast.LENGTH_SHORT).show();
                } else {
                    db.execSQL("INSERT INTO user(login,password,name,occupation) VALUES('" + logintext.getText().toString() + "','" + passwordtext.getText().toString() + "','"+nametext.getText().toString()+"','"+occupationtext.getText().toString()+"');");
                    Toast.makeText(getActivity(), "User added Successfully", Toast.LENGTH_SHORT).show();
                    logintext.setText("");
                    passwordtext.setText("");
                    nametext.setText("");
                    occupationtext.setText("");
                }
            }
        });




        return rootView;
    }


}
