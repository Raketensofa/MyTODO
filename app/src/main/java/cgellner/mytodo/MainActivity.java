package cgellner.mytodo;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import model.Todo;

public class MainActivity extends Activity {


    private ArrayList<Todo> todolist;
    private  ListView listviewTodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        createListviewTodos();
    }


    private void createListviewTodos(){

        try {
            todolist = new ArrayList<>();
            todolist.add(new Todo("Testtodo", "Das ist ein Test.", false, false, new Date(2017, 4, 22)));
            todolist.add(new Todo("Testtodo", "Das ist ein Test.", true, false, new Date(2017, 4, 22)));
            todolist.add(new Todo("Testtodo", "Das ist ein Test.", false, true, new Date(2017, 4, 22)));

            TodolistArrayAdapter adapter = new TodolistArrayAdapter(this, todolist);

            listviewTodos = (ListView) findViewById(R.id.listview_todolist);
            listviewTodos.setAdapter(adapter);

        }catch (Exception ex){

            Log.d(this.getClass().getName(), ex.getMessage());
        }
    }

}
