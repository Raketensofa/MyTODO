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

import model.Todo;

public class ActivityTodoDetail extends Activity {

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

    private Todo CurrentTodo;
    private String ViewMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo_detail);
        Bundle extraData = getIntent().getExtras();

        initToolbar();

        //Detailansicht oder Formular fuer ein neues To-Do
        ViewMode = extraData.getString("type");
        if(ViewMode.equals("new")){
            initNewTodoView();
            setListener();

        }else if(ViewMode.equals("detail")){
            initDetailView();
            CurrentTodo = new Todo();
            CurrentTodo.setDataFromIntentExtras(extraData);
            setTodoDataToComponents();
            setComponentsEditMode(false);
        }
    }


    private void createNewTodoMenu(Menu menu){

        itemSave = menu.add(Menu.NONE,
                R.id.action_delete_todo,
                1, "Speichern");
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemSave.setIcon(R.drawable.ic_done_white_24dp);

        itemSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //Eingegebene Daten an die MainActivity zur Verarbeitung uebergeben
                CurrentTodo = readTodoDataFromComponents();

                Intent intent = new Intent();
                CurrentTodo.putToIntentExtras(intent);
                setResult(RESULT_OK, intent);
                finish();

                return false;
            }
        });

    }

    private void createTodoDetailStartMenu(Menu menu){

        itemEdit = menu.add(Menu.NONE,
                R.id.action_edit_todo,
                1, "Bearbeiten");
        itemEdit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemEdit.setIcon(R.drawable.ic_mode_edit_white_24dp);

        itemEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                setComponentsEditMode(true);

                itemEdit.setVisible(false);
                itemDelete.setVisible(false);
                itemSave.setVisible(true);
                itemCancel.setVisible(true);

                return false;
            }
        });



        itemDelete = menu.add(Menu.NONE,
                R.id.action_delete_todo,
                2, "Löschen");
        itemDelete.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemDelete.setIcon(R.drawable.ic_delete_white_24dp);
        itemDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //Sind Sie sich sicher-Dialog?
                //Wenn sicher, dann löschen aus DB


                return false;
            }
        });



        itemCancel = menu.add(Menu.NONE,
                R.id.action_cancel_todo,
                4, "Abbrechen");
        itemCancel.setVisible(false);
        itemCancel.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemCancel.setIcon(R.drawable.ic_cancel_white_24dp);
        itemCancel.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                setTodoDataToComponents();
                setComponentsEditMode(false);

                itemEdit.setVisible(true);
                itemDelete.setVisible(true);
                itemSave.setVisible(false);
                itemCancel.setVisible(false);

                return false;
            }
        });


        itemSave = menu.add(Menu.NONE,
                R.id.action_delete_todo,
                3, "Speichern");
        itemSave.setVisible(false);
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemSave.setIcon(R.drawable.ic_done_white_24dp);

        itemSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {




                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(ViewMode.equals("new")){

            createNewTodoMenu(menu);

        }else if(ViewMode.equals("detail")){

            createTodoDetailStartMenu(menu);
        }

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


    private void initToolbar(){

        //Initialisierung der Toolbar
        Toolbar toolbar = (android.widget.Toolbar)findViewById(R.id.todo_form_toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void initNewTodoView(){

        getActionBar().setTitle("Neues TODO");


    }


    private void initDetailView(){

        getActionBar().setTitle("TODO Details");


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
    }


    private Todo readTodoDataFromComponents(){

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


    private void setComponentsEditMode(boolean isEditable){

        EditText nameEdit = (EditText) findViewById(R.id.todo_name_edittext);
        nameEdit.setClickable(isEditable);
        nameEdit.setFocusable(isEditable);

        EditText decriptionEdit = (EditText) findViewById(R.id.todo_description_edittext);
        decriptionEdit.setClickable(isEditable);
        decriptionEdit.setFocusable(isEditable);

        TextView date = (TextView)findViewById(R.id.todo_date_textview);
        date.setClickable(isEditable);
        date.setFocusable(isEditable);

        TextView time = (TextView)findViewById(R.id.todo_time_textview);
        time.setClickable(isEditable);
        time.setFocusable(isEditable);

        CheckBox isfav = (CheckBox)findViewById(R.id.todo_isfavourite_checkbox);
        isfav.setClickable(isEditable);
        isfav.setFocusable(isEditable);

        CheckBox isdone = (CheckBox)findViewById(R.id.todo_isdone_checkbox);
        isdone.setClickable(isEditable);
        isdone.setFocusable(isEditable);
    }


    private void setTodoDataToComponents() {

        EditText nameEdit = (EditText) findViewById(R.id.todo_name_edittext);
        EditText decriptionEdit = (EditText) findViewById(R.id.todo_description_edittext);
        TextView date = (TextView)findViewById(R.id.todo_date_textview);
        TextView time = (TextView)findViewById(R.id.todo_time_textview);
        CheckBox isfav = (CheckBox)findViewById(R.id.todo_isfavourite_checkbox);
        CheckBox isdone = (CheckBox)findViewById(R.id.todo_isdone_checkbox);

        nameEdit.setText(CurrentTodo.getName());
        decriptionEdit.setText(CurrentTodo.getDescription());
        date.setText(CurrentTodo.getDeadlineDate());
        time.setText(CurrentTodo.getDeadlineTime());

        if(CurrentTodo.getIsFavourite() == 1){

            isfav.setChecked(true);
        }

        if(CurrentTodo.getIsDone() == 1){

            isdone.setChecked(true);
        }
    }


    private void startTodoEdit(){




    }


    private void cancelTodoEdit(){



    }


    private void deleteTodo(){


    }


    private void updateTodo(){


    }
}
