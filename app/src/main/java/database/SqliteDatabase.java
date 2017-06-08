package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


import cgellner.mytodo.R;
import model.TodoItem;

/**
 * Created by Carolin on 21.04.2017.
 */
public class SqliteDatabase extends SQLiteOpenHelper {


    //region Fields

    private static final String DATABASE_NAME = "Mytodo_Sqlite.db";
    private static final int DATABASE_VERSION = 7;
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
            Database.execSQL(Queries.CREATE_TABLE_MAIN_SETTINGS);
            Database.execSQL(Queries.CREATE_TABLE_TODO_CONTACTS);

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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Queries.TABLE_MAIN_SETTINGS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Queries.TABLE_TODO_CONTACTS);

        this.onCreate(sqLiteDatabase);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public long createTodo(TodoItem todoItem) {

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


    public List<TodoItem> readAllTodoItems() {

        List<TodoItem> list = null;
        Cursor cursor = null;

        try {

                if(Database.isOpen()) {

                    list = new ArrayList<>();

                    //Abfrage
                    cursor = Database.query(Queries.TABLE_TODOS, Queries.COLUMNS_TABLE_TODOS, null, null, null, null, Queries.COLUMN_ID + " ASC");

                    if (cursor != null) {
                        if (cursor.moveToFirst()) {

                            do {

                                TodoItem todoItem = new TodoItem();
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


    public TodoItem readTodoItem(long todoItemId) {

        TodoItem todoItem = null;
        Cursor cursor = null;

        try {


            if(Database.isOpen()) {

                //Abfrage
                cursor = Database.query(Queries.TABLE_TODOS, Queries.COLUMNS_TABLE_TODOS, Queries.COLUMN_ID + " = ?", new String[]{String.valueOf(todoItemId)}, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {

                        do {

                            todoItem = new TodoItem();
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


    public boolean updateTodoItem(TodoItem item) {

        boolean isUpdated = false;

        if(Database.isOpen()) {

            try {

                ContentValues todoValues = item.createContentValues();

                int result = Database.update(Queries.TABLE_TODOS, todoValues, "_id=" + item.getId(), null);

                if(result > 0){
                    isUpdated = true;
                }

            } catch (Exception ex) {

            }
        }

        return isUpdated;
    }


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


    public long createMainSettings() {

        long mainSettingId = -1;

        try {

            ContentValues settingValues = new ContentValues();
            settingValues.put(Queries.COLUMN_SORTMODE, R.integer.SORT_MODE_DEADLINE_FAVOURITE);

            if (Database.isOpen()) {

                mainSettingId = Database.insert(Queries.TABLE_MAIN_SETTINGS, null, settingValues);
            }

        } catch (Exception ex) {

            Log.e(this.getClass().getName(), ex.getMessage());
            return mainSettingId;
        }

        return mainSettingId;
    }



    public int readSortMode() {

        int mode = -1;
        Cursor cursor = null;

        try {

            if(Database.isOpen()) {

                cursor = Database.query(Queries.TABLE_MAIN_SETTINGS, new String[]{Queries.COLUMN_SORTMODE}, null, null, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {

                        do {
                             mode = cursor.getInt(cursor.getColumnIndexOrThrow(Queries.COLUMN_SORTMODE));

                        } while (cursor.moveToNext());
                    }
                }
            }

        } catch (Exception ex) {

            Log.e(this.getClass().getName(), ex.getMessage());
            return mode;

        }finally {

            cursor.close();
        }

        return mode;
    }


    public boolean updateSortMode(int mode) {

        boolean isUpdated = false;

        if(Database.isOpen()) {

            try {

                ContentValues settingValues = new ContentValues();
                settingValues.put(Queries.COLUMN_SORTMODE, mode);

                int result = Database.update(Queries.TABLE_MAIN_SETTINGS, settingValues, null, null);

                if(result > 0){
                    isUpdated = true;
                }

            } catch (Exception ex) {

                Log.e(this.getClass().getName(), ex.getMessage());

            }
        }

        return isUpdated;
    }
}
