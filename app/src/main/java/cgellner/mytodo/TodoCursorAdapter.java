package cgellner.mytodo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import database.Queries;

/**
 * Created by Carolin on 07.05.2017.
 */
public class TodoCursorAdapter extends CursorAdapter {


    public TodoCursorAdapter(Context context, Cursor cursor) {

        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.todo_list_item, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Bestandteile der View
        TextView todoName = (TextView)view.findViewById(R.id.todo_textview_name);
        TextView todoDeadline = (TextView)view.findViewById(R.id.todo_textview_deadline);
        CheckBox todoIsDone = (CheckBox)view.findViewById(R.id.todo_checkbox_isdone);
        RatingBar todoIsFavourite = (RatingBar)view.findViewById(R.id.todo_start_isfavourite);


        //Werte aus Cursor auslesen
        String itemName = cursor.getString(cursor.getColumnIndexOrThrow(Queries.COLUMN_NAME));
        String itemDedline = cursor.getString(cursor.getColumnIndexOrThrow(Queries.COLUMN_DEADLINE));
        String itemIsDone =  cursor.getString(cursor.getColumnIndexOrThrow(Queries.COLUMN_ISDONE));
        String isFavourite =  cursor.getString(cursor.getColumnIndexOrThrow(Queries.COLUMN_ISFAVOURITE));


        //Werte zuweisen
        todoName.setText(itemName);
        todoDeadline.setText("Fälligkeit: " + itemDedline);
        todoIsDone.setChecked(Boolean.parseBoolean(itemIsDone));

        if(Boolean.parseBoolean(isFavourite) == Boolean.TRUE){

            todoIsFavourite.setRating(1);

        }else{

            todoIsFavourite.setRating(0);
        }
    }
}
