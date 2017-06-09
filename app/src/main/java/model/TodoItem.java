package model;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import java.io.Serializable;

import database.Queries;

/**
 * Created by Carolin on 21.04.2017.
 */
public class TodoItem implements Serializable{

    //region Attribute
    private long id;
    private String name;
    private String description;
    private int isDone; //1=is done - 0=is not done
    private int isFavourite;  //1=is favourite - 0=is not favourite
    private String deadlineDate;
    private String deadlineTime;
    //private ArrayList<Contact> contacts;

    //endregion



    public TodoItem() {
    }

//region Getter und Setter

   /** public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    //endregion


    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isDone=" + isDone +
                ", isFavourite=" + isFavourite +
                ", deadlineDate='" + deadlineDate + '\'' +
                ", deadlineTime='" + deadlineTime + '\'' +
                '}';
    }


    public void putToIntentExtras(Intent intent){

        intent.putExtra(Queries.COLUMN_ID, id);
        intent.putExtra(Queries.COLUMN_NAME, name);
        intent.putExtra(Queries.COLUMN_DESCRIPTION, description);
        intent.putExtra(Queries.COLUMN_ISFAVOURITE, isFavourite);
        intent.putExtra(Queries.COLUMN_ISDONE, isDone);
        intent.putExtra(Queries.COLUMN_DEADLINE_DATE, deadlineDate);
        intent.putExtra(Queries.COLUMN_DEADLINE_TIME, deadlineTime);

    }



    public void setAllDataFromCursor(Cursor cursor) {

        if(cursor != null) {

            id = cursor.getLong(cursor.getColumnIndexOrThrow(Queries.COLUMN_ID));
            name = cursor.getString(cursor.getColumnIndexOrThrow(Queries.COLUMN_NAME));
            description = cursor.getString(cursor.getColumnIndexOrThrow(Queries.COLUMN_DESCRIPTION));
            deadlineDate = cursor.getString(cursor.getColumnIndexOrThrow(Queries.COLUMN_DEADLINE_DATE));
            deadlineTime = cursor.getString(cursor.getColumnIndexOrThrow(Queries.COLUMN_DEADLINE_TIME));
            isDone = cursor.getInt(cursor.getColumnIndexOrThrow(Queries.COLUMN_ISDONE));
            isFavourite = cursor.getInt(cursor.getColumnIndexOrThrow(Queries.COLUMN_ISFAVOURITE));
        }
    }


    public ContentValues createContentValues(){

        ContentValues todoValues = new ContentValues();
        todoValues.put(Queries.COLUMN_NAME, name);
        todoValues.put(Queries.COLUMN_DESCRIPTION, description);
        todoValues.put(Queries.COLUMN_ISFAVOURITE, isFavourite);
        todoValues.put(Queries.COLUMN_ISDONE, isDone);
        todoValues.put(Queries.COLUMN_DEADLINE_DATE, deadlineDate);
        todoValues.put(Queries.COLUMN_DEADLINE_TIME, deadlineTime);

        return todoValues;
    }


}
