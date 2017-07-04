package cgellner.mytodo;


import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;


import database.IRemoteInitAsync;
import model.User;



public class LoginActivity extends Activity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private ProgressDialog progressDialog;
    private  Button mEmailSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar)findViewById(R.id.todo_form_toolbar);
        toolbar.setTitle("MyTodo");

        progressDialog = new ProgressDialog(this);

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setEnabled(false);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailView = (EditText) findViewById(R.id.email);


        mEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(mEmailView.getText().toString()) && !TextUtils.isEmpty(mPasswordView.getText().toString()) ){
                    mEmailSignInButton.setEnabled(true);
                }else{
                    mEmailSignInButton.setEnabled(false);
                }
            }
        });


        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(mEmailView.getText().toString()) && !TextUtils.isEmpty(mPasswordView.getText().toString()) ){
                    mEmailSignInButton.setEnabled(true);
                }else{
                    mEmailSignInButton.setEnabled(false);
                }
            }
        });


        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });



    }


    private void attemptLogin() {

        progressDialog.setTitle("Login");
        progressDialog.setMessage("Prüfe Login Daten");
        progressDialog.show();

        boolean isEmailCorrect = isEmailTextFieldValid();
        boolean isPwdCorrect = isPwdTextFieldValid();

        if (isEmailCorrect && isPwdCorrect) {

            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();

            User user = new User();
            user.setEmail(email);
            user.setPwd(password);

            ((MyTodoApplication) getApplication()).getRemoteInitImpl().authorizeUser(user, new IRemoteInitAsync.CallbackFunction<Boolean>() {
                @Override
                public void process(Boolean result) {

                    if (result) {

                        progressDialog.setMessage("Login erfolgreich");
                        progressDialog.hide();

                        Intent intent = new Intent(getBaseContext(), TodoOverviewActivity.class);
                        startActivity(intent);
                        finish();
                        progressDialog.setMessage("Login erfolgreich");

                    } else {

                        progressDialog.setMessage("Login nicht erfolgreich");
                        progressDialog.hide();

                        Toast.makeText(getBaseContext(), "Anmeldung fehlgeschlagen. E-Mail und/oder Passwort falsch.", Toast.LENGTH_LONG).show();
                        mEmailView.setError("ggf. falsche E-Mail");
                        mPasswordView.setError("ggf. falsches Passwort");
                        mEmailView.requestFocus();
                    }
                }
            });

        }else{
            progressDialog.setMessage("Logindaten fehlerhaft");
            progressDialog.hide();
        }
    }


    private boolean isEmailValid(String email) {

        boolean isValid = false;

        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            isValid = true;
        }

        return isValid;
    }


    private boolean isPasswordValid(String password) {

        return password.length() == 6;
    }


    private boolean isEmailTextFieldValid(){

        boolean isValid = false;

        if (!TextUtils.isEmpty(mEmailView.getText().toString())){

            if (isEmailValid(mEmailView.getText().toString())) {

                isValid = true;
                mEmailView.setError(null);

            }else{

                mEmailView.setError("Falsches Format");
            }

        }

        return isValid;
    }


    private boolean isPwdTextFieldValid(){

        boolean isValid = false;

        if (!TextUtils.isEmpty(mPasswordView.getText().toString())){

            if (isPasswordValid(mPasswordView.getText().toString())) {

                isValid = true;
                mPasswordView.setError(null);

            }else{

                mPasswordView.setError("Passwort muss 6 Zeichen haben");
            }
        }

        return isValid;
    }
}

