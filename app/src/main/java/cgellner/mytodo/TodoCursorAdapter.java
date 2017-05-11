package cgellner.mytodo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import model.Todo;

/**
 * Created by Carolin on 07.05.2017.
 */
public class TodoCursorAdapter extends CursorAdapter {


    private MainActivity MainActivity;

    public void setMainActivity(MainActivity mainActivity) {
        this.MainActivity = mainActivity;
    }


    public TodoCursorAdapter(Context context, Cursor cursor) {

        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.todo_list_item, parent, false);
    }


    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {

        view.setClickable(true);

        //Werte aus Cursor auslesen
        final Todo todo = new Todo();
        todo.setAllDataFromCursor(cursor);

        //Werte den View-Componenten zuweisen
        setTodoDataToComponents(view, todo);

        //OnClickListener der View
        //Detailview wird gestartet, wenn auf ein Item geklickt wird
       view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ActivityTodoDetail.class);
                intent.putExtra(String.valueOf(R.string.view_mode), R.integer.VIEW_MODE_DETAIL);
                todo.putToIntentExtras(intent);
                MainActivity.startActivityForResult(intent, R.integer.DETAIL_ACTIVITY);
            }
        });
    }


    private void setTodoDataToComponents(View view, Todo todo){

        TextView todoName = (TextView)view.findViewById(R.id.todo_textview_name);
        TextView todoDeadline = (TextView)view.findViewById(R.id.todo_textview_deadline);
        CheckBox todoIsDone = (CheckBox)view.findViewById(R.id.todo_checkbox_isdone);
        RatingBar todoIsFavourite = (RatingBar)view.findViewById(R.id.todo_start_isfavourite);


        //Werte den Elementen in der Listen-Ansicht zuweisen
        todoName.setText(todo.getName());
        todoDeadline.setText("Fälligkeit: " + todo.getDeadlineDate() + " " + todo.getDeadlineTime());
        todoIsDone.setChecked(false);
        if(todo.getIsDone() == 1){

            todoIsDone.setChecked(true);
        }

        todoIsFavourite.setRating(todo.getIsFavourite());
    }

}
