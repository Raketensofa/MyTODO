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
    public void bindView(final View view, final Context context, Cursor cursor) {

        //Bestandteile der View
        TextView todoName = (TextView)view.findViewById(R.id.todo_textview_name);
        TextView todoDeadline = (TextView)view.findViewById(R.id.todo_textview_deadline);
        CheckBox todoIsDone = (CheckBox)view.findViewById(R.id.todo_checkbox_isdone);
        RatingBar todoIsFavourite = (RatingBar)view.findViewById(R.id.todo_start_isfavourite);


        //Werte aus Cursor auslesen
        final long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(Queries.COLUMN_ID));
        final String itemName = cursor.getString(cursor.getColumnIndexOrThrow(Queries.COLUMN_NAME));
        final String itemDescription = cursor.getString(cursor.getColumnIndexOrThrow(Queries.COLUMN_DESCRIPTION));
        final String itemDeadlineDate = cursor.getString(cursor.getColumnIndexOrThrow(Queries.COLUMN_DEADLINE_DATE));
        final String itemDeadlineTime = cursor.getString(cursor.getColumnIndexOrThrow(Queries.COLUMN_DEADLINE_TIME));
        final int itemIsDone =  cursor.getInt(cursor.getColumnIndexOrThrow(Queries.COLUMN_ISDONE));
        final int isFavourite =  cursor.getInt(cursor.getColumnIndexOrThrow(Queries.COLUMN_ISFAVOURITE));


        //Werte den Elementen in der Listen-Ansicht zuweisen
        todoName.setText(itemName);
        todoDeadline.setText("Fälligkeit: " + itemDeadlineDate + " " + itemDeadlineTime);
        todoIsDone.setChecked(false);
        if(itemIsDone == 1){

            todoIsDone.setChecked(true);
        }

        todoIsFavourite.setRating(isFavourite);

        //OnClickListener der View
        //Detailview wird gestartet, wenn auf ein Item geklickt wird
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ActivityTodoDetail.class);

                intent.putExtra(Queries.COLUMN_ID, itemId);
                intent.putExtra(Queries.COLUMN_NAME, itemName);
                intent.putExtra(Queries.COLUMN_DESCRIPTION, itemDescription);
                intent.putExtra(Queries.COLUMN_ISFAVOURITE, isFavourite);
                intent.putExtra(Queries.COLUMN_ISDONE, itemIsDone);
                intent.putExtra(Queries.COLUMN_DEADLINE_DATE, itemDeadlineDate);
                intent.putExtra(Queries.COLUMN_DEADLINE_TIME, itemDeadlineTime);

                view.getContext().startActivity(intent);
            }
        });
    }

}
