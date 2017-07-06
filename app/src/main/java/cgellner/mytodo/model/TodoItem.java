package cgellner.mytodo.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cgellner.mytodo.database.Columns;

/**
 * Created by Carolin on 21.04.2017.
 */
public class TodoItem implements Serializable{

    //region Attribute

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("done")
    private boolean done; //1=is done - 0=is not done

    @SerializedName("favourite")
    private boolean favourite;  //1=is favourite - 0=is not favourite

    @SerializedName("expiry")
    private long expiry;

    @SerializedName("contacts")
    private ArrayList<String> contacts;

    //endregion



    public TodoItem() {
    }

//region Getter und Setter


    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }

    public long getExpiry() {
       return expiry;
   }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }



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

    public boolean getIsDone() {
        return done;
    }

    public void setIsDone(boolean isDone) {
        this.done = isDone;
    }

    public boolean getIsFavourite() {
        return favourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.favourite = isFavourite;
    }

    //endregion

    public void setAllDataFromCursor(Cursor cursor) {

        if(cursor != null) {

            id = cursor.getLong(cursor.getColumnIndexOrThrow(Columns.id.toString()));
            name = cursor.getString(cursor.getColumnIndexOrThrow(Columns.name.toString()));
            description = cursor.getString(cursor.getColumnIndexOrThrow(Columns.description.toString()));
            expiry = cursor.getLong(cursor.getColumnIndexOrThrow(Columns.expiry.toString()));
            String done = cursor.getString(cursor.getColumnIndexOrThrow(Columns.is_done.toString()));
            String conStr =  cursor.getString(cursor.getColumnIndexOrThrow(Columns.contacts.toString()));

            if(conStr.length() > 5) {
                String[] contac = conStr.split(";");
                List<String> list = Arrays.asList(contac);
                contacts = new ArrayList<String>(list);
            }

            if(done.equals(Boolean.TRUE.toString())){
                this.done = true;
            }else{
                this.done = false;
            }

            String favourite = cursor.getString(cursor.getColumnIndexOrThrow(Columns.is_favourite.toString()));
            if(favourite.equals(Boolean.TRUE.toString())){
                this.favourite = true;
            }else{
                this.favourite = false;
            }
        }
    }


    public ContentValues createContentValues(){

        ContentValues todoValues = new ContentValues();

        if(id > -1){
            todoValues.put(Columns.id.toString(), id);
        }
        todoValues.put(Columns.name.toString(), name);
        todoValues.put(Columns.description.toString(), description);
        todoValues.put(Columns.is_favourite.toString(), String.valueOf(favourite));
        todoValues.put(Columns.is_done.toString(), String.valueOf(done));
        todoValues.put(Columns.expiry.toString(), expiry);

        String allContacts = "";
        if(contacts != null && contacts.size() > 0) {
            for (String contact : contacts) {
                allContacts += contact + ";";
            }
            todoValues.put(Columns.contacts.toString(), allContacts);
        }else{
            todoValues.put(Columns.contacts.toString(), "");
        }

        return todoValues;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", done=" + done +
                ", favourite=" + favourite +
                ", expiry=" + expiry +
                ", contacts='" + contacts + '\'' +
                '}';
    }
}
