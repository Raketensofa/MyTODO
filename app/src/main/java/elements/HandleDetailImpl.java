package elements;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cgellner.mytodo.R;
import cgellner.mytodo.NewTodoActivity;
import cgellner.mytodo.DetailTodoActivity;
import model.Contact;
import model.TodoItem;

/**
 * Created by Carolin on 22.06.2017.
 */
public class HandleDetailImpl {

    private String TAG = HandleDetailImpl.class.getSimpleName();


    private ArrayAdapter<Contact> contactListViewAdapter;
    private ListView contactlistView;
    private Activity baseActivity;

    public HandleDetailImpl(Activity baseActivity){
         this.baseActivity = baseActivity;

        setComponentListener();

        initContactListAdapter();

        initDoneAndFavourite(baseActivity);

        contactlistView = (ListView)baseActivity.findViewById(R.id.todo_detail_view_contacts_listview);
        contactlistView.setAdapter(contactListViewAdapter);
        contactListViewAdapter.setNotifyOnChange(true);
    }

    public void addNewContactToList(Uri uri){

        TodoContactProvider contactProvider = new TodoContactProvider(baseActivity);
        Contact selectedContact = contactProvider.getContactDataFromUri(uri);

        Log.i(TAG, "selected Contact: " + selectedContact.toString());

        contactListViewAdapter.add(selectedContact);
        contactListViewAdapter.notifyDataSetChanged();
    }

    public TodoItem readTodoDataFromComponents(){

        TodoItem item = new TodoItem();

        EditText name = (EditText)baseActivity.findViewById(R.id.todo_detail_view_name_edittext);
        EditText descr = (EditText)baseActivity.findViewById(R.id.todo_detail_view_description_edittext);
        TextView date = (TextView)baseActivity.findViewById(R.id.todo_detail_view_date_textview);
        TextView time = (TextView)baseActivity.findViewById(R.id.todo_detail_view_time_textview);
        Switch isFav = (Switch)baseActivity.findViewById(R.id.todo_detail_view_favourite_switch);

        item.setIsDone(false);
        item.setId(-1); //default
        item.setName(name.getText().toString());
        item.setDescription(descr.getText().toString());


        long expiry = 0;

        try {

            String dateTime = date.getText().toString() + " " + time.getText().toString();
            Date expiryDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(dateTime);
            expiry = expiryDate.getTime();

        }catch (ParseException ex){
            expiry = 0;
        }
        item.setExpiry(expiry);

        if(isFav.isChecked()){
            item.setIsFavourite(true);
        }else{
            item.setIsFavourite(false);
        }


        ArrayList<String> contacts = new ArrayList<>();
        if(contactListViewAdapter.getCount() > 0){
            for(int i = 0; i < contactListViewAdapter.getCount(); i++){
                contacts.add(contactListViewAdapter.getItem(i).getUri());
            }
        }

        item.setContacts(contacts);

        return item;
    }


    private void addContact() {

        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        baseActivity.startActivityForResult(pickContactIntent, R.integer.PICK_CONTACT_REQUEST );
    }


    private class ContactItemViewHolder {

        public Contact contact;
        public TextView contactName;

        public ImageView sendSMS;
        public ImageView sendMail;

        public ImageView deleteContact;
    }


