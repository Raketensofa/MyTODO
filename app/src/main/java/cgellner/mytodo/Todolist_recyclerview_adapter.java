package cgellner.mytodo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import models.Todo;

/**
 * Created by Carolin on 21.04.2017.
 */
public class Todolist_recyclerview_adapter extends RecyclerView.Adapter<Todolist_recyclerview_adapter.ViewHolder> {

    private ArrayList<Todo> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;


        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }


    public Todolist_recyclerview_adapter(ArrayList<Todo> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public Todolist_recyclerview_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.mTextView.setText(mDataset.get(position));


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
