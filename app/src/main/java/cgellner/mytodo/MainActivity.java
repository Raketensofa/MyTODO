package cgellner.mytodo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
        intent.putExtra("type", "new");
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

          if (requestCode == 1) {

              if (resultCode == RESULT_OK) {

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
              }
          }
    }

}
