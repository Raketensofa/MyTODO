package model;

/**
 * Created by Carolin on 21.04.2017.
 */
public class Contact{

    private long _id;
    private String name;
    private String email;
    private String phone;

    public Contact() {

    }

    public Contact(String name) {
        this.name = name;
    }

    public Contact(long _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public Contact(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Contact(long _id, String name, String email, String phone) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
