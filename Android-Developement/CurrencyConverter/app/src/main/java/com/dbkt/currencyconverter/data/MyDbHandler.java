package com.dbkt.currencyconverter.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dbkt.currencyconverter.model.DbRow;
import com.dbkt.currencyconverter.params.Params;

import java.util.ArrayList;
import java.util.List;

public class MyDbHandler extends SQLiteOpenHelper {

    public MyDbHandler(Context context){
        super(context, Params.DB_NAME,null,Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE IF NOT EXISTS " +
                Params.TABLE_NAME + "("
                + Params.KEY_ID + " INTEGER PRIMARY KEY, "
                + Params.KEY_SHORT_NAME + " TEXT UNIQUE, "
                + Params.KEY_LONG_NAME + " TEXT UNIQUE, "
                + Params.KEY_RATES + " REAL, "
                + Params.KEY_FLAG_LOCATION + " TEXT DEFAULT '@drawable/default_flag.png', "
                + Params.KEY_BASE_CURRENCY + " TEXT DEFAULT 'EURO', "
                + Params.KEY_TIMESTAMP + " TEXT DEFAULT '0000-00-00'"
                + ")";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // create
    public void addRow(DbRow row){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Params.KEY_SHORT_NAME,row.getShort_name());
        values.put(Params.KEY_LONG_NAME,row.getLong_name());
        values.put(Params.KEY_RATES,row.getRates());

        if(row.getFlag_location()!=null)
            values.put(Params.KEY_FLAG_LOCATION,row.getFlag_location());

        if(row.getBase_currency()!=null)
            values.put(Params.KEY_BASE_CURRENCY,row.getBase_currency());

        if(row.getTimestamp()!=null)
            values.put(Params.KEY_TIMESTAMP,row.getTimestamp());

        db.insert(Params.TABLE_NAME,null,values);
        db.close();
    }

    // read
    public List<DbRow> getAllRows(){
        List<DbRow> row_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + Params.TABLE_NAME;
        Cursor cursor = db.rawQuery(select,null);

        if(cursor.moveToFirst()){
            do{
                DbRow row = new DbRow();
                row.setId(cursor.getInt(0));
                row.setShort_name(cursor.getString(1));
                row.setLong_name(cursor.getString(2));
                row.setRates((double) cursor.getFloat(3));
                row.setFlag_location(cursor.getString(4));
                row.setBase_currency(cursor.getString(5));
                row.setTimestamp(cursor.getString(6));

                row_list.add(row);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return row_list;
    }

    // update
    public void updateRow(DbRow row){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Params.KEY_SHORT_NAME,row.getShort_name());

        if(row.getLong_name()!=null)
            values.put(Params.KEY_LONG_NAME,row.getLong_name());

        values.put(Params.KEY_RATES,row.getRates());

        if(row.getFlag_location()!=null)
            values.put(Params.KEY_FLAG_LOCATION,row.getFlag_location());

        if(row.getBase_currency()!=null)
            values.put(Params.KEY_BASE_CURRENCY,row.getBase_currency());

        if(row.getTimestamp()!=null)
            values.put(Params.KEY_TIMESTAMP,row.getTimestamp());

        db.update(Params.TABLE_NAME,values,Params.KEY_SHORT_NAME +"=?"
                ,new String[]{row.getShort_name()});

        db.close();
    }

    public void update_rate_base_timeStamp(String short_name,double rate,String base,String timeStamp){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Params.KEY_SHORT_NAME,short_name);
        if(rate!=0)
            values.put(Params.KEY_RATES,rate);
        if(base!=null)
            values.put(Params.KEY_BASE_CURRENCY,base);
        if(timeStamp!=null)
            values.put(Params.KEY_TIMESTAMP,timeStamp);
        db.update(Params.TABLE_NAME,values,Params.KEY_SHORT_NAME+"=?",new String[]{short_name});
        db.close();
    }



    public void deleteRow(String KEY_SHORT_NAME){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Params.TABLE_NAME,Params.KEY_SHORT_NAME + "=?",new String[]{KEY_SHORT_NAME});
        db.close();
    }

    public void deleteTable(String TABLE_NAME){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
    }

    public int get_day_of_first_entry(){
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM " + Params.TABLE_NAME + " WHERE "+Params.KEY_SHORT_NAME+"='AFN'";
        Cursor cursor = db.rawQuery(select,null);
        int date = 0;
        if(cursor!=null &&cursor.moveToFirst()) {
            String cid = cursor.getString(6).split("-")[2];
            date = Integer.parseInt(cid);
            cursor.close();
            db.close();
        }
        return date;
    }

    public boolean isEmpty(){
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT " + Params.KEY_SHORT_NAME +" FROM "+Params.TABLE_NAME+ " WHERE " + Params.KEY_SHORT_NAME +"='AFN'";
        Cursor cursor = db.rawQuery(select,null);
        if(cursor!=null && cursor.moveToFirst()) {
            cursor.close();
            return false;
        }else {
            return true;
        }
    }

    public List<String> getColumn(String column_name){
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT " + column_name +" FROM "+ Params.TABLE_NAME;
        Cursor cursor = db.rawQuery(select,null);
        List<String> lst = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                String s = cursor.getString(0);
                lst.add(s);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lst;
    }


    public String getFlagLocationAndRate(String countryName){
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM "+ Params.TABLE_NAME + " WHERE "+ Params.KEY_LONG_NAME + "=" + "'"+countryName+"'";
        Cursor cursor = db.rawQuery(select,null);

        String short_name="";
        double rate=1.0;

        if(cursor.moveToFirst()) {
            rate = cursor.getDouble(3);
            short_name = cursor.getString(4);
        }
        cursor.close();
        db.close();

        return short_name+"#"+rate;
    }
}
