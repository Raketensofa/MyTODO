package cgellner.mytodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import database.Queries;
import elements.DateAndTimePicker;
import model.TodoItem;

public class TodoAddNewActivity extends Activity {

    private MenuItem itemCancel;
    private MenuItem itemSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail_activity_add_view);

        Toolbar toolbar = (Toolbar)findViewById(R.id.todo_form_toolbar);
        setActionBar(toolbar);
        getActionBar().setTitle("Neues TODO");

        setListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        createNewTodoMenu(menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

                case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void createNewTodoMenu(Menu menu){

        itemSave = menu.add(Menu.NONE,
                R.id.action_todo_save,
                1, "Speichern");
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemSave.setIcon(R.drawable.save_icon_345);

        itemSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //Eingegebene Daten an die MainActivity zur Verarbeitung uebergeben
                // CurrentTodo = readTodoDataFromComponents();

                Intent intent = new Intent();
                // CurrentTodo.putToIntentExtras(intent);
                setResult(R.integer.SAVE_TODO, intent);
                finish();

                return false;
            }
        });


        itemCancel = menu.add(Menu.NONE,
                R.id.action_cancel_todo,
                2, "Abbrechen");
        itemCancel.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemCancel.setIcon(R.drawable.ic_cancel_white_24dp);

        itemCancel.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                finish();
                return false;
            }
        });
    }


    private void setListener(){

        final TextView dateTextview = (TextView) findViewById(R.id.todo_detail_view_date_textview);
        dateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateTextview.setText(DateAndTimePicker.startDatePickerDailog(v));
            }
        });

        final TextView timeTextview = (TextView) findViewById(R.id.todo_detail_view_time_textview);
        timeTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeTextview.setText(DateAndTimePicker.startTimePickerDialog(timeTextview));
            }
        });
    }
}
