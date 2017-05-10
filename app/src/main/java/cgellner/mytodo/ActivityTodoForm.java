package cgellner.mytodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo_form);

        getActionBar().setDisplayHomeAsUpEnabled(true);

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
}