    private void initDoneAndFavourite(Activity baseActivity){

        final Switch isDone = (Switch) baseActivity.findViewById(R.id.todo_detail_view_done_switch);
        final TextView isDoneText = (TextView) baseActivity.findViewById(R.id.todo_detail_view_done_textview);
        final Switch isFav = (Switch) baseActivity.findViewById(R.id.todo_detail_view_favourite_switch);
        final TextView isFavText = (TextView) baseActivity.findViewById(R.id.todo_detail_view_favourite_textview);
        final ImageView isDoneIcon = (ImageView) baseActivity.findViewById(R.id.todo_detail_view_done_icon);


        if(baseActivity.getClass().getSimpleName().equals(NewTodoActivity.class.getSimpleName())){
            isDone.setVisibility(View.INVISIBLE);
            isDoneText.setVisibility(View.INVISIBLE);
            isFav.setVisibility(View.VISIBLE);
            isFavText.setVisibility(View.VISIBLE);
            isDoneIcon.setVisibility(View.INVISIBLE);
        }else if(baseActivity.getClass().getSimpleName().equals(DetailTodoActivity.class.getSimpleName())){
            isDone.setVisibility(View.INVISIBLE);
            isDoneText.setVisibility(View.VISIBLE);
            isFav.setVisibility(View.INVISIBLE);
            isFavText.setVisibility(View.VISIBLE);
            isDoneIcon.setVisibility(View.VISIBLE);
        }

        isDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDoneText.setText(R.string.done_label);
                } else {
                    isDoneText.setText(R.string.not_done_label);
                }
            }
        });

        isFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isFavText.setText(R.string.isfavourite_label);
                } else {
                    isFavText.setText(R.string.is_not_favourite_label);
                }
            }
        });
    }


    private void setComponentListener(){

        final TextView dateTextview = (TextView) baseActivity.findViewById(R.id.todo_detail_view_date_textview);
        dateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateAndTimePicker.startDatePickerDailog(dateTextview, 0);
            }
        });

        final TextView timeTextview = (TextView) baseActivity.findViewById(R.id.todo_detail_view_time_textview);
        timeTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateAndTimePicker.startTimePickerDialog(timeTextview, 0);
            }
        });


        final ImageView imageViewNewContact = (ImageView) baseActivity.findViewById(R.id.todo_detail_view_contact_add_icon);
        imageViewNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addContact();
            }
        });

    }


    public void showAllDeleteContactViews(boolean show){

        if(contactListViewAdapter != null && contactListViewAdapter.getCount() > 0){

            for( int i = 1; i < contactlistView.getAdapter().getCount(); i++){

                if(show) {
                    contactlistView.getChildAt(i).findViewById(R.id.imageview_delete_contact).setVisibility(View.VISIBLE);

                }else{
                    contactlistView.getChildAt(i).findViewById(R.id.imageview_delete_contact).setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    //TODO Mail und SMS versenden
    private void initContactListAdapter() {

        contactListViewAdapter = new ArrayAdapter<Contact>(baseActivity, R.layout.contact_list_item) {

            @NonNull
            @Override
            public View getView(int position, View itemView, ViewGroup parent) {

                if (itemView == null) {

                    itemView = baseActivity.getLayoutInflater().inflate(R.layout.contact_list_item, null);
                    TextView itemNameView = (TextView) itemView.findViewById(R.id.textview_contact_name);
                    ImageView sms = (ImageView) itemView.findViewById(R.id.imageview_sms);
                    ImageView mail = (ImageView) itemView.findViewById(R.id.imageview_mail);
                    ImageView delete = (ImageView) itemView.findViewById(R.id.imageview_delete_contact);

                    ContactItemViewHolder itemViewHolder = new ContactItemViewHolder();
                    itemViewHolder.contactName = itemNameView;
                    itemViewHolder.sendMail = mail;
                    itemViewHolder.sendSMS = sms;
                    itemViewHolder.deleteContact = delete;

                    itemView.setTag(itemViewHolder);
                }

                final Contact contact = getItem(position);
                final ContactItemViewHolder viewHolder = (ContactItemViewHolder) itemView.getTag();

                viewHolder.contact = contact;
                viewHolder.contactName.setText(contact.getName());


                if (contact.getEmail() != null) {
                    viewHolder.sendMail.setVisibility(View.VISIBLE);

                    //TODO Mail versenden


                } else {
                    viewHolder.sendMail.setVisibility(View.INVISIBLE);
                }

                if (contact.getPhone() != null) {
                    viewHolder.sendSMS.setVisibility(View.VISIBLE);

                    //TODO SMS versenden


                } else {
                    viewHolder.sendSMS.setVisibility(View.INVISIBLE);
                }


                viewHolder.deleteContact.setClickable(true);
                viewHolder.deleteContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        contactListViewAdapter.remove(contact);
                        contactListViewAdapter.notifyDataSetChanged();
                    }
                });


                return itemView;
            }
        };
    }

}
