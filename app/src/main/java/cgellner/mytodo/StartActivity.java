package cgellner.mytodo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import database.RemoteTodoItemCRUDOperations;

public class StartActivity extends Activity{

    private static String TAG = StartActivity.class.getSimpleName();
    private RemoteTodoItemCRUDOperations remoteOperations;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        remoteOperations = new RemoteTodoItemCRUDOperations();

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                boolean state = remoteOperations.isConnectedToWeb();
                Log.d(TAG, "Connected to WebApi: " + String.valueOf(state));
                return state;
            }

            @Override
            protected void onPostExecute(Boolean isConnected) {

                Intent intent = null;

                if(isConnected == true){

                    intent = new Intent(getBaseContext(), LoginActivity.class);
                    intent.putExtra(String.valueOf(R.string.WEPAPI_CONN), R.integer.WEBAPI_CONNECTION_TRUE);

                    Log.d(TAG, "Starting Activity: " + LoginActivity.class.getSimpleName());

                }else{

                    intent = new Intent(getBaseContext(), TodoOverviewActivity.class);
                    intent.putExtra(String.valueOf(R.string.WEPAPI_CONN), R.integer.WEBAPI_CONNECTION_FALSE);

                    Log.d(TAG, "Starting Activity: " + TodoOverviewActivity.class.getSimpleName());
                }
                progressDialog.hide();
                startActivity(intent);
                finish();
            }

        }.execute();
    }
}
