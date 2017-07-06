package cgellner.mytodo.database;

import java.util.List;

import cgellner.mytodo.model.TodoItem;

/**
 * Created by Carolin on 15.06.2017.
 */
public interface ITodoItemCRUDAsync {

    public static interface CallbackFunction<T>{

        public void process (T result);

    }

    public void createTodoItem(TodoItem todoItem, CallbackFunction<TodoItem> callback);
    public void readAllTodoItems(CallbackFunction<List<TodoItem>> callback);
    public void readTodoItem(long id, CallbackFunction<TodoItem> callback );
    public void updateTodoItem(TodoItem item, CallbackFunction<TodoItem> callback);
    public void deleteTodoItem(long id, CallbackFunction<Boolean> callback);
    public void deleteAllTodoItems(CallbackFunction<Boolean> callback);

}

