package thefinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neopterix.uploadgallery.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Neopterix on 01/09/15.
 */
public class AdminVideoListAdapter extends BaseAdapter {
    //Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Video> videoList = null;
    private ArrayList<Video> arraylist;

    SQLiteDatabase db;

    public AdminVideoListAdapter(Context mContext, List<Video> videoList) {
        this.mContext = mContext;
        this.videoList = videoList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Video>();
        this.arraylist.addAll(videoList);

    }

    public class ViewHolder{
        TextView name;
        TextView controllername;
        TextView location;
        TextView date;
        TextView length;
        ImageView delete;

    }


    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if(view==null){
            holder = new ViewHolder();
            inflater.inflate(R.layout.adminvideolist_single, null, true);
            view = inflater.inflate(R.layout.adminvideolist_single,null);

            holder.name = (TextView) view.findViewById(R.id.listvideonametextadmin);
            holder.controllername = (TextView) view.findViewById(R.id.adminvideolistcontrollername);
            holder.location = (TextView) view.findViewById(R.id.listlocaltextadmin);
            holder.date = (TextView) view.findViewById(R.id.listdatetextadmin);
            holder.length = (TextView) view.findViewById(R.id.listlengthtextadminvideo);
            holder.delete = (ImageView) view.findViewById(R.id.adminremovevideoimage);

            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(videoList.get(position).getName());
        holder.controllername.setText(videoList.get(position).getControllername());
        holder.location.setText(videoList.get(position).getLocation());
        holder.date.setText(videoList.get(position).getDate());
        holder.length.setText(videoList.get(position).getLength());
        holder.delete.setImageResource(R.drawable.delete);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(mContext, "You Clicked at " + userList.get(position).getName() + " of id = " + userList.get(position).getId(), Toast.LENGTH_SHORT).show();

                DataBaseDisplayer d=new DataBaseDisplayer(mContext);
                d.showalltables();
                Toast.makeText(mContext, "clicked on icon", Toast.LENGTH_SHORT).show();
                System.out.println("clicked on icon");

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Delete");
                builder.setMessage("Would you like to Delete " + videoList.get(position).getName() + " of id = " + videoList.get(position).getIdentification() + " ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db = mContext.openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE, null);
                        db.execSQL("DELETE FROM circle WHERE videoid=" + videoList.get(position).getIdentification() + ";");
                        db.execSQL("DELETE FROM comment WHERE videoid=" + videoList.get(position).getIdentification() + ";");
                        Cursor c1 = db.rawQuery("SELECT * FROM video WHERE id=" + videoList.get(position).getIdentification(), null);
                        if (c1.moveToNext()) {
                            File file = new File(c1.getString(17));
                            boolean deleted = false;
                            deleted = file.delete();
                            if(deleted) System.out.println(c1.getString(17)+"*******is deleted");
                            else System.out.println(c1.getString(17)+"*******deletion failure");
                        }
                        c1.close();
                        db.execSQL("DELETE FROM video WHERE id=" + videoList.get(position).getIdentification() + ";");

                        DataBaseDisplayer d = new DataBaseDisplayer(mContext);
                        d.showalltables();

                        //arraylist.remove(userList.get(position));
                        //notifyDataSetChanged();
                        ((Activity) mContext).finish();
                        Intent intent = new Intent(mContext, AdminProfileActivity.class);
                        mContext.startActivity(intent);
                        /*
                        Intent intent = new Intent(VideoActivity2.this, ProfileActivity.class);
                        intent.putExtra("userid", userid);
                        reviewsnumber++;
                        db.execSQL("UPDATE video SET reviews="+reviewsnumber+" WHERE id="+videoid+";");
                        System.out.println("**********this is the intent am passing : "+userid+"*********");
                        startActivity(intent);*/
                        Toast.makeText(mContext, "Video Delete successful", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*
                        System.out.println("*****Database content for circles and comments*****");
                        c4 = db.rawQuery("SELECT * FROM circle where videoid=" + videoid, null);
                        while (c4.moveToNext()) {
                            System.out.println("circleid=" + c4.getInt(0) + ",appeartime=" + c4.getInt(1) + ",desappeartime=" + c4.getInt(2) + ",leftmargin=" + c4.getInt(3) +
                                    ",rightmargin=" + c4.getInt(4) + ",topmargin=" + c4.getInt(5) + ",bottommargin=" + c4.getInt(6) + ",height=" + c4.getInt(7) + ",width=" + c4.getInt(8) + ",videoid=" + c4.getInt(9));
                        }
                        c4.close();
                        c5 = db.rawQuery("SELECT * FROM comment where videoid=" + videoid, null);
                        while (c5.moveToNext()) {
                            System.out.println("commentid=" + c5.getInt(0) + ",appeartime=" + c5.getInt(1) + ",desappeartime=" + c5.getInt(2) + ",text=" + c5.getString(3) + ",leftmargin=" + c5.getInt(4) +
                                    ",rightmargin=" + c5.getInt(5) + ",topmargin=" + c5.getInt(6) + ",bottommargin=" + c5.getInt(7) + ",videoid=" + c5.getInt(8));
                        }
                        c5.close();
                        dialog.cancel();*/
                        Toast.makeText(mContext, "Video Delete canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                /*Intent intent = new Intent(mContext, VideoPresentationActivity.class);

                intent.putExtra("identification", worldPopulationList.get(position).getIdentification());
                intent.putExtra("date", worldPopulationList.get(position).getDate());
                intent.putExtra("length", worldPopulationList.get(position).getLength());

                mContext.startActivity(intent);*/
            }
        });



        return view;
    }

    //filter class
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        videoList.clear();
        if(charText.length()==0){
            videoList.addAll(arraylist);
        } else {
            for(Video wp:arraylist){
                if(wp.getName().toLowerCase(Locale.getDefault()).contains(charText)){
                    videoList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}

