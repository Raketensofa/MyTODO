package cgellner.mytodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cgellner.mytodo.model.TodoItem;

/**
 * Created by Carolin on 08.06.2017.
 */
public class LocalDatabaseImpl implements ITodoItemCRUD{

    protected static String TAG = LocalDatabaseImpl.class.getSimpleName();

    private SQLiteDatabase database;


    public LocalDatabaseImpl(Context context){

        database = context.openOrCreateDatabase("mytodoDatabase.sqlite", Context.MODE_PRIVATE, null);
        if(database.getVersion() == 0){

            database.setVersion(1);
           String CREATE_TABLE_TODOS =
                    "CREATE TABLE " + Columns.todos.toString() + "(" +
                            Columns.id.toString() + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                            Columns.name.toString() + " TEXT NOT NULL, " +
                            Columns.description.toString()  + " TEXT," +
                            Columns.expiry.toString() + " TEXT," +
                            Columns.is_favourite.toString() + " TEXT NOT NULL,"+
                            Columns.is_done.toString() + " TEXT NOT NULL," +
                            Columns.contacts.toString() +  " TEXT)";

                    Log.i(TAG, CREATE_TABLE_TODOS);

            database.execSQL(CREATE_TABLE_TODOS);
        }

        if(database.isOpen()){
            Log.i(TAG, "Datenbank ist geoeffnet -  Version:" + database.getVersion());
        }
    }


    @Override
    public TodoItem createTodoItem(TodoItem todoItem) {

        Log.i(TAG, "start creating Item: " + todoItem.toString());

        ContentValues values = todoItem.createContentValues();

        long id = database.insert(Columns.todos.toString(), null, values);

        todoItem.setId(id);

        Log.i(TAG, "Todo-Item local gespeichert: " + todoItem.toString());

        return todoItem;
    }


    @Override
    public List<TodoItem> readAllTodoItems(){
        List<TodoItem> itemList = new ArrayList<TodoItem>();
        Cursor cursor = null;


        String[] COLUMNS_TABLE_TODOS = {
                Columns.id.toString(),
                Columns.name.toString(),
                Columns.description.toString() ,
                Columns.expiry.toString(),
                Columns.is_favourite.toString(),
                Columns.is_done.toString(),
                Columns.contacts.toString()
        };

        cursor = database.query(Columns.todos.toString(), COLUMNS_TABLE_TODOS, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {

                do {
                    TodoItem todoItem = new TodoItem();
                    todoItem.setAllDataFromCursor(cursor);
                    itemList.add(todoItem);

                    Log.i(TAG, "Todo-Item:" + todoItem.toString());

                } while (cursor.moveToNext());
            }
        }

        Log.i(TAG, "Anzahl Todo-Items:" + itemList.size());

        return itemList;
    }


    @Override
    public TodoItem readTodoItem(long todoItemId) {

        TodoItem todoItem = null;


        String[] COLUMNS_TABLE_TODOS = {
                Columns.id.toString(),
                Columns.name.toString(),
                Columns.description.toString() ,
                Columns.expiry.toString(),
                Columns.is_favourite.toString(),
                Columns.is_done.toString(),
                Columns.contacts.toString()
        };

        Cursor cursor = database.query(Columns.todos.toString(), COLUMNS_TABLE_TODOS,  Columns.id.toString() + " = ?", new String[]{String.valueOf(todoItemId)}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {

                do {
                    todoItem = new TodoItem();
                    todoItem.setAllDataFromCursor(cursor);

                } while (cursor.moveToNext());
            }
        }

        Log.i(TAG, "Read Todo-Item:" +  todoItem.toString());

        return todoItem ;
    }


    @Override
    public TodoItem updateTodoItem(TodoItem item) {

        ContentValues todoValues = item.createContentValues();

        int result = database.update(Columns.todos.toString(), todoValues,  Columns.id.toString() + "=" + item.getId(), null);

        if(result > 0) {
            Log.i(TAG, "Updated local Todo-Item:" + item.toString());
        }else{
            item = null;
        }

        return item;
    }


    @Override
    public boolean deleteTodoItem(long todoItemId) {

        boolean deleted = false;

        int result = database.delete(Columns.todos.toString(),  Columns.id.toString() + " = ?", new String[]{String.valueOf(todoItemId)});

        Log.i(TAG, "Delete Result: " + result);

        if(result == 1) {
            Log.i(TAG, "Delete Todo-Item:" + todoItemId + " - true");
            deleted = true;
        }else{
            Log.i(TAG, "Error Delete Todo-Item:" + todoItemId);
        }

        return deleted;
    }

    @Override
    public boolean deleteAllTodoItems() {
        return false;
    }
}
