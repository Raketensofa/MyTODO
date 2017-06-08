package cgellner.mytodo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.TodoItem;

/**
 * Created by Carolin on 07.05.2017.
 */
public class TodoCursorAdapter extends CursorAdapter {


    private TodoOverviewActivity MainActivity;

    public void setMainActivity(TodoOverviewActivity mainActivity) {
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
        final TodoItem todo = new TodoItem();
        todo.setAllDataFromCursor(cursor);

        //Werte den View-Componenten zuweisen
        //setTodoDataToComponents(view, todo);

        fillColor(view, todo.getDeadlineDate(), todo.getDeadlineTime());



        View labelView = (View)view.findViewById(R.id.labels_layout);
        //OnClickListener der View
        //Detailview wird gestartet, wenn auf ein Item geklickt wird
        labelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), TodoDetailActivity.class);
                intent.putExtra(String.valueOf(R.string.view_mode), R.integer.VIEW_MODE_DETAIL);
                todo.putToIntentExtras(intent);
                MainActivity.startActivityForResult(intent, R.integer.DETAIL_ACTIVITY);
            }
        });
    }





    private void setIsFavouriteStar(ImageView imageView, int isFavourite) {

        imageView.setClickable(true);

        if(isFavourite == 1){

            imageView.setImageResource(R.drawable.ic_star_black_24dp);

        }else{

            imageView.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }


    private void fillColor(View view, String date, String time){

        SimpleDateFormat sdfToDate = new SimpleDateFormat( "dd.MM.yyyy HH:mm" );


        try {

            Calendar cal = Calendar.getInstance();

            Date todoDateTime = sdfToDate.parse(date + " " + time);

            if(todoDateTime.getTime() < cal.getTime().getTime()){

                TextView highlight = (TextView) view.findViewById(R.id.textview_highlight);
                highlight.setBackgroundColor(Color.RED);
            }

        }catch (Exception ex){

        }
    }

}
