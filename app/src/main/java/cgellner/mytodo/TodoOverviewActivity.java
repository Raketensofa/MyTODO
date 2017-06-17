package cgellner.mytodo;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import database.ITodoItemCRUD;
import database.ITodoItemCRUDAsync;
import database.Queries;
import model.TodoItem;

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

    private static String TODO_ITEM = "TODO_ITEM";

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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == R.integer.NEWTODO_ACTIVITY && resultCode == R.integer.SAVE_TODO) {

            TodoItem newTodo = (TodoItem) data.getSerializableExtra(TODO_ITEM);
            Log.i(TAG, "New Todo: " + newTodo.toString());
            createAndShowItem(newTodo);

        }else
        if (requestCode == R.integer.DETAIL_ACTIVITY) {
              if (resultCode == R.integer.DELETE_TODO) {

                  long todoId = data.getLongExtra(Queries.COLUMN_ID, -1);
                  deleteAndRemoveTodoItem(todoId);

            } else if (resultCode == R.integer.UPDATE_TODO) {

                  TodoItem updatedItem = (TodoItem) data.getSerializableExtra(TODO_ITEM);
                  updateTodoItem(updatedItem);
                  listviewTodos.setAdapter(todoListViewAdapter);
                  Toast.makeText(getApplicationContext(), "Änderungen gespeichert", Toast.LENGTH_SHORT).show();
            }
        }
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

        Intent intent = new Intent(this, TodoDetailActivity.class);
        intent.putExtra(TODO_ITEM, item);
        startActivityForResult(intent, R.integer.DETAIL_ACTIVITY);
    }


    private void createAndShowItem(TodoItem item) {

        progressDialog.show();

        new AsyncTask<TodoItem, Void, TodoItem>(){

            @Override
            protected TodoItem doInBackground(TodoItem... params) {
                return  localCrudOperations.createTodoItem(params[0]);
            }

            @Override
            protected void onPostExecute(TodoItem result) {

                if (myTodoApplication.isConnToRemote()) {

                    remoteCrudOperations.createTodoItem(result, new ITodoItemCRUDAsync.CallbackFunction<TodoItem>() {

                        @Override
                        public void process(TodoItem result) {

                            todoListViewAdapter.add(result);
                            progressDialog.hide();
                        }
                    });

                }else{

                    todoListViewAdapter.add(result);
                    progressDialog.hide();
                }
            }
        }.execute(item);
    }


    private void updateTodoItem(final TodoItem item) {

        progressDialog.show();

       TodoItem updatedLocalItem = localCrudOperations.updateTodoItem(item);

        if(updatedLocalItem != null && myTodoApplication.isConnToRemote()){

            remoteCrudOperations.updateTodoItem(item, new ITodoItemCRUDAsync.CallbackFunction<TodoItem>() {
                    @Override
                    public void process(TodoItem result) {

                        Log.i(TAG, "");
                    }
            });
        }

        updateTodoItemInList(updatedLocalItem);
        progressDialog.hide();

        /**
        new AsyncTask<TodoItem, Void, TodoItem>() {

            @Override
            protected TodoItem doInBackground(TodoItem... params) {
                return localCrudOperations.updateTodoItem(params[0]);
            }

            @Override
            protected void onPostExecute(TodoItem item) {

                if (((MyTodoApplication) getApplication()).isConnToRemote()) {

                    remoteCrudOperations.updateTodoItem(item, new ITodoItemCRUDAsync.CallbackFunction<TodoItem>() {
                        @Override
                        public void process(TodoItem result) {

                            updateTodoItemInList(result);
                            progressDialog.hide();
                        }
                    });

                } else {
                    updateTodoItemInList(item);
                    progressDialog.hide();
                }
            }

        }.execute(item);*/
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
                    }
                }
            });

        }else{

           List<TodoItem> items = localCrudOperations.readAllTodoItems();

            if (items != null) {
                for (TodoItem item : items) {
                    todoListViewAdapter.add(item);
                }
            }


        }

        progressDialog.hide();
    }


    private void deleteAndRemoveTodoItem(final long todoId){

        progressDialog.show();

        boolean deletedLocalItem = localCrudOperations.deleteTodoItem(todoId);

        if(deletedLocalItem && myTodoApplication.isConnToRemote()){

            remoteCrudOperations.deleteTodoItem(todoId, new ITodoItemCRUDAsync.CallbackFunction<Boolean>() {

                @Override
                public void process(Boolean deleted) {

                    Log.i(TAG, "Remote Item gelöscht: " + deleted);
                }
            });
        }

        progressDialog.hide();
        if(deletedLocalItem){

            todoListViewAdapter.remove(findTodoItemInList(todoId));
            Toast.makeText(getApplicationContext(), "TODO gelöscht.", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getApplicationContext(), "Error: Fehler beim Löschen des Todos aufgetreten.", Toast.LENGTH_SHORT).show();
        }





        /**
        new AsyncTask<Long, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Long... params) {
                return localCrudOperations.deleteTodoItem(todoId);
            }

            @Override
            protected void onPostExecute(Boolean result) {

                if(((MyTodoApplication) getApplication()).isConnToRemote()) {

                    remoteCrudOperations.deleteTodoItem(todoId, new ITodoItemCRUDAsync.CallbackFunction<Boolean>() {

                        @Override
                        public void process(Boolean deleted) {

                            if (deleted) {
                                todoListViewAdapter.remove(findTodoItemInList(todoId));
                                Toast.makeText(getApplicationContext(), "TODO gelöscht.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error: Fehler beim Löschen des Todos aufgetreten.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{

                    if (result) {
                        todoListViewAdapter.remove(findTodoItemInList(todoId));
                        Toast.makeText(getApplicationContext(), "TODO gelöscht.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error: Fehler beim Löschen des Todos aufgetreten.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }.execute(todoId);*/
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

                todoListViewAdapter.clear();
                readItemsAndFillListView();

                return false;
            }
        });
    }


    private void showFormForNewTodo() {

        Intent intent = new Intent(getBaseContext(), TodoAddNewActivity.class);
        startActivityForResult(intent, R.integer.NEWTODO_ACTIVITY);
    }


    private void initToolbar() {

        Toolbar toolbar = (android.widget.Toolbar) findViewById(R.id.todo_main_toolbar);
        toolbar.setTitle("MyTODO");
        setActionBar(toolbar);
    }


    private void markExpiredTodoItems(TextView view, long expiry) {

            try {

                Calendar cal = Calendar.getInstance();

                if (expiry < cal.getTime().getTime()) {

                    view.setTextColor(Color.WHITE);
                    view.setBackgroundColor(Color.RED);
                    //view.setBackgroundColor(Color.DKGRAY);

                }else{

                    view.setBackgroundColor(Color.WHITE);
                    view.setTextColor(Color.DKGRAY);
                }

            } catch (Exception ex) {


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
}
