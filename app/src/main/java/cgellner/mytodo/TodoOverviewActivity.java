package cgellner.mytodo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.List;

import database.ITodoItemCRUD;
import database.LocalTodoItemCRUDOperations;
import model.TodoItem;

public class TodoOverviewActivity extends Activity{

    private static String TAG = TodoOverviewActivity.class.getSimpleName();

    private ListView listviewTodos;
    private ArrayAdapter<TodoItem> todoListViewAdapter;

    private MenuItem itemNew;
    private MenuItem itemSort;

    private int SortMode; //1 = Faelligkeit+Wichtigkeit  - 2= Wichtigkeit+Faelligkeit

    private ITodoItemCRUD crudOperations;

    private static String TODO_ITEM = "todo_item";

    private ProgressDialog progressDialog;


    private class ItemViewHolder{

        public long itemId;

        public TextView itemName;
        public TextView itemDescription;
        public TextView itemDeadline;
        public CheckBox itemIsDone;
        public ImageView itemIsFavourite;

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo_overview);

        progressDialog = new ProgressDialog(this);

        initToolbar();


        /**SortMode = database.readSortMode();
        if (SortMode < 1) {
            database.createMainSettings();
            SortMode = database.readSortMode();

        Log.e("SortMode", String.valueOf(SortMode));
         }*/

