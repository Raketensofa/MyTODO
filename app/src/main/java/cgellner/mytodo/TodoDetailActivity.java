package cgellner.mytodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import database.Queries;
import model.TodoItem;

public class TodoDetailActivity extends Activity {

    private static String TAG = TodoDetailActivity.class.getSimpleName();

    private MenuItem itemEdit;
    private MenuItem itemDelete;
    private MenuItem itemCancel;
    private MenuItem itemSave;

    private TodoItem todoItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.todo_detail_activity_add_view);

        todoItem = new TodoItem();
        todoItem = (TodoItem) getIntent().getExtras().getSerializable("TODO_ITEM");

        initToolbar();

        setListener();

        setTodoDataToComponents();


        // setComponentsEditMode(false);


    }


    private void createTodoDetailStartMenu(Menu menu) {


        itemEdit = menu.add(Menu.NONE,
                R.id.action_edit_todo,
                1, "Bearbeiten");
        itemEdit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemEdit.setIcon(R.drawable.ic_mode_edit_white_24dp);

        itemEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                setEditMode(true);

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
                startDeleteTodoProcess();

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

                itemEdit.setVisible(true);
                itemDelete.setVisible(true);
                itemSave.setVisible(false);
                itemCancel.setVisible(false);

                return false;
            }
        });


        itemSave = menu.add(Menu.NONE,
                R.id.action_todo_save,
                3, "Speichern");
        itemSave.setVisible(false);
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemSave.setIcon(R.drawable.save_icon_345);

        itemSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                TodoItem uptatedItem = readDataFromComponents(todoItem);
                Intent intent = new Intent();
                intent.putExtra("TODO_ITEM", uptatedItem);
                setResult(R.integer.UPDATE_TODO, intent);
                finish();

                return false;
            }
        });
    }


    private void startDeleteTodoProcess() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("TODO löschen");

        alertDialogBuilder
                .setMessage("TODO tatsächlich löschen?")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent();
                        intent.putExtra(Queries.COLUMN_ID, todoItem.getId());
                        setResult(R.integer.DELETE_TODO, intent);
                        finish();
                    }
                })
                .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        createTodoDetailStartMenu(menu);

        itemEdit.setVisible(true);
        itemDelete.setVisible(true);
        itemSave.setVisible(false);
        itemCancel.setVisible(false);

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


    private void initToolbar() {

        //Initialisierung der Toolbar
        Toolbar toolbar = (android.widget.Toolbar) findViewById(R.id.todo_form_toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true); //TODO Farbe weiß eintellen?

    }

    private void setTodoDataToComponents() {

        getActionBar().setTitle("TODO Details");

        EditText itemName = (EditText) findViewById(R.id.todo_detail_view_name_edittext);
        EditText itemDesc = (EditText) findViewById(R.id.todo_detail_view_description_edittext);
        TextView itemDate = (TextView) findViewById(R.id.todo_detail_view_date_textview);
        TextView itemTime = (TextView) findViewById(R.id.todo_detail_view_time_textview);
        Switch itemFav = (Switch) findViewById(R.id.todo_detail_view_favourite_switch);
        ImageView addContac = (ImageView) findViewById(R.id.todo_detail_view_contact_add_icon);
        //TODO isDone element

        itemName.setText(todoItem.getName());
        itemDesc.setText(todoItem.getDescription());


        itemDate.setText(DateFormat.format("dd.MM.yyyy", new Date(todoItem.getExpiry())).toString());
        itemTime.setText(DateFormat.format("HH:mm", new Date(todoItem.getExpiry())).toString());

        itemFav.setChecked(todoItem.getIsFavourite());

        addContac.setClickable(true);
        addContac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO
            }
        });

        setEditMode(false);
    }

    private void setEditMode(boolean isEditMode) {

        EditText itemName = (EditText) findViewById(R.id.todo_detail_view_name_edittext);
        EditText itemDesc = (EditText) findViewById(R.id.todo_detail_view_description_edittext);
        TextView itemDate = (TextView) findViewById(R.id.todo_detail_view_date_textview);
        TextView itemTime = (TextView) findViewById(R.id.todo_detail_view_time_textview);
        Switch itemFav = (Switch) findViewById(R.id.todo_detail_view_favourite_switch);
        //TODO: isdone
        ImageView addContac = (ImageView) findViewById(R.id.todo_detail_view_contact_add_icon);

        itemName.setEnabled(isEditMode);
        itemName.setTextColor(Color.BLACK);
        itemDesc.setEnabled(isEditMode);
        itemDesc.setTextColor(Color.BLACK);
        itemDate.setEnabled(isEditMode);
        itemDate.setTextColor(Color.BLACK);
        itemTime.setEnabled(isEditMode);
        itemTime.setTextColor(Color.BLACK);
        itemFav.setClickable(isEditMode);
        //TODO: isdone

        if (isEditMode == true) {
            addContac.setVisibility(View.VISIBLE);
        } else {
            addContac.setVisibility(View.INVISIBLE);
        }
    }


    private void setListener() {

        /**
         //DatePickerDialog oeffnen bei Klick auf Datum-Textbox
         dateTextview = (TextView) findViewById(R.id.todo_date_textview);
         dateTextview.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {

        dateTextView.set(DateAndTimePicker.startDatePickerDialog(dateTextview));
        }
        });

         //TimePickerDialog oeffnen bei Klick auf Uhrzeit-Textbox
         timeTextview = (TextView) findViewById(R.id.todo_time_textview);
         timeTextview.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {

        timeTextView.set(DateAndTimePicker.startTimePickerDialog(timeTextview));
        }
        });*/
    }


    private TodoItem readDataFromComponents(TodoItem item){

        EditText name = (EditText)findViewById(R.id.todo_detail_view_name_edittext);
        EditText descr = (EditText)findViewById(R.id.todo_detail_view_description_edittext);
        TextView date = (TextView)findViewById(R.id.todo_detail_view_date_textview);
        TextView time = (TextView)findViewById(R.id.todo_detail_view_time_textview);
        Switch isFav = (Switch)findViewById(R.id.todo_detail_view_favourite_switch);

        item.setName(name.getText().toString());
        item.setDescription(descr.getText().toString());
        item.setIsFavourite(isFav.isChecked());

        long expiry = 0;

        try {

            String dateTime = date.getText().toString() + " " + time.getText().toString();
            Date expiryDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(dateTime);
            expiry = expiryDate.getTime();

        }catch (ParseException ex){
            expiry = 0;
        }
        item.setExpiry(expiry);


        //TODO Kontakte

        return item;
    }
}

