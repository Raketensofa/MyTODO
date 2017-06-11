package database;


import android.util.Log;

import java.util.List;

import model.TodoItem;
import model.User;
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

    private static String TAG = RemoteTodoItemCRUDOperations.class.getSimpleName();

    public interface ITodoItemCRUDWebApi {

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

        @PUT("/api/users/auth")
        public Call<Boolean> authorizeUser(@Body User user);

    }

    private ITodoItemCRUDWebApi webApi;

    public RemoteTodoItemCRUDOperations(){



        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.3.2:8080/")
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

        try{

            List<TodoItem> todoItemList = this.webApi.readAllTodoItems().execute().body();

            Log.i(TAG, "readAllTodoItems");

            return todoItemList;

        }catch (Exception e){

            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public TodoItem readTodoItem(long todoItemId) {

        try{

            TodoItem todoItem = this.webApi.readTodoItem(todoItemId).execute().body();
            return todoItem;

        }catch (Exception e){

            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public TodoItem updateTodoItem(long id, TodoItem item) {

        try{

            TodoItem updatedItem = this.webApi.updateTodoItem(id, item).execute().body();
            return updatedItem;

        }catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
      }

    @Override
    public boolean deleteTodoItem(long todoItemId) {

        try{

            boolean deleted = this.webApi.deleteTodoItem(todoItemId).execute().body();
            return deleted;

        }catch (Exception e){

            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public boolean authorizeUser(User user) {

        try{

            boolean isAuthorize = this.webApi.authorizeUser(user).execute().body();
            return isAuthorize;

        }catch (Exception e){

            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean isConnectedToWeb() {

        boolean connected = false;

        try{

            int state = this.webApi.readAllTodoItems().execute().code();
            Log.i(TAG, "Connection State WebApi: " + String.valueOf(state));

            if(state == 200){

                connected = true;
            }

            return connected;

        }catch (Exception e){

            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
