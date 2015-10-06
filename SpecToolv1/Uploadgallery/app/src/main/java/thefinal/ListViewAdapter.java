package thefinal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neopterix.uploadgallery.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Neopterix on 24/08/15.
 */
public class ListViewAdapter extends BaseAdapter{

    //Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<WorldPopulation> worldPopulationList = null;
    private ArrayList<WorldPopulation> arraylist;

    public ListViewAdapter(Context mContext, List<WorldPopulation> worldPopulationList) {
        this.mContext = mContext;
        this.worldPopulationList = worldPopulationList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<WorldPopulation>();
        this.arraylist.addAll(worldPopulationList);

    }

    public class ViewHolder{
        TextView name;
        TextView location;
        TextView date;
        TextView length;
        ImageView edited;
    }


    @Override
    public int getCount() {
        return worldPopulationList.size();
    }

    @Override
    public Object getItem(int position) {
        return worldPopulationList.get(position);
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
            inflater.inflate(R.layout.list_single, null, true);
            view = inflater.inflate(R.layout.list_single,null);

            holder.name = (TextView) view.findViewById(R.id.listvideonametext);
            holder.location = (TextView) view.findViewById(R.id.listlocaltext);
            holder.date = (TextView) view.findViewById(R.id.listdatetext);
            holder.length = (TextView) view.findViewById(R.id.listlengthtext);
            holder.edited = (ImageView) view.findViewById(R.id.listeditedimage);

            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(worldPopulationList.get(position).getName());
        holder.location.setText(worldPopulationList.get(position).getLocation());
        holder.date.setText(worldPopulationList.get(position).getDate());
        holder.length.setText(worldPopulationList.get(position).getLength());
        if(worldPopulationList.get(position).getEdited()==0) {
            holder.edited.setImageResource(R.drawable.nonedited);
        }
        else{
            holder.edited.setImageResource(R.drawable.edited);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(mContext,VideoPresentationActivity.class);
                intent.putExtra("identification",worldPopulationList.get(position).getIdentification());
                intent.putExtra("name",worldPopulationList.get(position).getName());
                intent.putExtra("location",worldPopulationList.get(position).getLocation());
                intent.putExtra("date",worldPopulationList.get(position).getLocation());
                intent.putExtra("length",worldPopulationList.get(position).getLength());
                intent.putExtra("edited",worldPopulationList.get(position).getEdited());
                mContext.startActivity(intent);*/
                //Toast.makeText(mContext,"You Clicked at " + worldPopulationList.get(position).getName() +" of id = "+worldPopulationList.get(position).getIdentification(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,VideoPresentationActivity.class);

                intent.putExtra("identification",worldPopulationList.get(position).getIdentification());
                intent.putExtra("date",worldPopulationList.get(position).getDate());
                intent.putExtra("length",worldPopulationList.get(position).getLength());

                mContext.startActivity(intent);
            }
        });



        return view;
    }

    //filter class
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        worldPopulationList.clear();
        if(charText.length()==0){
            worldPopulationList.addAll(arraylist);
        } else {
            for(WorldPopulation wp:arraylist){
                if(wp.getName().toLowerCase(Locale.getDefault()).contains(charText)){
                    worldPopulationList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
