package cgellner.mytodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import database.Queries;
import model.Todo;

public class ActivityTodoForm extends Activity {

    private Button buttonSave;
    private EditText nameEdittext;
    private EditText descriptionEdittext;
    private TextView dateTextview;
    private TextView timeTextview;
    private CheckBox isFavouriteCheckbox;

    private MenuItem itemEdit;
    private MenuItem itemDelete;
    private MenuItem itemCancel;
    private MenuItem itemSave;

    private Bundle extraData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo_form);

        Toolbar toolbar = (android.widget.Toolbar)findViewById(R.id.todo_form_toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("TODO Details");

        extraData = getIntent().getExtras();
        setTodoDataToElements(extraData);


        setListener();
    }



    private void setListener(){

        //DatePickerDialog oeffnen bei Klick auf Datum-Textbox
        dateTextview = (TextView) findViewById(R.id.todo_date_textview);
        dateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar myCalendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd.MM.yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                        dateTextview.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                new DatePickerDialog(v.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        //TimePickerDialog oeffnen bei Klick auf Uhrzeit-Textbox
        timeTextview = (TextView) findViewById(R.id.todo_time_textview);
        timeTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar myCalendar = Calendar.getInstance();
                final TimePickerDialog.OnTimeSetListener timePickerListener =
                        new TimePickerDialog.OnTimeSetListener() {

                            public void onTimeSet(TimePicker view, int selectedHour,
                                                  int selectedMinute) {

                                myCalendar.set(Calendar.HOUR, selectedHour);
                                myCalendar.set(Calendar.MINUTE, selectedMinute);

                                String myFormat = "HH:mm";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                                timeTextview.setText(sdf.format(myCalendar.getTime()));
                            }
                        };

                new TimePickerDialog(v.getContext(), timePickerListener, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), true).show();

            }
        });

        buttonSave = (Button)findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Eingegebene Daten an die MainActivity zur Verarbeitung uebergeben
                Todo newTodo = readTodoData();

                if(newTodo != null) {

                    Intent intent = new Intent();
                    intent.putExtra(Queries.COLUMN_NAME, newTodo.getName());
                    intent.putExtra(Queries.COLUMN_DESCRIPTION, newTodo.getDescription());
                    intent.putExtra(Queries.COLUMN_ISFAVOURITE, newTodo.getIsFavourite());
                    intent.putExtra(Queries.COLUMN_ISDONE, newTodo.getIsDone());
                    intent.putExtra(Queries.COLUMN_DEADLINE_DATE, newTodo.getDeadlineDate());
                    intent.putExtra(Queries.COLUMN_DEADLINE_TIME, newTodo.getDeadlineTime());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }


    private Todo readTodoData(){

        Todo todoItem = null;

        try {

            nameEdittext = (EditText) findViewById(R.id.todo_name_edittext);
            descriptionEdittext = (EditText) findViewById(R.id.todo_description_edittext);
            dateTextview = (TextView) findViewById(R.id.todo_date_textview);
            timeTextview = (TextView) findViewById(R.id.todo_time_textview);
            isFavouriteCheckbox = (CheckBox) findViewById(R.id.todo_isfavourite_checkbox);


            String name = nameEdittext.getText().toString();
            String description = descriptionEdittext.getText().toString();
            String date = dateTextview.getText().toString();
            String time = timeTextview.getText().toString();

            int fav = 0;
            if (isFavouriteCheckbox.isChecked()) {
                fav = 1;
            }

            todoItem = new Todo(name, description, 0, fav, date, time);

        }catch (Exception ex){


            return todoItem;
        }

        return todoItem;
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


        //IDEE: Menue neu erstellen

        //Buttons in der Titelleiste anpassen
        // menu.findItem(R.id.action_edit_todo).setVisible(false);
        // menu.findItem(R.id.action_delete_todo).setVisible(false);
        // menu.findItem(R.id.action_cancel_todo).setVisible(true);
        // menu.findItem(R.id.action_todo_save).setVisible(true);

    }

    private void cancelTodoEdit(){

        // menu.findItem(R.id.action_edit_todo).setVisible(true);
        // menu.findItem(R.id.action_delete_todo).setVisible(true);
        // menu.findItem(R.id.action_cancel_todo).setVisible(false);
        // menu.findItem(R.id.action_todo_save).setVisible(false);

    }

    private void deleteTodo(){


    }

    private void updateTodo(){


    }
}
