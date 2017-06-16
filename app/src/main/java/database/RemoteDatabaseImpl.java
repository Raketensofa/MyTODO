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
public class RemoteDatabaseImpl implements ITodoItemCRUD, IRemoteInit {

    private static String TAG = RemoteDatabaseImpl.class.getSimpleName();

    public interface IWebApi {

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


    private IWebApi webApi;

    public RemoteDatabaseImpl(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.3.2:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        this.webApi = retrofit.create(IWebApi.class);
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
    public List<TodoItem> readAllTodoItems() {

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
    public TodoItem updateTodoItem(TodoItem item) {

        try{

            TodoItem updatedItem = this.webApi.updateTodoItem(item.getId(), item).execute().body();
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


    @Override
    public boolean authorizeUser(User user) {

        try{

            boolean isAuthorize = this.webApi.authorizeUser(user).execute().body();
            return isAuthorize;

        }catch (Exception e){

            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean isConnected() {

        boolean connected = false;

        try{
            int state = this.webApi.readAllTodoItems().execute().code();

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
