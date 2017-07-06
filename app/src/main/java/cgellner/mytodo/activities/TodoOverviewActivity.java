package cgellner.mytodo.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cgellner.mytodo.basic_operations.MyTodoApplication;
import cgellner.mytodo.R;
import cgellner.mytodo.database.Columns;
import cgellner.mytodo.database.interfaces.ITodoItemCRUD;
import cgellner.mytodo.database.interfaces.ITodoItemCRUDAsync;
import cgellner.mytodo.elements.TodoListSortComparator;
import cgellner.mytodo.model.TodoItem;

public class TodoOverviewActivity extends Activity {

    private static String TAG = TodoOverviewActivity.class.getSimpleName();

    private ListView listviewTodos;
    private ArrayAdapter<TodoItem> todoListViewAdapter;

    private MenuItem itemNew;
    private MenuItem itemSort;

    private int SortMode; //1 = Faelligkeit+Wichtigkeit  - 2= Wichtigkeit+Faelligkeit

    private MyTodoApplication myTodoApplication;
    private ITodoItemCRUDAsync remoteCrudOperations;
    private ITodoItemCRUD localCrudOperations;

    private ProgressDialog progressDialog;

    public class ItemViewHolder {

        public long itemId;

        public TextView itemName;
        public TextView itemDescription;
        public TextView itemDeadline;
        public ImageView itemIsDone;
        public ImageView itemIsFavourite;

        public View basicDataView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo_overview);

        progressDialog = new ProgressDialog(this);

        initToolbar();

        SortMode = R.integer.SORT_MODE_DEADLINE_FAVOURITE;  //default

        listviewTodos = (ListView) findViewById(R.id.listview_todolist);
        todoListViewAdapter = new ArrayAdapter<TodoItem>(this, R.layout.todo_list_item) {


            @NonNull
            @Override
            public View getView(int position, View itemView, ViewGroup parent) {

                //Initialsierung der View-Elemente
                if (itemView == null) {

                    itemView = getLayoutInflater().inflate(R.layout.todo_list_item, null);
                    TextView itemNameView = (TextView) itemView.findViewById(R.id.todo_textview_name);
                    TextView itemDescriptionView = (TextView) itemView.findViewById(R.id.todo_textview_deadline);
                    TextView itemDeadlineView = (TextView) itemView.findViewById(R.id.todo_textview_deadline);
                    ImageView itemIsDoneView = (ImageView) itemView.findViewById(R.id.imageview_todo_isdone_2);
                    ImageView itemIsFavouriteView = (ImageView) itemView.findViewById(R.id.imageview_todo_isfavourite);

                    View basicDataView = itemView.findViewById(R.id.labels_layout);

                    ItemViewHolder itemViewHolder = new ItemViewHolder();
                    itemViewHolder.itemName = itemNameView;
                    itemViewHolder.itemDescription = itemDescriptionView;
                    itemViewHolder.itemDeadline = itemDeadlineView;
                    itemViewHolder.itemIsDone = itemIsDoneView;
                    itemViewHolder.itemIsFavourite = itemIsFavouriteView;

                    itemViewHolder.basicDataView = basicDataView;

                    itemView.setTag(itemViewHolder);
                }

                //Zuweisung der View-Element-Inhalte
                final TodoItem todoItem = getItem(position);
                final ItemViewHolder viewHolder = (ItemViewHolder) itemView.getTag();

                setViewHolderData(viewHolder, todoItem);

                return itemView;
            }
        };

        listviewTodos.setAdapter(todoListViewAdapter);
        todoListViewAdapter.setNotifyOnChange(true);

        myTodoApplication = ((MyTodoApplication)getApplication());
        localCrudOperations = myTodoApplication.getLocalCrud();
        remoteCrudOperations = myTodoApplication.getCRUDOperationsImpl();

        readItemsAndFillListView();


