package database;

import android.content.res.Resources;

import cgellner.mytodo.R;

/**
 * Created by Carolin on 21.04.2017.
 */
public abstract class Queries {

    private final static String DATATYPE_TEXT = "TEXT";
    private final static String DATATYPE_NUMERIC = "NUMERIC";
    private final static String DATATYPE_INTEGER = "INTEGER";
    private final static String PRIMARY_KEY_AUTO = "PRIMARY KEY AUTOINCREMENT";
    private final static String NOT_NOLL = "NOT NULL";

    private final static String TABLE_TODOS = "todos";

    private final static String COLUMN_ID = "id";
    private final static String COLUMN_NAME = "name";
    private final static String COLUMN_DESCRIPTION = "description";
    private final static String COLUMN_DEADLINE = "deadline";
    private final static String COLUMN_ISFAVOURITE = "is_favourite";
    private final static String COLUMN_ISDONE = "is_done";



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
