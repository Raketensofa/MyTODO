package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.session.MediaSession;

import java.util.List;

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
            database.execSQL(Queries.CREATE_TABLE_MAIN_SETTINGS);
            database.execSQL(Queries.CREATE_TABLE_TODO_CONTACTS);
        }

    }

    @Override
    public TodoItem createTodoItem(TodoItem todoItem) {


        return null;
    }

    @Override
    public List<TodoItem> readAllTodoItems() {



        return null;
    }

    @Override
    public TodoItem readTodoItem(long todoItemId) {



        return null;
    }

    @Override
    public TodoItem updateTodoItem(long id, TodoItem item) {


        return null;
    }

    @Override
    public boolean deleteTodoItem(long todoItemId) {


        return false;
    }

}
