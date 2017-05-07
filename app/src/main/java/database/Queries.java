package database;

import android.content.res.Resources;

import cgellner.mytodo.R;

/**
 * Created by Carolin on 21.04.2017.
 */
public abstract class Queries {

    public final static String DATATYPE_TEXT = "TEXT";
    public final static String DATATYPE_NUMERIC = "NUMERIC";
    public final static String DATATYPE_INTEGER = "INTEGER";
    public final static String PRIMARY_KEY_AUTO = "PRIMARY KEY AUTOINCREMENT";
    public final static String NOT_NOLL = "NOT NULL";

    public final static String TABLE_TODOS = "todos";

    public final static String COLUMN_ID = "id";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_DESCRIPTION = "description";
    public final static String COLUMN_DEADLINE = "deadline";
    public final static String COLUMN_ISFAVOURITE = "is_favourite";
    public final static String COLUMN_ISDONE = "is_done";



    public final static String CREATE_TABLE_TODOS =
                            "CREATE TABLE " +  TABLE_TODOS + "(" +
                                    COLUMN_ID  + " " +  DATATYPE_INTEGER + " " + PRIMARY_KEY_AUTO + ", " +
                                    COLUMN_NAME + " " +  DATATYPE_TEXT + " " + NOT_NOLL + ", " +
                                    COLUMN_DESCRIPTION  + " " +  DATATYPE_TEXT  +  ", " +
                                    COLUMN_DEADLINE + " " +  DATATYPE_NUMERIC + ", " +
                                    COLUMN_ISFAVOURITE + " " +  DATATYPE_NUMERIC  + " " + NOT_NOLL + ", " +
                                    COLUMN_ISDONE + " " +  DATATYPE_NUMERIC  + " " + NOT_NOLL + ")";


    public final static String[] COLUMNS_TABLE_TODOS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_DESCRIPTION ,
            COLUMN_DEADLINE ,
            COLUMN_ISFAVOURITE,
            COLUMN_ISDONE };




}
