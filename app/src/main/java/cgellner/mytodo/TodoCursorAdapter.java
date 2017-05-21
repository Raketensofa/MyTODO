package cgellner.mytodo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Space;
import android.widget.TextView;

import java.security.spec.ECField;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

        fillColor(view, todo.getDeadlineDate(), todo.getDeadlineTime());



        View labelView = (View)view.findViewById(R.id.labels_layout);
        //OnClickListener der View
        //Detailview wird gestartet, wenn auf ein Item geklickt wird
        labelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ActivityTodoDetail.class);
                intent.putExtra(String.valueOf(R.string.view_mode), R.integer.VIEW_MODE_DETAIL);
                todo.putToIntentExtras(intent);
                MainActivity.startActivityForResult(intent, R.integer.DETAIL_ACTIVITY);
            }
        });
    }


    private void setTodoDataToComponents(View view, final Todo todo){

        TextView todoName = (TextView)view.findViewById(R.id.todo_textview_name);
        TextView todoDeadline = (TextView)view.findViewById(R.id.todo_textview_deadline);
        CheckBox todoIsDone = (CheckBox)view.findViewById(R.id.todo_checkbox_isdone);
        ImageView todoIsFavourite = (ImageView) view.findViewById(R.id.imageview_todo_isfavourite);


        //Werte den Elementen in der Listen-Ansicht zuweisen
        todoName.setText(todo.getName());
        todoDeadline.setText(todo.getDeadlineDate() + " " + todo.getDeadlineTime());
        todoIsDone.setChecked(false);
        if(todo.getIsDone() == 1){

            todoIsDone.setChecked(true);
        }

        setIsFavouriteStar(todoIsFavourite, todo.getIsFavourite());

        todoIsDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        todoIsFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int isfav = todo.getIsFavourite();

                int newFav = -1;
                if(isfav == 1){
                    newFav = 0;
                }else if(isfav == 0){
                    newFav = 1;
                }

                ImageView iv = (ImageView)v.findViewById(R.id.imageview_todo_isfavourite);
                setIsFavouriteStar(iv, newFav);
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
