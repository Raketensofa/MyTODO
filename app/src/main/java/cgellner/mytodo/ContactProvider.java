package cgellner.mytodo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

import model.Contact;

/**
 * Created by Carolin on 22.05.2017.
 */
public class ContactProvider {


    private Context context;


    public ContactProvider(Context context) {
        this.context = context;
    }

    public ArrayList<Contact> getAllContactNamesFromDevice(){

        ArrayList<Contact> contactsList = null;
        Cursor cursorContacts = null;


        try{
                cursorContacts = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

                 if (cursorContacts.getCount() > 0) {

                     contactsList = new ArrayList<>();

                     while (cursorContacts.moveToNext()) {

                          Contact contact = new Contact();
                          long contact_id = cursorContacts.getLong(cursorContacts.getColumnIndex(ContactsContract.Contacts._ID));
                          String contact_display_name = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                          contact.set_id(contact_id);
                          contact.setName(contact_display_name);

                          Log.e("Contact", contact.toString());

                          contactsList.add(contact);
                     }
                 }

        } catch (Exception ex) {

            Log.e("Error on contact", ex.getMessage());
        }

        return contactsList;
    }


    private String getPhoneNumber(Cursor cursor, ContentResolver contentResolver, int contact_id){

        String phone = null;

        int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

        if (hasPhoneNumber > 0) {

            Cursor phoneCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    , null
                    , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                    , new String[]{String.valueOf(contact_id)}
                    , null);

            while (phoneCursor.moveToNext()) {

                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone = phoneNumber;
            }

            phoneCursor.close();
        }

        return phone;
    }

    private String getMailAdress(){



        return null;
    }
}
