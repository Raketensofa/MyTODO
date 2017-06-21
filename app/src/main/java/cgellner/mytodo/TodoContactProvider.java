package cgellner.mytodo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.Contact;

/**
 * Created by Carolin on 22.05.2017.
 */
public class TodoContactProvider {

    private static String TAG = TodoContactProvider.class.getSimpleName();

    private Context context;


    //SPEICHERN DER Uri der Kontakte in der DB und dann darüber immer die Daten auslesen

    public TodoContactProvider(Context context) {
        this.context = context;
    }


    public Contact getContactDataFromUri(Uri uri){

        Contact contact = new Contact();

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();

        //contact.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)));
        contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));

        /**
        int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

        if (hasPhoneNumber > 0) {

            int phoneType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA2));
            if(phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                contact.setPhone(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            }

        }
        contact.setEmail(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)));*/


        contact.setUri(uri.toString());

        return contact;
    }



  public String getPhoneNumberFromUri(Uri uri){

        String phoneNumber = null;



        return phoneNumber;
    }


    public String getEmailAddressFromUri(Uri uri){

        String email = null;



        return email;
    }



    private String getPhonNumber(Cursor cursor, ContentResolver contentResolver, long contact_id){

        String PhoneNumer = null;

        int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

        if (hasPhoneNumber > 0) {

            Cursor phoneCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    , null
                    , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                    , new String[]{String.valueOf(contact_id)}
                    , null);


            while (phoneCursor.moveToNext()) {

                int phoneType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA2));
                if(phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                    PhoneNumer = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
            }
            phoneCursor.close();
        }

        return PhoneNumer;
    }

    private String getMailAdress(Cursor cursor, ContentResolver contentResolver, long contact_id){

        String email = null;

        Cursor mailCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI
                    , null
                    , ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?"
                    , new String[]{String.valueOf(contact_id)}
                    , null);


            while (mailCursor.moveToNext()) {
                email = mailCursor.getString(mailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            }

            mailCursor.close();

        return email;
    }




}
