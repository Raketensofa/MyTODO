package database;


/**
 * Created by Carolin on 21.04.2017.
 */
public abstract class Queries {

    public final static String DATATYPE_TEXT = "TEXT";
    public final static String DATATYPE_INTEGER = "INTEGER";
    public final static String PRIMARY_KEY_AUTO = "PRIMARY KEY AUTOINCREMENT";
    public final static String NOT_NOLL = "NOT NULL";

    public final static String TABLE_TODOS = "todos";
    public final static String TABLE_TODO_CONTACTS = "todo_contacts";


    public final static String COLUMN_ID = "_id";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_DESCRIPTION = "description";
    public final static String COLUMN_DEADLINE_DATE = "deadline_date";
    public final static String COLUMN_DEADLINE_TIME = "deadline_time";
    public final static String COLUMN_ISFAVOURITE = "is_favourite";
    public final static String COLUMN_ISDONE = "is_done";
    public final static String COLUMN_CONTACT_ID= "contact_id";
    public final static String COLUMN_TODO_ID = "todo_id";



    public final static String CREATE_TABLE_TODO_CONTACTS =
            "CREATE TABLE " +  TABLE_TODO_CONTACTS + "(" +
                    COLUMN_ID  + " " +  DATATYPE_INTEGER + " " + PRIMARY_KEY_AUTO + ", " +
                    COLUMN_TODO_ID + " " +  DATATYPE_INTEGER  + " " + NOT_NOLL + "," +
                    COLUMN_CONTACT_ID + " " + DATATYPE_INTEGER  + " " + NOT_NOLL + "," +
                    " FOREIGN KEY(" + COLUMN_TODO_ID + ") REFERENCES " + TABLE_TODOS + "(" + COLUMN_ID +")" + ")";


    public final static String CREATE_TABLE_TODOS =
                            "CREATE TABLE " +  TABLE_TODOS + "(" +
                                    COLUMN_ID  + " " +  DATATYPE_INTEGER + " " + PRIMARY_KEY_AUTO + ", " +
                                    COLUMN_NAME + " " +  DATATYPE_TEXT + " " + NOT_NOLL + ", " +
                                    COLUMN_DESCRIPTION  + " " +  DATATYPE_TEXT  +  ", " +
                                    COLUMN_DEADLINE_DATE + " " +  DATATYPE_TEXT + ", " +
                                    COLUMN_DEADLINE_TIME + " " +  DATATYPE_TEXT + ", " +
                                    COLUMN_ISFAVOURITE + " " +  DATATYPE_INTEGER  + " " + NOT_NOLL + ", " +
                                    COLUMN_ISDONE + " " +  DATATYPE_INTEGER  + " " + NOT_NOLL + ")";


    public final static String[] COLUMNS_TABLE_TODOS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_DESCRIPTION ,
            COLUMN_DEADLINE_DATE ,
            COLUMN_DEADLINE_TIME,
            COLUMN_ISFAVOURITE,
            COLUMN_ISDONE
    };

    public final static String[] COLUMNS_TABLE_TODO_CONTACTS = {
            COLUMN_ID,
            COLUMN_TODO_ID,
            COLUMN_CONTACT_ID
    };


}
