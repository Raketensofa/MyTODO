package cgellner.mytodo;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

import models.Todo;

public class MainActivity extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private ArrayList<Todo> todolist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        try {

            todolist = new ArrayList<>();
            todolist.add(new Todo("Testtodo", "Das ist ein Test.", false, false, new Date(2017, 4, 22)));
            todolist.add(new Todo("Testtodo", "Das ist ein Test.", true, false, new Date(2017, 4, 22)));
            todolist.add(new Todo("Testtodo", "Das ist ein Test.", false, true, new Date(2017,4,22)));


            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_todolist);
            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new Todolist_recyclerview_adapter(todolist);
            mRecyclerView.setAdapter(mAdapter);

        } catch (Exception ex) {

        }


    }

}
