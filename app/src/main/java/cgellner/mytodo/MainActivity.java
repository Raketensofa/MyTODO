package cgellner.mytodo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import database.Queries;
import database.SqliteDatabase;
import model.Todo;

public class MainActivity extends Activity {


    private ListView listviewTodos;
    private SqliteDatabase database;
    private Button buttonNewTodo;
    private TodoCursorAdapter todoCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        database = new SqliteDatabase(this);
        database.open();

        setButtonListener();
        createListviewTodos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void createListviewTodos(){

        try {

            Cursor todoCursor = database.getDatabase().rawQuery("SELECT  * FROM " + Queries.TABLE_TODOS, null);
            listviewTodos = (ListView) findViewById(R.id.listview_todolist);
            todoCursorAdapter = new TodoCursorAdapter(this, todoCursor);
            todoCursorAdapter.setMainActivity(this);
            listviewTodos.setAdapter(todoCursorAdapter);



            // Switch to new cursor and update contents of ListView
            // todoAdapter.changeCursor(newCursor);

        }catch (Exception ex){

            Log.d(this.getClass().getName(), ex.getMessage());
        }
    }


    private void setButtonListener(){

        buttonNewTodo = (Button)findViewById(R.id.todolist_button_newTodo);
        buttonNewTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFormForNewTodo();
            }
        });
    }


    private void showFormForNewTodo(){

        Intent intent = new Intent(getBaseContext(), ActivityTodoDetail.class);
        intent.putExtra(String.valueOf(R.string.view_mode), R.integer.VIEW_MODE_NEW);
        startActivityForResult(intent, R.integer.DETAIL_ACTIVITY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

          if (requestCode == R.integer.DETAIL_ACTIVITY) {

              if (resultCode == R.integer.SAVE_TODO) {

                  //Daten auslesen die vom Benutzer eingegeben wurden
                  Todo newTodo = new Todo();
                  newTodo.setDataFromIntentExtras(data.getExtras());

                  //Daten speichern in der DB
                  database.open();
                  long id = database.createTodo(newTodo);

                  if(id > 0){

                      Toast.makeText(getApplicationContext(), "TODO gespeichert!", Toast.LENGTH_SHORT).show();

                      Cursor cur = database.getDatabase().rawQuery("SELECT  * FROM " + Queries.TABLE_TODOS, null);
                      todoCursorAdapter.changeCursor(cur);

                  }else{

                      Toast.makeText(getApplicationContext(), "TODO konnte nicht gespeichert werden.", Toast.LENGTH_SHORT).show();
                  }

              }else if(resultCode == R.integer.DELETE_TODO){

                    long todoId = data.getLongExtra(Queries.COLUMN_ID, 0);
                    database.open();
                    boolean deleted = database.deleteTodoItem(todoId);

                  if(deleted == true){

                      Toast.makeText(getApplicationContext(), "TODO wurde gelöscht.", Toast.LENGTH_SHORT).show();
                      Cursor cur = database.getDatabase().rawQuery("SELECT  * FROM " + Queries.TABLE_TODOS, null);
                      todoCursorAdapter.changeCursor(cur);

                  }else{

                      Toast.makeText(getApplicationContext(), "Fehler beim Löschen aufgetreten.", Toast.LENGTH_SHORT).show();
                  }
              }else if(requestCode == R.integer.UPDATE_TODO){

                  Todo newTodoData = new Todo();
                  newTodoData.setDataFromIntentExtras(data.getExtras());

                  database.open();
                  boolean updated = database.updateTodoItem(newTodoData);

                  if(updated == true){

                      Toast.makeText(getApplicationContext(), "Daten geändert", Toast.LENGTH_SHORT).show();
                      Cursor cur = database.getDatabase().rawQuery("SELECT  * FROM " + Queries.TABLE_TODOS, null);
                      todoCursorAdapter.changeCursor(cur);

                  }else{

                      Toast.makeText(getApplicationContext(), "Daten nicht geändert", Toast.LENGTH_SHORT).show();
                  }
              }
          }
    }


}
