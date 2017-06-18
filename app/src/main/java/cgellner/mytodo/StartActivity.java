package cgellner.mytodo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import database.IRemoteInitAsync;
import database.ITodoItemCRUD;
import database.ITodoItemCRUDAsync;
import database.RemoteDatabaseImpl;
import model.TodoItem;


public class StartActivity extends Activity{

    private static String TAG = StartActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    private ITodoItemCRUD remoteCrud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        remoteCrud = new RemoteDatabaseImpl();

        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        ((MyTodoApplication) getApplication()).getRemoteInitImpl().isConnected(new IRemoteInitAsync.CallbackFunction<Boolean>() {

            @Override
            public void process(Boolean result) {

                Log.d(TAG, "Connected to WebApi Result: " + String.valueOf(result));

                if (result) {

                    new AsyncTask<Void, Void, Boolean>(){

                        @Override
                        protected Boolean doInBackground(Void... params) {
                            return  compareDatabases();
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {

                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                            Log.d(TAG, "Starting Activity: " + LoginActivity.class.getSimpleName());

                            progressDialog.hide();
                            startActivity(intent);
                            finish();
                        }

                    }.execute();

                } else {

                    Intent intent = new Intent(getBaseContext(), TodoOverviewActivity.class);
                    Toast.makeText(getApplicationContext(), "WEB API NICHT VERFÜGBAR - NUTZUNG DER LOKALEN DATENBANK", Toast.LENGTH_LONG).show();

                    Log.d(TAG, "Starting Activity: " + TodoOverviewActivity.class.getSimpleName());

                    progressDialog.hide();
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    private boolean compareDatabases() {

        Log.i(TAG, "starting compare Databases...");

        final ITodoItemCRUD localDatabase = ((MyTodoApplication) getApplication()).getLocalCrud();
        final List<TodoItem> itemList = localDatabase.readAllTodoItems();

        Log.i(TAG, "Local Database Size: " + itemList.size());

        if (itemList.size() > 0) {

            List<TodoItem> remoteList = remoteCrud.readAllTodoItems();
            deleteAllRemoteTodoItems(remoteList);
            addAllLocalItemsToRemote(itemList);

        } else if (itemList.size() == 0) {

            addAllRemoteItemsToLocal();
        }

        return true;
    }


    private boolean deleteAllRemoteTodoItems(List<TodoItem> remoteItemList) {

        boolean deletedAll = false;
        int counter = 0;

        if (remoteItemList != null) {

            Log.i(TAG, "Starting delete " + remoteItemList.size() + " Items from WebApi ...");

            for (final TodoItem todoItem : remoteItemList) {

                boolean deletedItem = remoteCrud.deleteTodoItem(todoItem.getId());

                if (deletedItem) {
                    counter++;
                    Log.i(TAG, "Deleted Remote Item: " + todoItem.getId());

                } else {
                    Log.e(TAG, "ERROR Deleted Remote Item: " + todoItem.getId());
                }
            }
        }

        if(counter == remoteItemList.size() -1){

            deletedAll = true;
        }

        return deletedAll;
    }


    private void addAllLocalItemsToRemote(List<TodoItem> localTodoItems) {

        if (localTodoItems != null && localTodoItems.size() > 0) {

            Log.i(TAG, "Add " + localTodoItems.size() + " local TodoItems to remote ....");

            for (TodoItem item : localTodoItems) {

               TodoItem createdRemoteItem = remoteCrud.createTodoItem(item);

                if(createdRemoteItem != null){
                    Log.i(TAG, "Remote Item created: " + item.toString());

                }else{
                    Log.e(TAG, "ERROR Remote Item created: " + item.toString());
                }
            }
        }
    }


    private void addAllRemoteItemsToLocal(){

        ((MyTodoApplication) getApplication()).getCRUDOperationsImpl().readAllTodoItems(new ITodoItemCRUDAsync.CallbackFunction<List<TodoItem>>() {
            @Override
            public void process(List<TodoItem> result) {

                if(result.size() > 0){

                    Log.i(TAG, "Add " + result.size() + " Web Items to local Database ....");

                    for (TodoItem item : result) {

                        ((MyTodoApplication) getApplication()).getLocalCrud().createTodoItem(item);
                        Log.i(TAG, "Local Item created: " + item.toString());
                    }
                }
            }
        });
    }
}
