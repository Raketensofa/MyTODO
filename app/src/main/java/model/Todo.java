package model;

/**
 * Created by Carolin on 21.04.2017.
 */
public class Todo{

    //region Attribute
    private long _id;
    private String name;
    private String description;
    private int isDone; //1=is done - 0=is not done
    private int isFavourite;  //1=is favourite - 0=is not favourite
    private String deadlineDate;
    private String deadlineTime;

    //endregion




    public Todo(String name, String description, int isDone, int isFavourite, String deadlineDate, String deadlineTime) {
        this.name = name;
        this.description = description;
        this.isDone = isDone;
        this.isFavourite = isFavourite;
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
    }

    public Todo(int id, String name, String description, int isDone, int isFavourite, String date, String time) {

        this._id = id;
        this.name = name;
        this.description = description;
        this.isDone = isDone;
        this.isFavourite = isFavourite;
        this.deadlineTime = time;
        this.deadlineDate = date;
    }

    public Todo() {
    }

//region Getter und Setter


    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
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
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isDone=" + isDone +
                ", isFavourite=" + isFavourite +
                ", deadlineDate='" + deadlineDate + '\'' +
                ", deadlineTime='" + deadlineTime + '\'' +
                '}';
    }
}
