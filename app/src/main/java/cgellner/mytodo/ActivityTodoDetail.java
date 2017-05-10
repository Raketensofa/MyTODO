package cgellner.mytodo;


import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;



public class ActivityTodoDetail extends Activity {

    private Bundle extraData;
    private MenuItem itemEdit;
    private MenuItem itemDelete;
    private MenuItem itemCancel;
    private MenuItem itemSave;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.todo_detail_view);

        android.widget.Toolbar toolbar = (android.widget.Toolbar)findViewById(R.id.toolbar_details);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("TODO Details");

        extraData = getIntent().getExtras();
        setTodoDataToElements(extraData);
    }

    //TODO
    private void setTodoDataToElements(Bundle data){



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        itemEdit = menu.add(Menu.NONE,
                R.id.action_edit_todo,
                1, "Bearbeiten");
        itemEdit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemEdit.setIcon(R.drawable.ic_mode_edit_white_24dp);



        itemDelete = menu.add(Menu.NONE,
                R.id.action_delete_todo,
                2, "Löschen");
        itemDelete.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemDelete.setIcon(R.drawable.ic_delete_white_24dp);


        itemCancel = menu.add(Menu.NONE,
                R.id.action_cancel_todo,
                3, "Abbrechen");
        itemCancel.setVisible(false);
        itemCancel.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemCancel.setIcon(R.drawable.ic_cancel_white_24dp);


        itemSave = menu.add(Menu.NONE,
                R.id.action_delete_todo,
                4, "Speichern");
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemSave.setVisible(false);
        itemSave.setIcon(R.drawable.ic_done_white_24dp);

        return super.onCreateOptionsMenu(menu);
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

            case R.id.action_todo_save:

                updateTodo();

           // case android.R.id.home:
             //   finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void startTodoEdit(){

        //Buttons in der Titelleiste anpassen

        itemDelete.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        itemDelete.setVisible(false);
        itemEdit.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        itemEdit.setVisible(false);
        itemCancel.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemCancel.setVisible(true);
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemSave.setVisible(true);




    }

    private void cancelTodoEdit(){

        itemDelete.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemDelete.setVisible(true);
        itemEdit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemEdit.setVisible(true);
        itemCancel.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        itemCancel.setVisible(false);
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        itemSave.setVisible(false);


    }

    private void deleteTodo(){


    }

    private void updateTodo(){


    }
}
