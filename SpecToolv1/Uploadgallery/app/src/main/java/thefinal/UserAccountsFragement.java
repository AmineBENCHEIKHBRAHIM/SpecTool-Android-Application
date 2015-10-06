package thefinal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.neopterix.uploadgallery.R;

import java.util.ArrayList;

/**
 * Created by Neopterix on 01/09/15.
 */
public class UserAccountsFragement extends Fragment {

    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        /*Button myButton = new Button(getActivity());
        myButton.setText("press me");

        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);*/


        db = getActivity().openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);
        Cursor c = db.rawQuery("SELECT * FROM user", null);
        int numberusers = c.getCount();
        int counter=0;
        //ListView list;
        String[] name = new String[numberusers];
        String[] occupation=new String[numberusers];
        Integer[] id=new Integer[numberusers];
        while (c.moveToNext())
        {
            id[counter]=c.getInt(0);
            name[counter] = c.getString(3);
            occupation[counter] = c.getString(4);
            counter++;
        }
        c.close();



        //***********freshly added code ************************
        ListView list;
        AdminUserListAdapter adapter;
        ArrayList<User> arrayList = new ArrayList<User>();

/*
        name[0]="a1";name[1]="a2";name[2]="a3";
        occupation[0]="o1";occupation[1]="o2";occupation[2]="o3";
        id[0]=1;id[1]=2;id[2]=3;
*/

        View rootView = inflater.inflate(R.layout.fragment_useraccounts,container,false);

        list =(ListView)rootView.findViewById(R.id.listViewfragmentuseraccounts);
        for(int i=0;i<numberusers;i++){
            User u = new User(id[i],name[i],occupation[i]);
            arrayList.add(u);
        }

        adapter =new AdminUserListAdapter(getActivity(),arrayList);
        list.setAdapter(adapter);


        /*RelativeLayout myLayout=(RelativeLayout)rootView.findViewById(R.id.fragment_useraccountsmainLayout);
        myLayout.addView(myButton,buttonParams);*/
        return rootView;
    }
}
