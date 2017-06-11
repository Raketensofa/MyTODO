package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cgellner.mytodo.R;
import model.TodoItem;

/**
 * Created by Carolin on 08.06.2017.
 */
public class LocalTodoItemCRUDOperations implements ITodoItemCRUD {

    protected static String TAG = LocalTodoItemCRUDOperations.class.getSimpleName();

    private SQLiteDatabase database;

    public LocalTodoItemCRUDOperations(Context context){

        database = context.openOrCreateDatabase("tododb.sqlite", Context.MODE_PRIVATE, null);
        if(database.getVersion() == 0){
            database.setVersion(1);
            database.execSQL(Queries.CREATE_TABLE_TODOS);
            database.execSQL(Queries.CREATE_TABLE_TODO_CONTACTS);
        }


        if(database.isOpen()){
            Log.i(TAG, "Datenbank ist geoeffnet -  Version:" + database.getVersion());
        }
    }

    @Override
    public TodoItem createTodoItem(TodoItem todoItem) {

        ContentValues values = todoItem.createContentValues();

        long id = database.insert(Queries.TABLE_TODOS, null, values);
        todoItem.setId(id);

        Log.i(TAG, "Todo-Item local gespeichert: " + todoItem.toString());

        return todoItem;
    }

    @Override
    public List<TodoItem> readAllTodoItems(int sortMode) {

        List<TodoItem> itemList = new ArrayList<TodoItem>();
        Cursor cursor = null;

        if(sortMode == R.integer.SORT_MODE_DEADLINE_FAVOURITE) {

            cursor = database.query(Queries.TABLE_TODOS, Queries.COLUMNS_TABLE_TODOS, null, null, null, null,
                    Queries.COLUMN_ISDONE +
                            "," + Queries.COLUMN_DEADLINE_DATE +
                            "," + Queries.COLUMN_DEADLINE_TIME +
                            "," + Queries.COLUMN_ISFAVOURITE + " ASC");

        }else if(sortMode == R.integer.SORT_MODE_FAVOURITE_DEADLINE){

            cursor = database.query(Queries.TABLE_TODOS, Queries.COLUMNS_TABLE_TODOS, null, null, null, null,
                    Queries.COLUMN_ISDONE +
                            "," + Queries.COLUMN_ISFAVOURITE +
                            "," + Queries.COLUMN_DEADLINE_DATE +
                            "," + Queries.COLUMN_DEADLINE_TIME +
                             " ASC");
        }


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
        Cursor cursor = database.query(Queries.TABLE_TODOS, Queries.COLUMNS_TABLE_TODOS, Queries.COLUMN_ID + " = ?", new String[]{String.valueOf(todoItemId)}, null, null, null);

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
    public TodoItem updateTodoItem(long id, TodoItem item) {

        ContentValues todoValues = item.createContentValues();

        int result = database.update(Queries.TABLE_TODOS, todoValues, Queries.COLUMN_ID + "=" + id, null);

        if(result > 0) {
            Log.i(TAG, "Updated Todo-Item:" + item.toString());
        }

        return item;
    }

    @Override
    public boolean deleteTodoItem(long todoItemId) {

        boolean deleted = false;

        int result = database.delete(Queries.TABLE_TODOS, Queries.COLUMN_ID + " = ?", new String[]{String.valueOf(todoItemId)});

        Log.i(TAG, "Delete Result: " + result);

        if(result == 1) {
            Log.i(TAG, "Delete Todo-Item:" + todoItemId + " - true");
            deleted = true;
        }else{
            Log.i(TAG, "Error Delete Todo-Item:" + todoItemId);
        }

        return deleted;
    }
}