        if(!myTodoApplication.hasPermission(this, Manifest.permission.READ_CONTACTS)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
        }
        if(!myTodoApplication.hasPermission(this, Manifest.permission.SEND_SMS)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == R.integer.NEWTODO_ACTIVITY && resultCode == R.integer.SAVE_TODO) {

            TodoItem newTodo = (TodoItem) data.getSerializableExtra(String.valueOf(R.string.TODO_ITEM));
            Log.i(TAG, "New Todo: " + newTodo.toString());
            createAndShowItem(newTodo);

        }else
        if (requestCode == R.integer.DETAIL_ACTIVITY) {
              if (resultCode == R.integer.DELETE_TODO) {

                  long todoId = data.getLongExtra(Columns.id.toString(), -1);
                  deleteAndRemoveTodoItem(todoId);

            } else if (resultCode == R.integer.UPDATE_TODO) {

                  TodoItem updatedItem = (TodoItem) data.getSerializableExtra(String.valueOf(R.string.TODO_ITEM));
                  updateTodoItem(updatedItem);
                  Toast.makeText(getApplicationContext(), "Änderungen gespeichert", Toast.LENGTH_SHORT).show();
            }
        }


        sortTodoList();
    }


    private TodoItem findTodoItemInList(long id){

        for(int i = 0;  i<todoListViewAdapter.getCount(); i++){

            if(todoListViewAdapter.getItem(i).getId() == id){

                return todoListViewAdapter.getItem(i);
            }
        }

        return null;
    }


    private void updateTodoItemInList(TodoItem item){

        for(int i = 0;  i < todoListViewAdapter.getCount(); i++){

            if(todoListViewAdapter.getItem(i).getId() == item.getId()){

                todoListViewAdapter.getItem(i).setExpiry(item.getExpiry());
                todoListViewAdapter.getItem(i).setIsFavourite(item.getIsFavourite());
                todoListViewAdapter.getItem(i).setIsDone(item.getIsDone());
                todoListViewAdapter.getItem(i).setName(item.getName());
                todoListViewAdapter.getItem(i).setDescription(item.getDescription());
                todoListViewAdapter.getItem(i).setContacts(item.getContacts());
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        createToolbarButtons(menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Detailansicht starten
     */
    private void startShowingDetailView(TodoItem item) {

        Intent intent = new Intent(this, DetailTodoActivity.class);
        intent.putExtra(String.valueOf(R.string.TODO_ITEM), item);
        startActivityForResult(intent, R.integer.DETAIL_ACTIVITY);
    }


    private void createAndShowItem(TodoItem item) {

        progressDialog.show();

        TodoItem createdLocalItem = localCrudOperations.createTodoItem(item);

        if (createdLocalItem.getId() > -1) {

            if (myTodoApplication.isConnToRemote()) {

                remoteCrudOperations.createTodoItem(createdLocalItem, new ITodoItemCRUDAsync.CallbackFunction<TodoItem>() {

                    @Override
                    public void process(TodoItem result) {

                        Log.i(TAG, "Remote Item created: " + result.toString());
                        todoListViewAdapter.add(result);
                        sortTodoList();
                        progressDialog.hide();
                    }
                });

            }else{

                todoListViewAdapter.add(createdLocalItem);
            }
        }

        sortTodoList();

        progressDialog.hide();
    }


    private void updateTodoItem(final TodoItem item) {

        progressDialog.show();

        TodoItem updatedLocalItem = localCrudOperations.updateTodoItem(item);
        Log.i(TAG, "updateTodoItem: " + updatedLocalItem.toString());

        if (updatedLocalItem != null) {

            if (myTodoApplication.isConnToRemote()) {

                remoteCrudOperations.updateTodoItem(item, new ITodoItemCRUDAsync.CallbackFunction<TodoItem>() {
                    @Override
                    public void process(TodoItem result) {

                        Log.i(TAG, "Remote Item updated: " + result.toString());
                        updateTodoItemInList(result);
                        sortTodoList();
                        progressDialog.hide();
                    }
                });

            }else{

                updateTodoItemInList(updatedLocalItem);
            }
        }

        sortTodoList();
        progressDialog.hide();
    }


    private void readItemsAndFillListView() {

        progressDialog.show();

        if (((MyTodoApplication) getApplication()).isConnToRemote()) {

            remoteCrudOperations.readAllTodoItems(new ITodoItemCRUDAsync.CallbackFunction<List<TodoItem>>() {
                @Override
                public void process(List<TodoItem> result) {

                    if (result != null) {
                        for (TodoItem item : result) {
                            todoListViewAdapter.add(item);
                        }
                        sortTodoList();
                    }
                }
            });

        }else{

           List<TodoItem> items = localCrudOperations.readAllTodoItems();

            if (items != null) {
                for (TodoItem item : items) {
                    todoListViewAdapter.add(item);
                }
                sortTodoList();
            }
        }

        progressDialog.hide();
    }


    private void deleteAndRemoveTodoItem(final long todoId) {

        progressDialog.show();

        boolean deletedLocalItem = localCrudOperations.deleteTodoItem(todoId);

        if (deletedLocalItem) {

            if (myTodoApplication.isConnToRemote()) {

                remoteCrudOperations.deleteTodoItem(todoId, new ITodoItemCRUDAsync.CallbackFunction<Boolean>() {

                    @Override
                    public void process(Boolean deleted) {

                        Log.i(TAG, "Remote Item gelöscht: " + deleted);

                        progressDialog.hide();
                        if (deleted) {
                            todoListViewAdapter.remove(findTodoItemInList(todoId));
                            sortTodoList();
                            Toast.makeText(getApplicationContext(), "Todo gelöscht.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Fehler beim Löschen", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else{

                todoListViewAdapter.remove(findTodoItemInList(todoId));
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), "Todo gelöscht.", Toast.LENGTH_SHORT).show();
            }

        }else{

            progressDialog.hide();
            Toast.makeText(getApplicationContext(), "Fehler beim Löschen", Toast.LENGTH_SHORT).show();
        }

        sortTodoList();
    }


    private void createToolbarButtons(Menu menu) {

        itemNew = menu.add(Menu.NONE,
                R.id.action_new_todo,
                1, "Neu");
        itemNew.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemNew.setIcon(R.drawable.ic_add_white_24dp);

        itemNew.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                showFormForNewTodo();
                return false;
            }
        });


        itemSort = menu.add(Menu.NONE,
                R.id.action_sort_todolist,
                2, "Sortiermodus");

        if (SortMode == R.integer.SORT_MODE_DEADLINE_FAVOURITE) {

            itemSort.setIcon(R.drawable.ic_swap_vert_white_24dp);

        } else if (SortMode == R.integer.SORT_MODE_FAVOURITE_DEADLINE) {

            itemSort.setIcon(R.drawable.ic_swap_vertical_circle_white_24dp);
        }

        itemSort.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        itemSort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (SortMode == R.integer.SORT_MODE_DEADLINE_FAVOURITE) {   //Datum + Wichtigkeit

                    SortMode = R.integer.SORT_MODE_FAVOURITE_DEADLINE;
                    item.setIcon(R.drawable.ic_swap_vertical_circle_white_24dp);

                    Toast.makeText(getApplicationContext(), "Sortierung nach Wichtigkeit + Fälligkeit", Toast.LENGTH_SHORT).show();

                } else if (SortMode == R.integer.SORT_MODE_FAVOURITE_DEADLINE) { //Wichtigkeit + Datum

                    SortMode = R.integer.SORT_MODE_DEADLINE_FAVOURITE;
                    item.setIcon(R.drawable.ic_swap_vert_white_24dp);
                    Toast.makeText(getApplicationContext(), "Sortierung nach Fälligkeit + Wichtigkeit", Toast.LENGTH_SHORT).show();
                }


                sortTodoList();

                return false;
            }
        });
    }


    private void showFormForNewTodo() {

        Intent intent = new Intent(getBaseContext(), NewTodoActivity.class);
        startActivityForResult(intent, R.integer.NEWTODO_ACTIVITY);
    }


    private void initToolbar() {

        Toolbar toolbar = (android.widget.Toolbar) findViewById(R.id.todo_main_toolbar);
        toolbar.setTitle("MyTODO");
        setActionBar(toolbar);
    }


    private void markExpiredTodoItems(TextView view, long expiry) {

        if (expiry > 0) {

            try {

                Calendar cal = Calendar.getInstance();

                if (expiry < cal.getTime().getTime()) {

                    view.setTextColor(Color.WHITE);
                    view.setBackgroundColor(Color.RED);

                } else {

                    view.setBackgroundColor(Color.WHITE);
                    view.setTextColor(Color.DKGRAY);
                }

            } catch (Exception ex) {


            }

        }else{

            view.setBackgroundColor(Color.WHITE);
            view.setTextColor(Color.WHITE);
        }
    }


    private void setViewHolderData(final ItemViewHolder viewHolder, final TodoItem todoItem){

        viewHolder.itemId = todoItem.getId();
        viewHolder.itemName.setText(todoItem.getName());

        if(todoItem.getExpiry() > 0) {
            String dateText = DateFormat.format("dd.MM.yyyy", new Date(todoItem.getExpiry())).toString();
            String timeText = DateFormat.format("HH:mm", new Date(todoItem.getExpiry())).toString();
            viewHolder.itemDeadline.setText(dateText + " " + timeText);
            markExpiredTodoItems(viewHolder.itemDeadline, todoItem.getExpiry());

        }else{
            viewHolder.itemDeadline.setText("");
        }

        if (todoItem.getIsFavourite() == true) {
            viewHolder.itemIsFavourite.setTag("IS_FAV");
            viewHolder.itemIsFavourite.setImageResource(R.drawable.yellow_star_1);
        } else if (todoItem.getIsFavourite() == false) {
            viewHolder.itemIsFavourite.setTag("IS_NOT_FAV");
            viewHolder.itemIsFavourite.setImageResource(R.drawable.icons8sternfilled50);
        }

        if (todoItem.getIsDone() == true) {
            viewHolder.itemIsDone.setTag("IS_DONE");
            viewHolder.itemIsDone.setImageResource(R.drawable.filled_checkbox_23);
        } else if (todoItem.getIsDone() == false) {
            viewHolder.itemIsDone.setTag("IS_NOT_DONE");
            viewHolder.itemIsDone.setImageResource(R.drawable.unfilled_checkbox34);
        }


        viewHolder.itemIsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isChecked = todoItem.getIsDone();

                if (viewHolder.itemIsDone.getTag().equals("IS_DONE")) {

                    viewHolder.itemIsDone.setImageResource(R.drawable.unfilled_checkbox34);
                    isChecked = false;
                    viewHolder.itemIsDone.setTag("IS_NOT_DONE");

                } else if (viewHolder.itemIsDone.getTag().equals("IS_NOT_DONE")) {

                    viewHolder.itemIsDone.setImageResource(R.drawable.filled_checkbox_23);
                    isChecked = true;
                    viewHolder.itemIsDone.setTag("IS_DONE");
                }

                todoItem.setIsDone(isChecked);
                updateTodoItem(todoItem);

                sortTodoList();

            }
        });

        viewHolder.itemIsFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isChecked = todoItem.getIsFavourite();

                if (viewHolder.itemIsFavourite.getTag().equals("IS_FAV")) {

                    viewHolder.itemIsFavourite.setImageResource(R.drawable.icons8sternfilled50);
                    viewHolder.itemIsFavourite.setTag("IS_NOT_FAV");
                    isChecked = false;

                } else if (viewHolder.itemIsFavourite.getTag().equals("IS_NOT_FAV")) {

                    viewHolder.itemIsFavourite.setImageResource(R.drawable.yellow_star_1);
                    viewHolder.itemIsFavourite.setTag("IS_FAV");
                    isChecked = true;
                }

                todoItem.setIsFavourite(isChecked);
                updateTodoItem(todoItem);

               sortTodoList();
            }
        });


        viewHolder.basicDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "Klick Item " + todoItem.getId());
                startShowingDetailView(todoItem);
            }
        });
    }


    private void sortTodoList(){

        todoListViewAdapter.sort(new TodoListSortComparator(SortMode, 0));
        todoListViewAdapter.sort(new TodoListSortComparator(SortMode, 1));
        todoListViewAdapter.sort(new TodoListSortComparator(SortMode, 2));
        todoListViewAdapter.notifyDataSetChanged();
    }




}
