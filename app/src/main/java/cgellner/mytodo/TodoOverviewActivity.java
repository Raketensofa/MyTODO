package cgellner.mytodo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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


    private ITodoItemCRUDAsync crudOperations;

    private static String TODO_ITEM = "TODO_ITEM";

    private ProgressDialog progressDialog;

    private class ItemViewHolder {

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

                viewHolder.itemId = todoItem.getId();
                viewHolder.itemName.setText(todoItem.getName());
                viewHolder.itemDeadline.setText(todoItem.getDeadlineDate() + " " + todoItem.getDeadlineTime());
                markExpiredTodoItems(viewHolder.itemDeadline, todoItem.getDeadlineDate(), todoItem.getDeadlineTime());

                if (todoItem.getIsFavourite() == 1) {
                    viewHolder.itemIsFavourite.setTag("IS_FAV");
                    viewHolder.itemIsFavourite.setImageResource(R.drawable.yellow_star_1);
                } else if (todoItem.getIsFavourite() == 0) {
                    viewHolder.itemIsFavourite.setTag("IS_NOT_FAV");
                    viewHolder.itemIsFavourite.setImageResource(R.drawable.icons8sternfilled50);
                }

                if (todoItem.getIsDone() == 1) {
                    viewHolder.itemIsDone.setTag("IS_DONE");
                    viewHolder.itemIsDone.setImageResource(R.drawable.filled_checkbox_23);
                } else if (todoItem.getIsDone() == 0) {
                    viewHolder.itemIsDone.setTag("IS_NOT_DONE");
                    viewHolder.itemIsDone.setImageResource(R.drawable.unfilled_checkbox34);
                }

                viewHolder.basicDataView.setClickable(true);
                viewHolder.basicDataView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.i(TAG, "Klick Item " + todoItem.getId());

                        startShowingDetailView(todoItem);
                    }
                });


                viewHolder.itemIsDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int checkState = todoItem.getIsDone();

                        if (viewHolder.itemIsDone.getTag().equals("IS_DONE")) {

                            viewHolder.itemIsDone.setImageResource(R.drawable.unfilled_checkbox34);
                            checkState = 0;
                            viewHolder.itemIsDone.setTag("IS_NOT_DONE");

                        } else if (viewHolder.itemIsDone.getTag().equals("IS_NOT_DONE")) {

                            viewHolder.itemIsDone.setImageResource(R.drawable.filled_checkbox_23);
                            checkState = 1;
                            viewHolder.itemIsDone.setTag("IS_DONE");
                        }

                        todoItem.setIsDone(checkState);
                        updateTodoItem(todoItem);
                        todoListViewAdapter.clear();
                        readItemsAndFillListView();
                    }
                });

                viewHolder.itemIsFavourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int checkState = todoItem.getIsFavourite();

                        if (viewHolder.itemIsFavourite.getTag().equals("IS_FAV")) {

                            viewHolder.itemIsFavourite.setImageResource(R.drawable.icons8sternfilled50);
                            viewHolder.itemIsFavourite.setTag("IS_NOT_FAV");
                            checkState = 0;

                        } else if (viewHolder.itemIsFavourite.getTag().equals("IS_NOT_FAV")) {

                            viewHolder.itemIsFavourite.setImageResource(R.drawable.yellow_star_1);
                            viewHolder.itemIsFavourite.setTag("IS_FAV");
                            checkState = 1;
                        }

                        todoItem.setIsFavourite(checkState);
                        updateTodoItem(todoItem);
                    }
                });

                return itemView;
            }
        };

        listviewTodos.setAdapter(todoListViewAdapter);
        todoListViewAdapter.setNotifyOnChange(true);

        crudOperations = ((MyTodoApplication)getApplication()).getCRUDOperationsImpl();

        readItemsAndFillListView();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == R.integer.NEWTODO_ACTIVITY && resultCode == R.integer.SAVE_TODO) {

            TodoItem newTodo = (TodoItem) data.getSerializableExtra(TODO_ITEM);
            createAndShowItem(newTodo);

        }else
        if (requestCode == R.integer.DETAIL_ACTIVITY) {
              if (resultCode == R.integer.DELETE_TODO) {

                  long todoId = data.getLongExtra(Queries.COLUMN_ID, -1);
                  deleteAndRemoveTodoItem(todoId);

            } else if (resultCode == R.integer.UPDATE_TODO) {

                  TodoItem updatedItem = (TodoItem) data.getSerializableExtra(TODO_ITEM);
                  updateTodoItem(updatedItem);
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

                 todoListViewAdapter.insert(item,i);
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

       crudOperations.createTodoItem(item, new ITodoItemCRUDAsync.CallbackFunction<TodoItem>(){

           @Override
           public void process(TodoItem result) {

               todoListViewAdapter.add(result);
               progressDialog.hide();
           }
       });
    }


    private void updateTodoItem(final TodoItem item) {

        progressDialog.show();

        crudOperations.updateTodoItem(item, new ITodoItemCRUDAsync.CallbackFunction<TodoItem>() {
            @Override
            public void process(TodoItem result) {

                updateTodoItemInList(result);
                progressDialog.hide();
            }
        });
    }


    private void readItemsAndFillListView() {

        progressDialog.show();
        crudOperations.readAllTodoItems(new ITodoItemCRUDAsync.CallbackFunction<List<TodoItem>>() {
            @Override
            public void process(List<TodoItem> result) {

                progressDialog.hide();

                if (result != null) {
                    for (TodoItem item : result) {
                        todoListViewAdapter.add(item);
                    }
                }
            }
        });
    }


    private void deleteAndRemoveTodoItem(final long todoId){

        crudOperations.deleteTodoItem(todoId, new ITodoItemCRUDAsync.CallbackFunction<Boolean>(){

            @Override
            public void process(Boolean deleted) {

                if(deleted){
                    todoListViewAdapter.remove(findTodoItemInList(todoId));
                    Toast.makeText(getApplicationContext(), "TODO gelöscht.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Error: Fehler beim Löschen des Todos aufgetreten.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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


    private void markExpiredTodoItems(TextView view, String date, String time) {

        SimpleDateFormat sdfToDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            try {

                Calendar cal = Calendar.getInstance();
                Date todoDateTime = sdfToDate.parse(date + " " + time);

                if (todoDateTime.getTime() < cal.getTime().getTime()) {

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
}
