package elements;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import model.Contact;

/**
 * Created by Carolin on 22.05.2017.
 */
public class TodoContactProvider {

    private static String TAG = TodoContactProvider.class.getSimpleName();

    private Context context;

    public TodoContactProvider(Context context) {
        this.context = context;
    }


    public Contact getContactDataFromUri(Uri uri){

        Contact contact = new Contact();

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        if(cursor != null) {

            cursor.moveToFirst();

            contact.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)));
            contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
            contact.setPhone(getPhoneNumberFromUri(uri));
            contact.setEmail(getEmailAddressFromUri(uri));
            contact.setUri(uri.toString());
        }

        Log.i(TAG, contact.toString());

        return contact;
    }


    public String getPhoneNumberFromUri(Uri uri){

      String phoneNumber = "";

      Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

      if(cursor != null) {

          cursor.moveToFirst();

           String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

           int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

           if (hasPhoneNumber == 1) {

               Cursor phoneCursor = context.getContentResolver().query(
                               ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                               ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " +
                                       contactID, null, null);


               while (phoneCursor.moveToNext()) {

                   int phoneType = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA2));
                   if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                       phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                   }
               }
           }
      }

       Log.i(TAG, "Phone Number: " + phoneNumber);

       return phoneNumber;
    }


    public String getEmailAddressFromUri(Uri uri){

        String email = "";

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        if(cursor != null) {

            cursor.moveToFirst();

            String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            if (cursor != null) {

                Cursor mailCursor = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " +
                                contactID, null, null);

                while (mailCursor.moveToNext()) {
                    email = mailCursor.getString(mailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                }
            }

        }

        Log.i(TAG, "E-mail: " + email);

        return email;
    }
}
