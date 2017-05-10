package cgellner.mytodo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;



public class ActivityTodoDetail extends AppCompatActivity {

    private Bundle extraData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.todo_detail_view);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_tododetail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("TODO Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Buttons in der Titelleiste (Bearbeiten, Logeschen)
        View buttonEdit = findViewById(R.id.action_edit_todo);
        buttonEdit.setVisibility(View.VISIBLE);
        View buttonDelete =  getActionBar().getCustomView().findViewById(R.id.action_delete_todo);
        buttonDelete.setVisibility(View.VISIBLE);


        extraData = getIntent().getExtras();
        setTodoDataToElements(extraData);
    }

    //TODO
    private void setTodoDataToElements(Bundle data){



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_edit_todo:

                startTodoEdit();

            case R.id.action_delete_todo:

                deleteTodo();

            case R.id.action_cancel_todo:

                cancelTodoEdit();

            case R.id.action_save_todo:

                updateTodo();

            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void startTodoEdit(){

        //Buttons in der Titelleiste anpassen
        View buttonEdit = findViewById(R.id.action_edit_todo);
        buttonEdit.setVisibility(View.INVISIBLE);
        View buttonDelete = findViewById(R.id.action_delete_todo);
        buttonDelete.setVisibility(View.INVISIBLE);
        View buttonSave = findViewById(R.id.action_save_todo);
        buttonSave.setVisibility(View.VISIBLE);
        View buttonCancel = findViewById(R.id.action_cancel_todo);
        buttonCancel.setVisibility(View.VISIBLE);




    }

    private void cancelTodoEdit(){

    }

    private void deleteTodo(){


    }

    private void updateTodo(){


    }
}
