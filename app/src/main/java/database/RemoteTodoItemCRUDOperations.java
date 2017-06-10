package database;


import java.util.List;

import model.TodoItem;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Carolin on 08.06.2017.
 */
public class RemoteTodoItemCRUDOperations implements ITodoItemCRUD{


    public interface ITodoItemCRUDWebApi{

        @POST("/api/todos")
        public Call<TodoItem> createTodoItem(@Body TodoItem todoItem);

        @GET("/api/todos")
        public Call<List<TodoItem>> readAllTodoItems();

        @POST("/api/todos/{id}")
        public Call<TodoItem> readTodoItem(@Path("id") long todoItemId);

        @PUT("/api/todos/{id}")
        public Call<TodoItem> updateTodoItem(@Path("id") long id, @Body TodoItem item);

        @DELETE("/api/todos/{id}")
        public Call<Boolean> deleteTodoItem(@Path("id") long todoItemId);

    }

    private ITodoItemCRUDWebApi webApi;

    public RemoteTodoItemCRUDOperations(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        this.webApi = retrofit.create(ITodoItemCRUDWebApi.class);
    }



    @Override
    public TodoItem createTodoItem(TodoItem todoItem) {

        try{

            TodoItem created = this.webApi.createTodoItem(todoItem).execute().body();
            return created;

        }catch (Exception e){

            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TodoItem> readAllTodoItems(int sortMode) {



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
