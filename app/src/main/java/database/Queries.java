package database;

import cgellner.mytodo.R;

/**
 * Created by Carolin on 21.04.2017.
 */
public abstract class Queries {

    public final static String CREATE_TABLE_TODOS = "";



    public final static String[] COLUMNS_TABLE_TODOS = {
            String.valueOf(R.string.column_id),
            String.valueOf(R.string.column_name),
            String.valueOf(R.string.column_description),
            String.valueOf(R.string.column_is_done),
            String.valueOf(R.string.column_is_favourite),
            String.valueOf(R.string.column_is_deadline)};



}
