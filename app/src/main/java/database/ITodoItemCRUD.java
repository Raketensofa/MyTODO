package database;

import java.util.List;

import model.TodoItem;

/**
 * Created by Carolin on 07.05.2017.
 */
public interface ITodoItemCRUD {

     public TodoItem createTodoItem(TodoItem todoItem);
     public List<TodoItem> readAllTodoItems(int sortMode);
     public TodoItem readTodoItem(long todoItemId);
     public TodoItem updateTodoItem(long id, TodoItem item);
     public boolean deleteTodoItem(long todoItemId);
    
}
