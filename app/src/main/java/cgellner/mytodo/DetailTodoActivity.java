package cgellner.mytodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import java.util.Date;

import database.Columns;
import elements.HandleDetailImpl;
import model.TodoItem;

public class DetailTodoActivity extends Activity {

    private static String TAG = DetailTodoActivity.class.getSimpleName();

    private MenuItem itemEdit;
    private MenuItem itemDelete;
    private MenuItem itemCancel;
    private MenuItem itemSave;

    private TodoItem currentTodoItem;
    private HandleDetailImpl operations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_and_add_todo_view);

        currentTodoItem = (TodoItem) getIntent().getExtras().getSerializable(String.valueOf(R.string.TODO_ITEM));

        initToolbar();

        operations = new HandleDetailImpl(this, currentTodoItem);

        setTodoDataToComponents();
    }


    private void createTodoDetailStartMenu(Menu menu) {


        itemEdit = menu.add(Menu.NONE,
                R.id.action_edit_todo,
                1, R.string.edit_label);
        itemEdit.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
                2, R.string.delete_label);
        itemDelete.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
                4, R.string.cancel_label);
        itemCancel.setVisible(false);
        itemCancel.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
                3, R.string.save_label);
        itemSave.setVisible(false);
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        itemSave.setIcon(R.drawable.save_icon_345);

        itemSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                TodoItem uptatedItem = operations.readTodoDataFromComponents();
                uptatedItem.setId(currentTodoItem.getId());
                Intent intent = new Intent();
                intent.putExtra(String.valueOf(R.string.TODO_ITEM), uptatedItem);
                setResult(R.integer.UPDATE_TODO, intent);
                finish();

                return true;
            }
        });
    }


    private void startDeleteTodoProcess() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(R.string.delete_todo_label);

        alertDialogBuilder
                .setMessage(R.string.delete_todo_label_accept)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent();
                        intent.putExtra(Columns.id.toString(), currentTodoItem.getId());
                        setResult(R.integer.DELETE_TODO, intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == R.integer.PICK_CONTACT_REQUEST && resultCode ==  RESULT_OK){
            operations.addNewContactToList(data.getData());
        }
    }


    private void initToolbar() {

        Toolbar toolbar = (android.widget.Toolbar) findViewById(R.id.todo_form_toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true); //TODO Farbe weiß eintellen?

    }


    private void setTodoDataToComponents() {

        EditText itemName = (EditText) findViewById(R.id.todo_detail_view_name_edittext);
        EditText itemDesc = (EditText) findViewById(R.id.todo_detail_view_description_edittext);
        TextView itemDate = (TextView) findViewById(R.id.todo_detail_view_date_textview);
        TextView itemTime = (TextView) findViewById(R.id.todo_detail_view_time_textview);
        Switch itemFav = (Switch) findViewById(R.id.todo_detail_view_favourite_switch);
        TextView itemFavText = (TextView) findViewById(R.id.todo_detail_view_favourite_textview);
        Switch itemDone = (Switch) findViewById(R.id.todo_detail_view_done_switch);
        TextView itemDoneText = (TextView) findViewById(R.id.todo_detail_view_done_textview);


        ImageView addContac = (ImageView) findViewById(R.id.todo_detail_view_contact_add_icon);

        itemName.setText(currentTodoItem.getName());
        itemDesc.setText(currentTodoItem.getDescription());

        if (currentTodoItem.getExpiry() > 0) {
            itemDate.setText(DateFormat.format("dd.MM.yyyy", new Date(currentTodoItem.getExpiry())).toString());
            itemTime.setText(DateFormat.format("HH:mm", new Date(currentTodoItem.getExpiry())).toString());
        }

        itemFav.setChecked(currentTodoItem.getIsFavourite());
        if (currentTodoItem.getIsFavourite()) {
            itemFavText.setText("Hohe Priorität");
        } else {
            itemFavText.setText("Normale Priorität");
        }

        itemDone.setChecked(currentTodoItem.getIsDone());
        if (currentTodoItem.getIsDone()) {
            itemDoneText.setText("Erledigt");
        } else {
            itemDoneText.setText("Nicht erledigt");
        }


        if(currentTodoItem.getContacts() != null && currentTodoItem.getContacts().size() > 0) {
            operations.getContactListViewAdapter().clear();
            for (String str : currentTodoItem.getContacts()) {

                if(str != null) {
                    Uri uri = Uri.parse(str);
                    Log.i(TAG, uri.toString());
                    operations.addNewContactToList(uri);
                }
            }
        }

        addContac.setClickable(true);

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
        if (isEditMode) {
            itemFav.setVisibility(View.VISIBLE);
        } else {
            itemFav.setVisibility(View.INVISIBLE);
        }
        itemDone.setClickable(isEditMode);
        if (isEditMode) {
            itemDone.setVisibility(View.VISIBLE);
        } else {
            itemDone.setVisibility(View.INVISIBLE);
        }

        if (isEditMode) {
            addContac.setVisibility(View.VISIBLE);
        } else {
            addContac.setVisibility(View.INVISIBLE);
        }

        operations.showContactItemEements(isEditMode);
    }


}

