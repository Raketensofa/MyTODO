package database;

import android.database.Cursor;
import android.R;

import java.util.List;

import model.Todo;

/**
 * Created by Carolin on 07.05.2017.
 */
public interface ITodoItemCRUD {

     long createTodo(Todo todoItem);

     List<Todo> readAllTodoItems();
     Todo readTodoItem(long todoItemId);
     boolean updateTodoItem(Todo item);
     boolean deleteTodoItem(long todoItemId);
    
}
