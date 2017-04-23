package model;

import java.util.Date;

/**
 * Created by Carolin on 21.04.2017.
 */
public class Todo extends Model {

    //region Attribute
    private String name;
    private String description;
    private boolean isDone;
    private boolean isFavourite;
    private Date deadline;

    //endregion

    public Todo(String name, String description, boolean isDone, boolean isFavourite, Date deadline) {
        this.name = name;
        this.description = description;
        this.isDone = isDone;
        this.isFavourite = isFavourite;
        this.deadline = deadline;
    }


    //region Getter und Setter

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

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    //endregion


}
