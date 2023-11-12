package com.seg2105.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_SKU = "SKU";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    //CREATE THE TABLE
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME
                + " TEXT," + COLUMN_SKU + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    //METHOD TO DROP OLD TABLE AND CREATE NEW ONES
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    //Method to add to the database
    public void addProduct(Product product){
        //get the data repository in write mode
        SQLiteDatabase db= this.getWritableDatabase();
        //create a new map of values where column names are the keys
        ContentValues values= new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_SKU, product.getSku());
        //insert
        db.insert(TABLE_PRODUCTS,null, values);
        db.close();
    }
    //Method to read from database
    public Product findProduct(String productname){
        //reference to database
        SQLiteDatabase db=this.getReadableDatabase();
        //query
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " = \"" + productname + "\"";
        Cursor cursor =db.rawQuery(query, null);
        //create object to get results
        Product product= new Product();
        if (cursor.moveToFirst()){
            product.setID(Integer.parseInt(cursor.getString(0))); //0 means first column of the database table
            product.setProductName(cursor.getString(1));
            product.setSku(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        }else{
            product=null;
        }
        db.close();
        return product;
    }
    //method to delete from database
    public boolean deleteProduct(String productname){
        boolean result= false;
        SQLiteDatabase db= this.getWritableDatabase();
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " = \"" + productname + "\"";
        Cursor cursor =db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            String idStr= cursor.getString(0);
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = " + idStr, null);
            result=true;
        }
        db.close();
        return result;
    }

}