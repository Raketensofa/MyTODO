package cgellner.mytodo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import database.IRemoteInitAsync;
import database.ITodoItemCRUD;
import database.ITodoItemCRUDAsync;
import model.TodoItem;


public class StartActivity extends Activity{

    private static String TAG = StartActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        ((MyTodoApplication) getApplication()).getRemoteInitImpl().isConnected(new IRemoteInitAsync.CallbackFunction<Boolean>() {

            @Override
            public void process(Boolean result) {

                Intent intent = null;

                Log.d(TAG, "Connected to WebApi Result: " + String.valueOf(result));

                if (result) {

                    compareDatabases();

                    intent = new Intent(getBaseContext(), LoginActivity.class);
                    Log.d(TAG, "Starting Activity: " + LoginActivity.class.getSimpleName());

                } else {

                    intent = new Intent(getBaseContext(), TodoOverviewActivity.class);
                    Toast.makeText(getApplicationContext(), "WEB API NICHT VERFÜGBAR - NUTZUNG DER LOKALEN DATENBANK", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Starting Activity: " + TodoOverviewActivity.class.getSimpleName());
                }

                progressDialog.hide();
                startActivity(intent);
                finish();
            }
        });
    }


    private void compareDatabases() {

        Log.i(TAG, "starting compare Databases...");

        final ITodoItemCRUD localDatabase = ((MyTodoApplication) getApplication()).getLocalCrud();
        final List<TodoItem> itemList = localDatabase.readAllTodoItems();
        Log.i(TAG, "Local Database Size: " + itemList.size());

        if (itemList.size() > 0) {

            deleteAllRemoteTodoItems();

            addAllLocalItemsToRemote(itemList);

        } else if (itemList.size() == 0) {

            addAllRemoteItemsToLocal();
        }
    }


    private void deleteAllRemoteTodoItems(){

        ((MyTodoApplication) getApplication()).getCRUDOperationsImpl().readAllTodoItems(new ITodoItemCRUDAsync.CallbackFunction<List<TodoItem>>() {
            @Override
            public void process(List<TodoItem> result) {

                if(result != null && result.size() > 0){

                    Log.i(TAG, "Starting delete" + result.size() + "Items from WebApi...");

                    for (final TodoItem todoItem : result){

                        ((MyTodoApplication) getApplication()).getCRUDOperationsImpl().deleteTodoItem(todoItem.getId(), new ITodoItemCRUDAsync.CallbackFunction<Boolean>() {
                            @Override
                            public void process(Boolean result) {

                                Log.i(TAG, "Remote TodoItem Id: " + todoItem.getId() + " deleted");
                            }
                        });

                    }
                }
            }
        });
    }


    private void addAllLocalItemsToRemote(List<TodoItem> localTodoItems){

        if(localTodoItems != null && localTodoItems.size() > 0){

            Log.i(TAG, "Add" + localTodoItems.size() + "local TodoItems to Remote ....");

            for (TodoItem item : localTodoItems) {

                Log.i(TAG, "Item: " + item.toString());

                ((MyTodoApplication) getApplication()).getCRUDOperationsImpl().createTodoItem(item, new ITodoItemCRUDAsync.CallbackFunction<TodoItem>() {
                    @Override
                    public void process(TodoItem result) {

                        Log.i(TAG, "Added Item to Remote: " + result.toString());
                    }
                });
            }
        }
    }


    private void addAllRemoteItemsToLocal(){

       ((MyTodoApplication) getApplication()).getCRUDOperationsImpl().readAllTodoItems(new ITodoItemCRUDAsync.CallbackFunction<List<TodoItem>>() {
           @Override
           public void process(List<TodoItem> result) {

               if (result != null) {

                   Log.i(TAG, "Add " + result.size() + " Web Items to local Database ....");

                   for (TodoItem item : result) {

                       Log.i(TAG, "Item: " + item.toString());
                       ((MyTodoApplication) getApplication()).getLocalCrud().createTodoItem(item);
                   }
               }
           }
       });
    }
}
