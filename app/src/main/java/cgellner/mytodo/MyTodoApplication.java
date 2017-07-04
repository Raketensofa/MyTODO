package cgellner.mytodo;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import database.IRemoteInit;
import database.ITodoItemCRUD;
import database.ITodoItemCRUDAsync;
import database.IRemoteInitAsync;
import database.LocalDatabaseImpl;
import database.RemoteDatabaseImpl;
import model.TodoItem;
import model.User;

/**
 * Created by Carolin on 15.06.2017.
 */
public class MyTodoApplication extends Application implements ITodoItemCRUDAsync, IRemoteInitAsync {

    private static String TAG = MyTodoApplication.class.getSimpleName();

    private boolean isConnToRemote;

    private ITodoItemCRUD localCrud;
    private ITodoItemCRUD remoteSyncCrud;
    private IRemoteInit remoteSyncInit;

    public boolean isConnToRemote() {
        return isConnToRemote;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        localCrud = new LocalDatabaseImpl(this);
        remoteSyncCrud = new RemoteDatabaseImpl();
        remoteSyncInit = new RemoteDatabaseImpl();

        Log.i(TAG, "onCreate8)");
    }


    public ITodoItemCRUD getLocalCrud() {
        return localCrud;
    }

    public ITodoItemCRUDAsync getCRUDOperationsImpl(){
        return this;
    }

    public IRemoteInitAsync getRemoteInitImpl(){
        return this;
    }


    @Override
    public void createTodoItem(TodoItem todoItem, final ITodoItemCRUDAsync.CallbackFunction<TodoItem> callback) {

        Log.i(TAG, "Start to create item ... ");

        new AsyncTask<TodoItem, Void, TodoItem>(){

            @Override
            protected TodoItem doInBackground(TodoItem... params) {


                return  remoteSyncCrud.createTodoItem(params[0]);
            }

            @Override
            protected void onPostExecute(TodoItem result) {
                callback.process(result);
            }

        }.execute(todoItem);
    }


    @Override
    public void readAllTodoItems(final ITodoItemCRUDAsync.CallbackFunction<List<TodoItem>> callback) {

        Log.i(TAG, "Start to read all items ... ");

        new AsyncTask<Void, Void, List<TodoItem>>(){

            @Override
            protected List<TodoItem> doInBackground(Void... params) {

                return remoteSyncCrud.readAllTodoItems();
            }

            @Override
            protected void onPostExecute(List<TodoItem> todoItems) {
                callback.process(todoItems);
            }

        }.execute();
    }


    @Override
    public void readTodoItem(long id, final ITodoItemCRUDAsync.CallbackFunction<TodoItem> callback) {

        Log.i(TAG, "Start to read item ... ");

        new AsyncTask<Long, Void, TodoItem>(){

            @Override
            protected TodoItem doInBackground(Long... params) {

                return remoteSyncCrud.readTodoItem(params[0]);
            }

            @Override
            protected void onPostExecute(TodoItem item) {
                callback.process(item);
            }

        }.execute(id);
    }


    @Override
    public void updateTodoItem(TodoItem item, final ITodoItemCRUDAsync.CallbackFunction<TodoItem> callback) {

        Log.i(TAG, "Start to update item ... ");

        new AsyncTask<TodoItem, Void, TodoItem>(){

            @Override
            protected TodoItem doInBackground(TodoItem... params) {

                return remoteSyncCrud.updateTodoItem(params[0]);
            }

            @Override
            protected void onPostExecute(TodoItem result) {
               callback.process(result);
            }
        }.execute(item);
    }


    @Override
    public void deleteTodoItem(long id, final ITodoItemCRUDAsync.CallbackFunction<Boolean> callback) {

        Log.i(TAG, "Start to delete item ... " + id);

        new AsyncTask<Long, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Long... params) {

                return remoteSyncCrud.deleteTodoItem(params[0]);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                callback.process(result);
            }

        }.execute(id);
    }


    @Override
    public void deleteAllTodoItems(final ITodoItemCRUDAsync.CallbackFunction<Boolean> callback) {

        Log.i(TAG, "Start to delete all items ... ");

        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {

                return remoteSyncCrud.deleteAllTodoItems();
            }

            @Override
            protected void onPostExecute(Boolean result) {
                callback.process(result);
            }

        }.execute();
    }


    @Override
    public void authorizeUser(User user, final IRemoteInitAsync.CallbackFunction<Boolean> callback) {

        Log.i(TAG, "Start to authorize user ... ");

        new AsyncTask<User, Void, Boolean >(){

            @Override
            protected Boolean doInBackground(User... params) {
                return remoteSyncInit.authorizeUser(params[0]);
            }

            @Override
            protected void onPostExecute(Boolean result) {
               callback.process(result);
            }

        }.execute(user);
    }


    @Override
    public void isConnected(final IRemoteInitAsync.CallbackFunction<Boolean> callback) {

        Log.i(TAG, "Start to check remote connection ... ");

        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {

                try{
                    return remoteSyncInit.isConnected();

                }catch (Exception ex){

                    Log.i(TAG, ex.getMessage());
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                isConnToRemote = result;
                callback.process(result);
            }

        }.execute();
    }


    public boolean hasPermission(Context ctx, String permission){

        boolean hasPermission = false;

        if(ctx.getPackageManager().checkPermission(permission, ctx.getPackageName()) == PackageManager.PERMISSION_GRANTED){

            hasPermission = true;
        }
        return hasPermission;
    }
}
