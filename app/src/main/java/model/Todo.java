package model;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Carolin on 21.04.2017.
 */
public class Todo extends Model{

    //region Attribute
    private String name;
    private String description;
    private int isDone;
    private int isFavourite;
    private String deadlineDate;
    private String deadlineTime;

    //endregion

    public Todo(String name, String description, int isDone, int isFavourite, String date, String time) {
        this.name = name;
        this.description = description;
        this.isDone = isDone;
        this.isFavourite = isFavourite;
        this.deadlineTime = time;
        this.deadlineDate = date;
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
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isDone=" + isDone +
                ", isFavourite=" + isFavourite +
                ", deadlineDate='" + deadlineDate + '\'' +
                ", deadlineTime='" + deadlineTime + '\'' +
                ", created='" + getCreated().toString() + '\'' +
                '}';
    }


}
