package cgellner.mytodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;

import models.Todo;

/**
 * Created by Carolin on 21.04.2017.
 */
public class Todolist_recyclerview_adapter extends RecyclerView.Adapter<Todolist_recyclerview_adapter.ViewHolder> {

    private ArrayList<Todo> todoList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView todoName;
        public TextView todoDeadline;
        public CheckBox todoIsDone;
        public RatingBar todoIsFavourite;

        public ViewHolder(View itemView) {

            super(itemView);

            todoName = (TextView)itemView.findViewById(R.id.todo_textview_name);
            todoDeadline = (TextView)itemView.findViewById(R.id.todo_textview_deadline);
            todoIsDone = (CheckBox)itemView.findViewById(R.id.todo_checkbox_isdone);
            todoIsFavourite = (RatingBar)itemView.findViewById(R.id.todo_start_isfavourite);

            todoIsDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                }
            });


            todoIsFavourite.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                }
            });
        }
    }


    public Todolist_recyclerview_adapter(ArrayList<Todo> todolist) {

        this.todoList = todolist;
    }


    @Override
    public Todolist_recyclerview_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_item, parent, false);
        ViewHolder itemVh = new ViewHolder(v);

        return itemVh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.todoName.setText(todoList.get(position).getName());
        holder.todoDeadline.setText("Fälligkeit: " + todoList.get(position).getDeadline().toString());
        holder.todoIsDone.setChecked(todoList.get(position).isDone());

        if(todoList.get(position).isFavourite()){

            holder.todoIsFavourite.setNumStars(1);
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }


}
