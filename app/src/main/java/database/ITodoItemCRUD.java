package database;

import java.util.List;

import model.Todo;

/**
 * Created by Carolin on 07.05.2017.
 */
public interface ITodoItemCRUD {

    public Todo createTodo(Todo todoItem);
    public List<Todo> readAllTodoItems();
    public Todo readTodoItem(long todoItemId);
    public Todo updateTodoItem(Todo item);
    public boolean deleteTodoItem(long todoItemId);
    
}