        listviewTodos = (ListView) findViewById(R.id.listview_todolist);
        todoListViewAdapter = new ArrayAdapter<TodoItem>(this, R.layout.todo_list_item){

            @NonNull
            @Override
            public View getView(int position, View itemView, ViewGroup parent) {

                //Initialsierung der View-Elemente
                if(itemView == null) {

                    itemView = getLayoutInflater().inflate(R.layout.todo_list_item, null);
                    TextView itemNameView = (TextView)itemView.findViewById(R.id.todo_textview_name);
                    TextView itemDescriptionView = (TextView)itemView.findViewById(R.id.todo_textview_deadline);
                    TextView itemDeadlineView = (TextView)itemView.findViewById(R.id.todo_textview_deadline);
                    CheckBox itemIsDoneView = (CheckBox)itemView.findViewById(R.id.todo_checkbox_isdone);
                    ImageView itemIsFavouriteView = (ImageView) itemView.findViewById(R.id.imageview_todo_isfavourite);

                    ItemViewHolder itemViewHolder = new ItemViewHolder();
                    itemViewHolder.itemName = itemNameView;
                    itemViewHolder.itemDescription =  itemDescriptionView;
                    itemViewHolder.itemDeadline = itemDeadlineView;
                    itemViewHolder.itemIsDone = itemIsDoneView;
                    itemViewHolder.itemIsFavourite = itemIsFavouriteView;

                    itemView.setTag(itemViewHolder);
                }


                //Zuweisung der View-Element-Inhalte
                final TodoItem todoItem = getItem(position);
                ItemViewHolder viewHolder = (ItemViewHolder)itemView.getTag();

                viewHolder.itemId = todoItem.getId();
                viewHolder.itemName.setText(todoItem.getName());
                viewHolder.itemDeadline.setText(todoItem.getDeadlineDate() + " " + todoItem.getDeadlineTime());
                viewHolder.itemIsDone.setChecked(false);
                if(todoItem.getIsDone() == 1){
                    viewHolder.itemIsDone.setChecked(true);
                }
                viewHolder.itemIsFavourite.setClickable(true);
                if(todoItem.getIsFavourite() == 1){
                  viewHolder.itemIsFavourite.setImageResource(R.drawable.ic_star_black_24dp);
                }else{
                    viewHolder.itemIsFavourite.setImageResource(R.drawable.ic_star_border_black_24dp);
                }

                viewHolder.itemIsDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



                    }
                });

                viewHolder.itemIsFavourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                    }
                });

                return itemView;
            }
        };


        listviewTodos.setAdapter(todoListViewAdapter);
        todoListViewAdapter.setNotifyOnChange(true);

        listviewTodos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TodoItem selectedItem = todoListViewAdapter.getItem(position);
                startShowingDetailView(selectedItem);
            }
        });


        crudOperations = new LocalTodoItemCRUDOperations(this);

        readItemsAndFillListView();
    }


    /**
     * Detailansicht starten
     */
    private void startShowingDetailView(TodoItem item){

        Intent intent = new Intent(this, TodoDetailActivity.class);
        intent.putExtra(String.valueOf(R.string.view_mode), R.integer.VIEW_MODE_DETAIL);
        intent.putExtra(TODO_ITEM, item);

        startActivityForResult(intent, R.integer.DETAIL_ACTIVITY);
    }


    private void readItemsAndFillListView(){

      /**  List<TodoItem> items = crudOperations.readAllTodoItems();
        if(items != null && items.size() > 0) {
            for (TodoItem item : items) {
                todoListViewAdapter.add(item);
            }
        }*/


        new AsyncTask<Void, Void, List<TodoItem>>(){

            @Override
            protected void onPreExecute() {
              progressDialog.show();
            }

            @Override
            protected List<TodoItem> doInBackground(Void... params) {
                return crudOperations.readAllTodoItems();
            }

            @Override
            protected void onPostExecute(List<TodoItem> todoItems) {
               progressDialog.hide();
                if(todoItems != null){

                    for(TodoItem item : todoItems){
                        todoListViewAdapter.add(item);
                    }
                }
            }
        }.execute();
    }


    private void createAndShowItem(final TodoItem item){

        /**  progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                TodoItem createdItem = crudOperations.createTodoItem(item);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        todoListViewAdapter.add(item);
                        progressDialog.hide();
                    }
                });
            }
        }).start();*/

        new AsyncTask<TodoItem, Void, TodoItem>(){

            @Override
            protected TodoItem doInBackground(TodoItem... params) {

                TodoItem createdItem = crudOperations.createTodoItem(params[0]);
                return createdItem;
            }

            @Override
            protected void onPreExecute() {
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(TodoItem item) {

                todoListViewAdapter.add(item);
                progressDialog.hide();
            }
        }.execute(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        createToolbarButtons(menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void createToolbarButtons(Menu menu){

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

        if(SortMode == R.integer.SORT_MODE_DEADLINE_FAVOURITE){

            itemSort.setIcon(R.drawable.ic_swap_vert_white_24dp);

        }else if(SortMode == R.integer.SORT_MODE_FAVOURITE_DEADLINE){

            itemSort.setIcon(R.drawable.ic_swap_vertical_circle_white_24dp);
        }

        itemSort.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        itemSort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(SortMode == R.integer.SORT_MODE_DEADLINE_FAVOURITE){   //Datum + Wichtigkeit

                   /** boolean updated = database.updateSortMode(R.integer.SORT_MODE_FAVOURITE_DEADLINE);
                    Log.e("Sortmode updated", String.valueOf(updated));

                    if(updated == true) {
                        SortMode = R.integer.SORT_MODE_FAVOURITE_DEADLINE;
                        item.setIcon(R.drawable.ic_swap_vertical_circle_white_24dp);
                        initTodoListView();
                        Toast.makeText(getApplicationContext(), "Sortierung nach Wichtigkeit + Fälligkeit", Toast.LENGTH_SHORT).show();
                }*/

                }else if(SortMode == R.integer.SORT_MODE_FAVOURITE_DEADLINE) { //Wichtigkeit + Datum

                   /** boolean updated = database.updateSortMode(R.integer.SORT_MODE_DEADLINE_FAVOURITE);

                    if (updated == true) {

                        SortMode = R.integer.SORT_MODE_DEADLINE_FAVOURITE;
                        item.setIcon(R.drawable.ic_swap_vert_white_24dp);
                        initTodoListView();
                        Toast.makeText(getApplicationContext(), "Sortierung nach Fälligkeit + Wichtigkeit", Toast.LENGTH_SHORT).show();
                    }*/
                }

                return false;
            }
        });
    }


    private void showFormForNewTodo(){

        Intent intent = new Intent(getBaseContext(), TodoAddNewActivity.class);
        startActivityForResult(intent, R.integer.NEWTODO_ACTIVITY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

          if (requestCode == R.integer.DETAIL_ACTIVITY) {

              if (resultCode == R.integer.SAVE_TODO) {

                  //Daten auslesen die vom Benutzer eingegeben wurden
                  TodoItem newTodo = (TodoItem)data.getSerializableExtra(TODO_ITEM);
                  createAndShowItem(newTodo);

              }else if(resultCode == R.integer.DELETE_TODO){



                  /**

                  if(deleted == true){

                      Toast.makeText(getApplicationContext(), "TODO wurde gelöscht.", Toast.LENGTH_SHORT).show();
                      initTodoListView();

                  }else{

                      Toast.makeText(getApplicationContext(), "Fehler beim Löschen aufgetreten.", Toast.LENGTH_SHORT).show();
                  }*/
              }else if(requestCode == R.integer.UPDATE_TODO){

                  /**
                  TodoItem newTodoData = new TodoItem();
                  newTodoData.setDataFromIntentExtras(data.getExtras());



                  if(updated == true){

                      Toast.makeText(getApplicationContext(), "Daten geändert", Toast.LENGTH_SHORT).show();
                      initTodoListView();

                  }else{

                      Toast.makeText(getApplicationContext(), "Daten nicht geändert", Toast.LENGTH_SHORT).show();
                  }*/
              }
          }
    }


    private void initToolbar(){

        Toolbar toolbar = (android.widget.Toolbar)findViewById(R.id.todo_main_toolbar);
        toolbar.setTitle("MyTODO");
        setActionBar(toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

}
