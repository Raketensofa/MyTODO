package cgellner.mytodo;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;


import java.util.List;

import database.IRemoteInitAsync;
import database.ITodoItemCRUDAsync;
import model.TodoItem;


public class    StartActivity extends Activity{

    private static String TAG = StartActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!((MyTodoApplication) getApplication()).hasPermission(this, Manifest.permission.INTERNET)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 0);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("MyTODO");
        progressDialog.setMessage("Prüfe Verbindung zur externen Datenbank");
        progressDialog.show();

        ((MyTodoApplication) getApplication()).getRemoteInitImpl().isConnected(new IRemoteInitAsync.CallbackFunction<Boolean>() {

            @Override
            public void process(Boolean result) {

                Log.d(TAG, "Connected to WebApi Result: " + String.valueOf(result));

                if (result) {

                    progressDialog.setMessage("Synchronisiere die Datenbanken");

                    new AsyncTask<Void, Boolean, Boolean>(){

                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            return compareDatabases();
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {

                           if(aBoolean) {

                               progressDialog.setMessage("Starte Anwendung");

                               Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                               Log.e(TAG, "START ACTIVITY: " + LoginActivity.class.getSimpleName());

                               progressDialog.hide();
                               startActivity(intent);
                               finish();
                           }
                        }
                    }.execute();

                } else {

                    progressDialog.setMessage("Starte Anwendung im lokalen Modus");

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

        final List<TodoItem> localItemList  = ((MyTodoApplication) getApplication()).getLocalCrud().readAllTodoItems();

        Log.i(TAG, "Local Database Size: " + localItemList.size());

        if (localItemList.size() > 0) {

            Log.i(TAG, "Start to delete all remote items ...");

            ((MyTodoApplication) getApplication()).getCRUDOperationsImpl().deleteAllTodoItems(new ITodoItemCRUDAsync.CallbackFunction<Boolean>() {
                @Override
                public void process(Boolean result) {

                    Log.i(TAG, "All remote items deleted: " + result);

                    if(result){
                        Log.i(TAG, "Start to add all local items to remote...");
                        addAllLocalItemsToRemote(localItemList);
                    }
                }
            });

        } else {
            Log.i(TAG, "Start to add all remote items to local...");
            addAllRemoteItemsToLocal();
        }

        return true;
    }


    private void addAllLocalItemsToRemote(List<TodoItem> localTodoItems) {

        if (localTodoItems != null && localTodoItems.size() > 0) {

            Log.i(TAG, "Add " + localTodoItems.size() + " local TodoItems to remote ....");

            for (TodoItem item : localTodoItems) {

                ((MyTodoApplication) getApplication()).getCRUDOperationsImpl().createTodoItem(item, new ITodoItemCRUDAsync.CallbackFunction<TodoItem>() {
                    @Override
                    public void process(TodoItem result) {

                        if(result != null){
                            Log.i(TAG, "Remote Item created: " + result.toString());

                        }else{
                            Log.e(TAG, "ERROR Remote Item created: " + result.toString());
                        }
                    }
                });
            }
        }
    }


    private void addAllRemoteItemsToLocal() {

        ((MyTodoApplication) getApplication()).getCRUDOperationsImpl().readAllTodoItems(new ITodoItemCRUDAsync.CallbackFunction<List<TodoItem>>() {
            @Override
            public void process(List<TodoItem> result) {

                if (result.size() > 0) {

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
