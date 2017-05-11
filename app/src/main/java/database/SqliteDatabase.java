package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


import model.Todo;


/**
 * Created by Carolin on 21.04.2017.
 */
public class SqliteDatabase extends SQLiteOpenHelper implements ITodoItemCRUD {


    //region Fields

    private static final String DATABASE_NAME = "Mytodo_Sqlite.db";
    private static final int DATABASE_VERSION = 2;
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



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try{

            Database = sqLiteDatabase;
            Database.execSQL(Queries.CREATE_TABLE_TODOS);

        }catch (Exception ex){

            Log.e(this.getClass().getName(), ex.getMessage());
        }

    }


    /**
     * Die Methode oeffnet die SQLite-Datenbank.
     */
    public void open(){

        Database = this.getWritableDatabase();

       Log.v(this.getClass().getName(), "Datenbank wurde geoeffnet");

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Queries.TABLE_TODOS);
        this.onCreate(sqLiteDatabase);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    @Override
    public long createTodo(Todo todoItem) {

        long newTodoId = 0;

        ContentValues todoValues = todoItem.createContentValues();

        try {

            if (Database.isOpen()) {

                newTodoId = Database.insert(Queries.TABLE_TODOS, null, todoValues);
            }

        } catch (Exception ex) {

            Log.e(this.getClass().getName(), ex.getMessage());
            return -1;
        }

        return newTodoId;
    }


    @Override
    public List<Todo> readAllTodoItems() {

        List<Todo> list = null;
        Cursor cursor = null;

        try {

                if(Database.isOpen()) {

                    list = new ArrayList<>();

                    //Abfrage
                    cursor = Database.query(Queries.TABLE_TODOS, Queries.COLUMNS_TABLE_TODOS, null, null, null, null, Queries.COLUMN_ID + " ASC");

                    if (cursor != null) {
                        if (cursor.moveToFirst()) {

                            do {

                                Todo todoItem = new Todo();
                                todoItem.setAllDataFromCursor(cursor);
                                list.add(todoItem);

                            } while (cursor.moveToNext());
                        }
                    }
                }

            } catch (Exception ex) {

                    Log.e(this.getClass().getName(), ex.getMessage());
                    return null;

            }finally {

                cursor.close();
            }

        return list;
    }


    @Override
    public Todo readTodoItem(long todoItemId) {

        Todo todoItem = null;
        Cursor cursor = null;

        try {


            if(Database.isOpen()) {

                //Abfrage
                cursor = Database.query(Queries.TABLE_TODOS, Queries.COLUMNS_TABLE_TODOS, Queries.COLUMN_ID + " = ?", new String[]{String.valueOf(todoItemId)}, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {

                        do {

                            todoItem = new Todo();
                            todoItem.setAllDataFromCursor(cursor);

                        } while (cursor.moveToNext());
                    }
                }
            }

        } catch (Exception ex) {

            Log.e(this.getClass().getName(), ex.getMessage());
            return null;

        }finally {

            cursor.close();
        }

        return todoItem;
    }


    @Override
    public boolean updateTodoItem(Todo item) {

        boolean isUpdated = false;

        if(Database.isOpen()) {

            try {

                ContentValues todoValues = item.createContentValues();

                int result = Database.update(Queries.TABLE_TODOS, todoValues, "_id=" + item.get_id(), null);

                if(result > 0){
                    isUpdated = true;
                }

            } catch (Exception ex) {

            }
        }

        return isUpdated;
    }


    @Override
    public boolean deleteTodoItem(long todoItemId) {

        boolean isDeleted = false;
        int result;

        try {

            if(Database.isOpen()) {

                //Abfrage
                result = Database.delete(Queries.TABLE_TODOS, Queries.COLUMN_ID + " = ?", new String[]{String.valueOf(todoItemId)});

                if(result > 0){

                    isDeleted = true;
                }
            }

        } catch (Exception ex) {

            Log.e(this.getClass().getName(), ex.getMessage());
            return false;
        }

        return isDeleted;
    }



}
