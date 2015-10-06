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
public class VideosFragement extends Fragment {

    SQLiteDatabase db;


    private Integer identification;
    private Integer userid;
    private String name;
    private String location;
    private String date;
    private String length;
    private String controllername;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){


        db = getActivity().openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);
        Cursor c1 = db.rawQuery("SELECT * FROM video", null);
        int numbervideos = c1.getCount();
        int counter=0;
        //ListView list;
        Integer[] identification=new Integer[numbervideos];
        Integer[] userid=new Integer[numbervideos];
        String[] name=new String[numbervideos];
        String[] location=new String[numbervideos];
        String[] date=new String[numbervideos];
        String[] length=new String[numbervideos];
        String[] controllername=new String[numbervideos];

        while(c1.moveToNext())
        {
            identification[counter]=c1.getInt(0);
            userid[counter]=c1.getInt(18);
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

            Cursor c = db.rawQuery("SELECT * FROM user WHERE id="+c1.getInt(18), null);
            if(c.moveToNext()){
                controllername[counter]=c.getString(3);
            }
            c.close();


            counter++;
        }
        c1.close();


        //***********freshly added code ************************
        ListView list;
        AdminVideoListAdapter adapter;
        ArrayList<Video> arrayList = new ArrayList<Video>();

        View rootView = inflater.inflate(R.layout.fragment_videos, container, false);


        list =(ListView)rootView.findViewById(R.id.listViewfragmentvideos);
        for(int i=0;i<numbervideos;i++){
            Video u = new Video(identification[i],userid[i],name[i],location[i],date[i],length[i],controllername[i]);
            arrayList.add(u);
        }

        adapter =new AdminVideoListAdapter(getActivity(),arrayList);
        list.setAdapter(adapter);

        return rootView;
    }
}
