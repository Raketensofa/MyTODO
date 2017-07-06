package cgellner.mytodo.database;

import java.util.List;

import cgellner.mytodo.model.TodoItem;

/**
 * Created by Carolin on 07.05.2017.
 */
public interface ITodoItemCRUD {

     public TodoItem createTodoItem(TodoItem todoItem);
     public List<TodoItem> readAllTodoItems();
     public TodoItem readTodoItem(long todoItemId);
     public TodoItem updateTodoItem(TodoItem item);
     public boolean deleteTodoItem(long todoItemId);
     public boolean deleteAllTodoItems();

}
