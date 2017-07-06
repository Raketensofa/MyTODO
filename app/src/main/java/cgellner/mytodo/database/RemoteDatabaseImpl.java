package cgellner.mytodo.database;


import android.util.Log;

import java.util.List;

import cgellner.mytodo.model.TodoItem;
import cgellner.mytodo.model.User;
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

        @POST("/api/todos/")
        public Call<TodoItem> createTodoItem(@Body TodoItem todoItem);

        @GET("/api/todos/")
        public Call<List<TodoItem>> readAllTodoItems();

        @POST("/api/todos/{id}")
        public Call<TodoItem> readTodoItem(@Path("id") long todoItemId);

        @PUT("/api/todos/{id}")
        public Call<TodoItem> updateTodoItem(@Path("id") long id, @Body TodoItem item);

        @DELETE("/api/todos/{id}")
        public Call<Boolean> deleteTodoItem(@Path("id") long todoItemId);

        @PUT("/api/users/auth")
        public Call<Boolean> authorizeUser(@Body User user);

        @DELETE("/api/todos/")
        public Call<Boolean> deleteAllTodoItems();

    }


    private IWebApi webApi;

    public RemoteDatabaseImpl(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        this.webApi = retrofit.create(IWebApi.class);
    }



    @Override
    public TodoItem createTodoItem(TodoItem todoItem) {

        try{
            TodoItem created = this.webApi.createTodoItem(todoItem).execute().body();
            Log.i(TAG, "Remote Item created: " + created.toString());

            return created;

        }catch (Exception e){

            Log.e(TAG, "createTodoItem: " + e.getMessage());
            return null;
        }
    }


    @Override
    public List<TodoItem> readAllTodoItems() {

        try{

            List<TodoItem> todoItemList = this.webApi.readAllTodoItems().execute().body();
            Log.i(TAG, "Remote ItemList size: " + todoItemList.size());

            return todoItemList;

        }catch (Exception e){

            Log.e(TAG, "readAllTodoItems: " + e.getMessage());
            return null;
        }
    }


    @Override
    public TodoItem readTodoItem(long todoItemId) {

        try{

            TodoItem todoItem = this.webApi.readTodoItem(todoItemId).execute().body();
            return todoItem;

        }catch (Exception e){

            Log.e(TAG, "readTodoItem: " + e.getMessage());
           return null;
        }
    }


    @Override
    public TodoItem updateTodoItem(TodoItem item) {

        try{

            TodoItem updatedItem = this.webApi.updateTodoItem(item.getId(), item).execute().body();
            return updatedItem;

        }catch (Exception e) {

            Log.e(TAG, "updateTodoItem: " + e.getMessage());
            return null;
        }
      }


    @Override
    public boolean deleteTodoItem(long todoItemId) {

        try{

            boolean deleted = this.webApi.deleteTodoItem(todoItemId).execute().body();
            return deleted;

        }catch (Exception e){

            Log.e(TAG, "deleteTodoItem(" + todoItemId + "): " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean deleteAllTodoItems() {

        try{

            boolean deleted = this.webApi.deleteAllTodoItems().execute().body();
            return deleted;

        }catch (Exception e){

            Log.e(TAG, "Error delete " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean authorizeUser(User user) {

        try{

            boolean isAuthorize = this.webApi.authorizeUser(user).execute().body();
            return isAuthorize;

        }catch (Exception e){

            Log.e(TAG, "authorizeUser: " + e.getMessage());
            return false;
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

            Log.e(TAG, "isConnected: " + e.getMessage());
            return false;
        }
    }
}
