package cgellner.mytodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import database.Queries;
import model.TodoItem;

public class TodoDetailActivity extends AppCompatActivity {

    private MenuItem itemEdit;
    private MenuItem itemDelete;
    private MenuItem itemCancel;
    private MenuItem itemSave;

    private TodoItem CurrentTodo;
    private int ViewMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo_detail);



        Bundle extraData = getIntent().getExtras();

        initToolbar();

        //Detailansicht oder Formular fuer ein neues To-Do
        ViewMode = extraData.getInt(String.valueOf(R.string.view_mode));
        if(ViewMode == R.integer.VIEW_MODE_NEW){
            initNewTodoView();
            setListener();

        }else if(ViewMode == R.integer.VIEW_MODE_DETAIL){
            initDetailView();
            CurrentTodo = new TodoItem();
            CurrentTodo.setDataFromIntentExtras(extraData);
            //setTodoDataToList();
            // setComponentsEditMode(false);
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
               // CurrentTodo = readTodoDataFromComponents();

                Intent intent = new Intent();
                CurrentTodo.putToIntentExtras(intent);
                setResult(R.integer.SAVE_TODO, intent);
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

                //setComponentsEditMode(true);

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

                //Zeigt einen AlertDialog an und prueft, ob tatsaechlich geloescht werden soll
                //Wenn geloescht werden soll, wird die ID des to-dos an die MainActitivy ueber einen Intent zureckgegeben
                //Das Loeschen in der DB wird in der MainActivity dann ausgefuehrt
                showDeleteAcceptDialog();

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

                //setTodoDataToList();
                //setComponentsEditMode(false);

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

                Intent intent = new Intent();
                long id = CurrentTodo.getId();
               // CurrentTodo = readTodoDataFromComponents();
                CurrentTodo.setId(id);
                CurrentTodo.putToIntentExtras(intent);
                setResult(R.integer.UPDATE_TODO, intent);
                finish();

                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(ViewMode == R.integer.VIEW_MODE_NEW){

            createNewTodoMenu(menu);

        }else if(ViewMode == R.integer.VIEW_MODE_DETAIL){

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

      /**
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
        });*/
    }


    //TODO
    private void setTodoDataToList(View mainView, TodoItem todo) {

       // LayoutInflater inflater = getLayoutInflater();

        //ListView listView = (ListView)mainView.findViewById(R.id.listview_detail_todo);

       /** ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listview_item_group_header, listView, false);
        TextView headerText = (TextView)header.findViewById(R.id.textview_header_name);
        headerText.setText("Informationen");
        listView.addHeaderView(header, null, false);*/




    }


    private void showDeleteAcceptDialog(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("TODO löschen");

        alertDialogBuilder
                .setMessage("TODO tatsächlich löschen?")
                .setCancelable(false)
                .setPositiveButton("Ja",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        Intent intent = new Intent();
                        intent.putExtra(Queries.COLUMN_ID, CurrentTodo.getId());
                        setResult(R.integer.DELETE_TODO, intent);
                        finish();
                    }
                })
                .setNegativeButton("Nein",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
