package cgellner.mytodo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import database.SqliteDatabase;
import model.Todo;

public class MainActivity extends Activity {


    private ArrayList<Todo> todolist;
    private ListView listviewTodos;
    private SqliteDatabase database;
    private Button buttonNewTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        database = new SqliteDatabase(this);
        database.open();

        setButtonListener();
        createListviewTodos();
    }


    private void createListviewTodos(){

        try {

            todolist = new ArrayList<>();
            todolist.add(new Todo("Testtodo", "Das ist ein Test.", false, false, new Date(2017, 4, 22)));
            todolist.add(new Todo("Testtodo", "Das ist ein Test.", true, false, new Date(2017, 4, 22)));
            todolist.add(new Todo("Testtodo", "Das ist ein Test.", false, true, new Date(2017, 4, 22)));


            Cursor todoCursor = database.getDatabase().rawQuery("SELECT  * FROM todo_items", null);
            listviewTodos = (ListView) findViewById(R.id.listview_todolist);
            TodoCursorAdapter todoCursorAdapter = new TodoCursorAdapter(this, todoCursor);
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

        Intent intent = new Intent(this, ActivityTodoForm.class);
        startActivity(intent);

    }

}
