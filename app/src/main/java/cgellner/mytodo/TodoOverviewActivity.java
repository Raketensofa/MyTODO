package cgellner.mytodo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.List;

import model.TodoItem;

public class TodoOverviewActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = TodoOverviewActivity.class.getSimpleName();

    private ListView listviewTodos;
    private ArrayAdapter<TodoItem> todoListViewAdapter;

    private SQLiteDatabase SqLiteDatabase;

    private MenuItem itemNew;
    private MenuItem itemSort;


    private List<TodoItem> todoItemList;

    private int SortMode; //1 = Faelligkeit+Wichtigkeit  - 2= Wichtigkeit+Faelligkeit


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

                //Bei Klick auf die View des Listenelements wird die jeweilige Detailansicht geoeffnet
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startShowingDetailView(v, todoItem);
                    }
                });


                return itemView;
            }
        };


        listviewTodos.setAdapter(todoListViewAdapter);
        todoListViewAdapter.setNotifyOnChange(true);

    }


    /**
     * Detailansicht starten
     * @param v
     */
    private void startShowingDetailView(View v, TodoItem item){

        Intent intent = new Intent(this, TodoDetailActivity.class);
        intent.putExtra(String.valueOf(R.string.view_mode), R.integer.VIEW_MODE_DETAIL);


        //todo.putToIntentExtras(intent);
        startActivityForResult(intent, R.integer.DETAIL_ACTIVITY);

    }


    private void readItemsAndFillListView(){

        for(TodoItem item : todoItemList){

            todoListViewAdapter.add(item);
        }
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


 /**
    private Cursor createTodoListCursor(int sortMode){

        Cursor cursor = null;

        if(sortMode == R.integer.SORT_MODE_DEADLINE_FAVOURITE){

            cursor = database.getDatabase().rawQuery("SELECT  * FROM "
                    + Queries.TABLE_TODOS + " ORDER BY "
                    + Queries.COLUMN_ISDONE + " ASC,"
                    + Queries.COLUMN_DEADLINE_DATE + " ASC,"
                    + Queries.COLUMN_DEADLINE_TIME + " ASC,"
                    + Queries.COLUMN_ISFAVOURITE + " DESC", null);

        }else if(sortMode == R.integer.SORT_MODE_FAVOURITE_DEADLINE){

            cursor = database.getDatabase().rawQuery("SELECT  * FROM "
                    + Queries.TABLE_TODOS + " ORDER BY "
                    + Queries.COLUMN_ISDONE + " ASC,"
                    + Queries.COLUMN_ISFAVOURITE + " DESC,"
                    + Queries.COLUMN_DEADLINE_DATE + " ASC,"
                    + Queries.COLUMN_DEADLINE_TIME + " ASC", null);
        }

        return cursor;
    }*/


    private void showFormForNewTodo(){

        Intent intent = new Intent(getBaseContext(), TodoDetailActivity.class);
        intent.putExtra(String.valueOf(R.string.view_mode), R.integer.VIEW_MODE_NEW);
        startActivityForResult(intent, R.integer.DETAIL_ACTIVITY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

          if (requestCode == R.integer.DETAIL_ACTIVITY) {

              if (resultCode == R.integer.SAVE_TODO) {

                  //Daten auslesen die vom Benutzer eingegeben wurden
                  TodoItem newTodo = new TodoItem();
                  newTodo.setDataFromIntentExtras(data.getExtras());

                  //Daten speichern in der DB
                 /**  database.open();
                  long id = database.createTodo(newTodo);

                  if(id > 0){

                      Toast.makeText(getApplicationContext(), "TODO gespeichert!", Toast.LENGTH_SHORT).show();

                      //initTodoListView();

                  }else{

                      Toast.makeText(getApplicationContext(), "TODO konnte nicht gespeichert werden.", Toast.LENGTH_SHORT).show();
                  }*/

              }else if(resultCode == R.integer.DELETE_TODO){

                   /** long todoId = data.getLongExtra(Queries.COLUMN_ID, 0);
                    database.open();
                    boolean deleted = database.deleteTodoItem(todoId);

                  if(deleted == true){

                      Toast.makeText(getApplicationContext(), "TODO wurde gelöscht.", Toast.LENGTH_SHORT).show();
                      initTodoListView();

                  }else{

                      Toast.makeText(getApplicationContext(), "Fehler beim Löschen aufgetreten.", Toast.LENGTH_SHORT).show();
                  }*/
              }else if(requestCode == R.integer.UPDATE_TODO){

                  TodoItem newTodoData = new TodoItem();
                  newTodoData.setDataFromIntentExtras(data.getExtras());
                  /**
                  database.open();
                  boolean updated = database.updateTodoItem(newTodoData);

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
    public void onClick(View v) {






    }
}
