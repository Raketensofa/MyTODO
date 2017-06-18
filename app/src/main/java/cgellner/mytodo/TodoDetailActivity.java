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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import database.Queries;
import elements.DateAndTimePicker;
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


        final Switch isFav = (Switch)findViewById(R.id.todo_detail_view_favourite_switch);
        isFav.setVisibility(View.INVISIBLE);
        final TextView isFavText = (TextView)findViewById(R.id.todo_detail_view_favourite_textview);
        isFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isFavText.setText("Hohe Priorität");
                }else {
                    isFavText.setText("Normale Priorität");
                }
            }
        });


        final Switch isDone = (Switch)findViewById(R.id.todo_detail_view_done_switch);
        isDone.setVisibility(View.INVISIBLE);
        final TextView isDoneText = (TextView)findViewById(R.id.todo_detail_view_done_textview);
        isDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isDoneText.setText("Erledigt");
                }else {
                    isDoneText.setText("Nicht erledigt");
                }
            }
        });
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

                return true;
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

        getActionBar().setTitle("Todo Details");

        EditText itemName = (EditText) findViewById(R.id.todo_detail_view_name_edittext);
        EditText itemDesc = (EditText) findViewById(R.id.todo_detail_view_description_edittext);
        TextView itemDate = (TextView) findViewById(R.id.todo_detail_view_date_textview);
        TextView itemTime = (TextView) findViewById(R.id.todo_detail_view_time_textview);
        Switch itemFav = (Switch) findViewById(R.id.todo_detail_view_favourite_switch);
        TextView itemFavText = (TextView)findViewById(R.id.todo_detail_view_favourite_textview);
        Switch itemDone = (Switch) findViewById(R.id.todo_detail_view_done_switch);
        TextView itemDoneText = (TextView)findViewById(R.id.todo_detail_view_done_textview);


        ImageView addContac = (ImageView) findViewById(R.id.todo_detail_view_contact_add_icon);

        itemName.setText(todoItem.getName());
        itemDesc.setText(todoItem.getDescription());

        if(todoItem.getExpiry() > 0) {
            itemDate.setText(DateFormat.format("dd.MM.yyyy", new Date(todoItem.getExpiry())).toString());
            itemTime.setText(DateFormat.format("HH:mm", new Date(todoItem.getExpiry())).toString());
        }

        itemFav.setChecked(todoItem.getIsFavourite());
        if(todoItem.getIsFavourite()) {
            itemFavText.setText("Hohe Priorität");
        }else {
            itemFavText.setText("Normale Priorität");
        }

        itemDone.setChecked(todoItem.getIsDone());
        if(todoItem.getIsDone()) {
            itemDoneText.setText("Erledigt");
        }else {
            itemDoneText.setText("Nicht erledigt");
        }


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
        Switch itemDone = (Switch) findViewById(R.id.todo_detail_view_done_switch);

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
        if(isEditMode) {
            itemFav.setVisibility(View.VISIBLE);
        }else{
            itemFav.setVisibility(View.INVISIBLE);
        }
        itemDone.setClickable(isEditMode);
        if(isEditMode) {
            itemDone.setVisibility(View.VISIBLE);
        }else{
            itemDone.setVisibility(View.INVISIBLE);
        }

        if (isEditMode == true) {
            addContac.setVisibility(View.VISIBLE);
        } else {
            addContac.setVisibility(View.INVISIBLE);
        }
    }


    private void setListener() {

        final TextView dateTextview = (TextView) findViewById(R.id.todo_detail_view_date_textview);
        dateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateAndTimePicker.startDatePickerDailog(dateTextview, todoItem.getExpiry());
            }
        });

        final TextView timeTextview = (TextView) findViewById(R.id.todo_detail_view_time_textview);
        timeTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateAndTimePicker.startTimePickerDialog(timeTextview, todoItem.getExpiry());
            }
        });
    }


    private TodoItem readDataFromComponents(TodoItem item){

        EditText name = (EditText)findViewById(R.id.todo_detail_view_name_edittext);
        EditText descr = (EditText)findViewById(R.id.todo_detail_view_description_edittext);
        TextView date = (TextView)findViewById(R.id.todo_detail_view_date_textview);
        TextView time = (TextView)findViewById(R.id.todo_detail_view_time_textview);
        Switch isFav = (Switch)findViewById(R.id.todo_detail_view_favourite_switch);
        Switch itemDone = (Switch) findViewById(R.id.todo_detail_view_done_switch);

        item.setName(name.getText().toString());
        item.setDescription(descr.getText().toString());
        item.setIsFavourite(isFav.isChecked());
        item.setIsDone(itemDone.isChecked());

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

