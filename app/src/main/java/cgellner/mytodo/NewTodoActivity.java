package cgellner.mytodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import elements.HandleDetailImpl;
import model.TodoItem;

public class NewTodoActivity extends Activity{

    private String TAG = NewTodoActivity.class.getSimpleName();

    private MenuItem itemCancel;
    private MenuItem itemSave;
    private HandleDetailImpl operations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_and_add_todo_view);

        Toolbar toolbar = (Toolbar)findViewById(R.id.todo_form_toolbar);
        setActionBar(toolbar);
        getActionBar().setTitle(R.string.new_todo_label);

        operations = new HandleDetailImpl(this, null);

        Log.i(TAG, "onCreate()");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.i(TAG, "Create Options Menu ... ");
        createNewTodoMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i(TAG, "Options Item Selected: " + item.getItemId());

        switch (item.getItemId()) {

                case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == R.integer.PICK_CONTACT_REQUEST && resultCode ==  RESULT_OK){

            Log.i(TAG, "onActivityResult - Pick contact");

            operations.addNewContactToList(data.getData());
        }
    }


    private void createNewTodoMenu(Menu menu){

        itemSave = menu.add(Menu.NONE,
                R.id.action_todo_save,
                1, R.string.save_label);
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        itemSave.setIcon(R.drawable.save_icon_345);

        itemSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                TodoItem newItem = operations.readTodoDataFromComponents();

                Intent intent = new Intent();
                intent.putExtra(String.valueOf(R.string.TODO_ITEM), newItem);
                setResult(R.integer.SAVE_TODO, intent);
                finish();
                return false;
            }
        });


        itemCancel = menu.add(Menu.NONE,
                R.id.action_cancel_todo,
                2, R.string.cancel_label);
        itemCancel.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        itemCancel.setIcon(R.drawable.ic_cancel_white_24dp);
        itemCancel.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        });
    }
}
