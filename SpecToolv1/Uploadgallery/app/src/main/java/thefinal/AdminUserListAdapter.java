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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Neopterix on 01/09/15.
 */
public class AdminUserListAdapter extends BaseAdapter {
    //Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<User> userList = null;
    private ArrayList<User> arraylist;

    SQLiteDatabase db;

    public AdminUserListAdapter(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<User>();
        this.arraylist.addAll(userList);

    }

    public class ViewHolder{
        TextView name;
        TextView occupation;
        ImageView delete;
    }


    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
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
            inflater.inflate(R.layout.adminuseraccountslist_single, null, true);
            view = inflater.inflate(R.layout.adminuseraccountslist_single,null);

            holder.name = (TextView) view.findViewById(R.id.listadminusernametext);
            holder.occupation = (TextView) view.findViewById(R.id.listadminoccupationtext);
            holder.delete = (ImageView) view.findViewById(R.id.adminremoveuserimage);

            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(userList.get(position).getName());
        holder.occupation.setText(userList.get(position).getOccupation());
        holder.delete.setImageResource(R.drawable.delete);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBaseDisplayer d=new DataBaseDisplayer(mContext);
                d.showalltables();
                //Toast.makeText(mContext, "You Clicked at " + userList.get(position).getName() + " of id = " + userList.get(position).getId(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Delete");
                builder.setMessage("Would you like to Delete "+ userList.get(position).getName() + " of id = " + userList.get(position).getId()+" ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db = mContext.openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE, null);
                        Cursor c1 = db.rawQuery("SELECT * FROM video where userid=" + userList.get(position).getId(), null);
                        while (c1.moveToNext()) {
                            db.execSQL("DELETE FROM circle WHERE videoid="+c1.getInt(0)+";");
                            db.execSQL("DELETE FROM comment WHERE videoid="+c1.getInt(0)+";");
                            db.execSQL("DELETE FROM video WHERE id=" + c1.getInt(0) + ";");
                        }
                        c1.close();


                        db.execSQL("DELETE FROM user WHERE id=" + userList.get(position).getId() + ";");


                        DataBaseDisplayer d=new DataBaseDisplayer(mContext);
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
                        Toast.makeText(mContext, "Delete successful", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(mContext, "Delete canceled", Toast.LENGTH_SHORT).show();
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
        userList.clear();
        if(charText.length()==0){
            userList.addAll(arraylist);
        } else {
            for(User wp:arraylist){
                if(wp.getName().toLowerCase(Locale.getDefault()).contains(charText)){
                    userList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}

