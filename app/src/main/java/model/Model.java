package model;

import java.util.Date;

/**
 * Created by Carolin on 21.04.2017.
 */
public class Model {

    private long id;
    private Date created;
    private Date lastUpdate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
