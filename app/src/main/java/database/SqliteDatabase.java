package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


import models.Todo;


/**
 * Created by Carolin on 21.04.2017.
 */
public class SqliteDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Mytodo_Sqlite.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;
    private SQLiteDatabase Database;

    //endregion


    //region Constructor

    /**
     * Der Konstruktur erstellt eine neue Instanz der Klasse SqliteDatabase.
     * @param context
     */
    public SqliteDatabase(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    //endregion


    //region Getter & Setter

    public SQLiteDatabase getDatabase() {
        return Database;
    }

    public void setDatabase(SQLiteDatabase database) {
        Database = database;
    }

    //endregion


    //region Public Methods

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try{

            Database = sqLiteDatabase;

           // Database.execSQL(Sql.CREATE_TABLE_PAYMENTS);



        }catch (Exception ex){

            Log.e("Database", ex.getMessage());

        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

      // sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Sql.NAME_TABLE_PAYMENTS);


        this.onCreate(sqLiteDatabase);

    }


    /**
     * Die Methode oeffnet die SQLite-Datenbank.
     */
    public void open(){

        Database = this.getWritableDatabase();

        Log.v("Database", "Datenbank wurde geoeffnet");
    }

    //endregion


    //region Private Methods


    private ArrayList<Todo> getAllTodos(){

        ArrayList<Todo> list = null;

        if(Database.isOpen()) {

            try {

                list = new ArrayList<>();

                Cursor cursor = Database.query(Queries.CREATE_TABLE_TODOS, Queries.COLUMNS_TABLE_TODOS, null, null, null, null, null);

                if (cursor != null) {

                    if (cursor.moveToFirst()) {
                        do {

                            Todo category = new Todo();

                           // category.setID(cursor.getLong(cursor.getColumnIndex(columns[0])));
                           // category.setName(cursor.getString(cursor.getColumnIndex(columns[1])));

                            list.add(category);
                            //Log.d("Category", category.toString());

                        } while (cursor.moveToNext());
                    }
                }

                cursor.close();

            } catch (Exception ex) {

                return null;

            }
        }

        return list;
    }


    private Todo getTodo(long id) {




        return null;
    }



    private void insertData(String sql){


        if(sql != null){

            try{

                if(Database.isOpen()) {

                    Database.execSQL(sql);

                }else{

                    Log.e("Database", "Datenbank ist geschlossen.");
                }

            }catch (Exception ex){

                Log.e("Database", ex.getMessage());

            }}
    }



    //endregion

}
