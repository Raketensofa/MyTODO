package cgellner.mytodo;


import java.util.Comparator;

import model.TodoItem;

/**
 * Created by Carolin on 17.06.2017.
 */
public class TodoListSort implements Comparator<TodoItem> {


    private String TAG = TodoListSort.class.getSimpleName();

    private int SortMode = 0;
    private int SortParam = 0;

    public TodoListSort(int sortMode, int sortParam) {

        SortMode = sortMode;
        SortParam = sortParam;
    }


    @Override
    public int compare(TodoItem o1, TodoItem o2) {

        if (SortMode == R.integer.SORT_MODE_DEADLINE_FAVOURITE) {

            if (SortParam == 0) {

                if (o1.getIsDone() == true && o2.getIsDone() == false) {
                    return 1;
                } else {
                    return -1;
                }

            } else if (SortParam == 1) {

                if (o1.getIsDone() == o2.getIsDone()) {

                    if (o1.getExpiry() < o2.getExpiry()) {
                        return 1;
                    } else {
                        return -1;
                    }

                } else {
                    return -1;
                }

            } else if (SortParam == 2) {

                if ((o1.getIsDone() == o2.getIsDone()) && (o1.getExpiry() == o2.getExpiry())) {

                    if (o1.getIsFavourite() == false && o2.getIsFavourite() == true) {
                        return 1;
                    } else {
                        return -1;
                    }

                } else {
                    return -1;
                }
            }
        } else if (SortMode == R.integer.SORT_MODE_FAVOURITE_DEADLINE) {

            if (SortParam == 0) {

                    if (o1.getIsDone() == true && o2.getIsDone() == false) {
                        return 1;
                    } else {
                        return -1;
                    }

            } else if (SortParam == 1) {

                if (o1.getIsDone() == o2.getIsDone()) {

                    if (o1.getIsFavourite() == true && o2.getIsFavourite() == false) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }

            } else if (SortParam == 2) {

                if ((o1.getIsDone() == o2.getIsDone()) && (o1.getIsFavourite() == o2.getIsFavourite())) {

                    if (o1.getExpiry() > o2.getExpiry()) {
                        return 1;
                    } else {
                        return -1;
                    }

                } else {
                    return -1;
                }
            }
        }


        return 0;
    }

}
