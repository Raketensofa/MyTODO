package cgellner.mytodo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import database.IRemoteInitAsync;


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
}
