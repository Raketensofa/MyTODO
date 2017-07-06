package cgellner.mytodo.basic_operations;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cgellner.mytodo.R;
import cgellner.mytodo.activities.NewTodoActivity;
import cgellner.mytodo.activities.DetailTodoActivity;
import cgellner.mytodo.elements.DateAndTimePicker;
import cgellner.mytodo.elements.TodoContactProvider;
import cgellner.mytodo.model.Contact;
import cgellner.mytodo.model.TodoItem;

/**
 * Created by Carolin on 22.06.2017.
 */
public class BasicDetailViewOperations {

    private String TAG = BasicDetailViewOperations.class.getSimpleName();

    public ArrayAdapter<Contact> getContactListViewAdapter() {
          return contactListViewAdapter;
    }

    private ArrayAdapter<Contact> contactListViewAdapter;
    private ListView contactlistView;
    private Activity baseActivity;
    private TodoItem currentTodoItem;

    public BasicDetailViewOperations(Activity baseActivity, TodoItem currentTodoItem){
         this.baseActivity = baseActivity;
        this.currentTodoItem = currentTodoItem;

        setComponentListener();

        initContactListAdapter();

        initDoneAndFavourite(baseActivity);

        contactlistView = (ListView)baseActivity.findViewById(R.id.todo_detail_view_contacts_listview);
        contactlistView.setAdapter(contactListViewAdapter);
        contactListViewAdapter.setNotifyOnChange(true);

        //showContactItemEements(true);
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


    public void showContactItemEements(boolean isEditMode) {

        if (contactListViewAdapter != null && contactListViewAdapter.getCount() > 0) {

            for (int i = 0; i < contactListViewAdapter.getCount(); i++) {
                //View item = contactlistView.getChildAt(i+1);

                View item = null;
                item = contactListViewAdapter.getView(i, item, contactlistView);

                if (isEditMode) {
                    item.findViewById(R.id.imageview_delete_contact).setVisibility(View.VISIBLE);
                    item.findViewById(R.id.imageview_mail).setVisibility(View.INVISIBLE);
                    item.findViewById(R.id.imageview_sms).setVisibility(View.INVISIBLE);

                } else {

                    item.findViewById(R.id.imageview_delete_contact).setVisibility(View.INVISIBLE);

                    if (contactListViewAdapter.getItem(i).getEmail().length() > 0 && contactlistView != null) {
                        item.findViewById(R.id.imageview_mail).setVisibility(View.VISIBLE);
                    } else {
                        item.findViewById(R.id.imageview_mail).setVisibility(View.INVISIBLE);
                    }

                    if (contactListViewAdapter.getItem(i).getPhone().length() > 0 && contactlistView != null) {
                        item.findViewById(R.id.imageview_sms).setVisibility(View.VISIBLE);
                    } else {
                        item.findViewById(R.id.imageview_sms).setVisibility(View.INVISIBLE);
                    }
                }
            }
            contactListViewAdapter.notifyDataSetChanged();
        }
    }


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


                if (contact.getEmail().length() > 0) {
                    viewHolder.sendMail.setVisibility(View.VISIBLE);

                    viewHolder.sendMail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendEMail(contact.getEmail(), currentTodoItem.getName(), currentTodoItem.getDescription());
                        }
                    });

                } else {
                    viewHolder.sendMail.setVisibility(View.INVISIBLE);
                }

                if (contact.getPhone().length() > 0) {
                    viewHolder.sendSMS.setVisibility(View.VISIBLE);
                    viewHolder.sendSMS.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendSMS(contact.getPhone(), currentTodoItem.getName(), currentTodoItem.getDescription());
                        }
                    });

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

    private void sendSMS(String phoneNumber, String todoTitle, String todoDescription) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("sms:" + phoneNumber));
        intent.putExtra("sms_body", "## TODO: " + todoTitle + " ##\n\n" + todoDescription);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        baseActivity.startActivity(intent);
    }


    private void sendEMail(String mail, String title, String mail_body) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_SUBJECT, "TODO: " + title);
        intent.putExtra(Intent.EXTRA_TEXT, mail_body);
        intent.setData(Uri.parse("mailto:" + mail));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            baseActivity.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(baseActivity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


}
