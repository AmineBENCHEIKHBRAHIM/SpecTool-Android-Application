package thefinal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Neopterix on 02/09/15.
 */
public class DataBaseDisplayer {
    Context mContext;

    public DataBaseDisplayer(Context mContext) {
        this.mContext = mContext;
    }


    public void displayUsers()
    {
        SQLiteDatabase db;
        db = mContext.openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);
        System.out.println("///////////////////The Database table 'user' content/////////////////////////");
        Cursor c = db.rawQuery("SELECT * FROM user", null);
        while(c.moveToNext())
        {
            System.out.println("id="+c.getInt(0)+"-"+"login="+c.getString(1)+"-"+"password="+c.getString(2)+"-"+"name="+c.getString(3)+"-"+"occupation="+c.getString(4));
        }
        c.close();
    }

    public void displayAdmins()
    {
        SQLiteDatabase db;
        db = mContext.openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);
        System.out.println("///////////////////The Database table 'admin' content/////////////////////////");
        Cursor c = db.rawQuery("SELECT * FROM admin", null);
        while(c.moveToNext())
        {
            System.out.println("id="+c.getInt(0)+"-"+"login="+c.getString(1)+"-"+"password="+c.getString(2));
        }
        c.close();
    }
    public void displayVideos()
    {
        SQLiteDatabase db;
        db = mContext.openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);
        System.out.println("///////////////////The Database table 'video' content/////////////////////////");
        Cursor c = db.rawQuery("SELECT * FROM video", null);
        while(c.moveToNext())
        {
            System.out.println("id="+c.getInt(0)+"-"+"creationday="+c.getInt(1)+"-"+"creationmonth="+c.getInt(2)+"-"+"creationyear="+c.getInt(3)+"-"+"lengthhours="+c.getInt(4)+"-"+"lengthminutes="+c.getInt(5)+"-"
                    +"lengthseconds="+c.getInt(6)+"-"+"edited="+c.getInt(7)+"-"+"name="+c.getString(8)+"-"+"location="+c.getString(9)+"-"+"reference="+c.getString(10)+"-"+"secretnumber="+c.getInt(11)+"-"+"description="+c.getString(12)
                    +"-"+"reviews="+c.getInt(13)+"-"+"lasteditday="+c.getInt(14)+"-"+"lasteditmonth="+c.getInt(15)+"-"+"lastedityear="+c.getInt(16)+"-"+"path="+c.getString(17)+"-"+"userid="+c.getInt(18));
        }
        c.close();
    }
    public void displayCircles()
    {
        SQLiteDatabase db;
        db = mContext.openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);
        System.out.println("///////////////////The Database table 'circle' content/////////////////////////");
        Cursor c = db.rawQuery("SELECT * FROM circle", null);
        while(c.moveToNext())
        {
            System.out.println("id="+c.getInt(0)+"-"+"appeartime="+c.getInt(1)+"-"+"desappeartime="+c.getInt(2)+"-"+"leftmargin="+c.getInt(3)+"-"+"rightmargin="+c.getInt(4)+"-"+"topmargin="+c.getInt(5)+"-"
                    +"bottommargin="+c.getInt(6)+"-"+"height="+c.getInt(7)+"-"+"width="+c.getInt(8)+"-"+"videoid="+c.getInt(9));
        }
        c.close();
    }
    public void displayComments()
    {
        SQLiteDatabase db;
        db = mContext.openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);
        System.out.println("///////////////////The Database table 'comment' content/////////////////////////");
        Cursor c = db.rawQuery("SELECT * FROM comment", null);
        while(c.moveToNext())
        {
            System.out.println("id="+c.getInt(0)+"-"+"appeartime="+c.getInt(1)+"-"+"desappeartime="+c.getInt(2)+"-"+"text="+c.getString(3)+"-"+"leftmargin="+c.getInt(4)+"-"+"rightmargin="+c.getInt(5)+"-"+"topmargin="+c.getInt(6)+"-"
                    +"bottommargin="+c.getInt(7)+"-"+"videoid="+c.getInt(8));
        }
        c.close();
    }

    public void showalltables()
    {
        SQLiteDatabase db;
        db = mContext.openOrCreateDatabase("SpecApplicationDB", Context.MODE_PRIVATE,null);
        System.out.println("///////////////////The Database table 'user' content/////////////////////////");
        Cursor c = db.rawQuery("SELECT * FROM user", null);
        while(c.moveToNext())
        {
            System.out.println("id="+c.getInt(0)+"-"+"login="+c.getString(1)+"-"+"password="+c.getString(2)+"-"+"name="+c.getString(3)+"-"+"occupation="+c.getString(4));
        }
        c.close();

        System.out.println("///////////////////The Database table 'admin' content/////////////////////////");
        c = db.rawQuery("SELECT * FROM admin", null);
        while(c.moveToNext())
        {
            System.out.println("id="+c.getInt(0)+"-"+"login="+c.getString(1)+"-"+"password="+c.getString(2));
        }
        c.close();

        System.out.println("///////////////////The Database table 'video' content/////////////////////////");
        c = db.rawQuery("SELECT * FROM video", null);
        while(c.moveToNext())
        {
            System.out.println("id="+c.getInt(0)+"-"+"creationday="+c.getInt(1)+"-"+"creationmonth="+c.getInt(2)+"-"+"creationyear="+c.getInt(3)+"-"+"lengthhours="+c.getInt(4)+"-"+"lengthminutes="+c.getInt(5)+"-"
                    +"lengthseconds="+c.getInt(6)+"-"+"edited="+c.getInt(7)+"-"+"name="+c.getString(8)+"-"+"location="+c.getString(9)+"-"+"reference="+c.getString(10)+"-"+"secretnumber="+c.getInt(11)+"-"+"description="+c.getString(12)
                    +"-"+"reviews="+c.getInt(13)+"-"+"lasteditday="+c.getInt(14)+"-"+"lasteditmonth="+c.getInt(15)+"-"+"lastedityear="+c.getInt(16)+"-"+"path="+c.getString(17)+"-"+"userid="+c.getInt(18));
        }
        c.close();

        System.out.println("///////////////////The Database table 'circle' content/////////////////////////");
        c = db.rawQuery("SELECT * FROM circle", null);
        while(c.moveToNext())
        {
            System.out.println("id="+c.getInt(0)+"-"+"appeartime="+c.getInt(1)+"-"+"desappeartime="+c.getInt(2)+"-"+"leftmargin="+c.getInt(3)+"-"+"rightmargin="+c.getInt(4)+"-"+"topmargin="+c.getInt(5)+"-"
                    +"bottommargin="+c.getInt(6)+"-"+"height="+c.getInt(7)+"-"+"width="+c.getInt(8)+"-"+"videoid="+c.getInt(9));
        }
        c.close();

        System.out.println("///////////////////The Database table 'comment' content/////////////////////////");
        c = db.rawQuery("SELECT * FROM comment", null);
        while(c.moveToNext())
        {
            System.out.println("id="+c.getInt(0)+"-"+"appeartime="+c.getInt(1)+"-"+"desappeartime="+c.getInt(2)+"-"+"text="+c.getString(3)+"-"+"leftmargin="+c.getInt(4)+"-"+"rightmargin="+c.getInt(5)+"-"+"topmargin="+c.getInt(6)+"-"
                    +"bottommargin="+c.getInt(7)+"-"+"videoid="+c.getInt(8));
        }
        c.close();
    }
}
