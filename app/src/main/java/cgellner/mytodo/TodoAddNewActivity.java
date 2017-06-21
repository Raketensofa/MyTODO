package cgellner.mytodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import elements.DateAndTimePicker;
import model.Contact;
import model.TodoItem;

public class TodoAddNewActivity extends Activity {

    private String TAG = TodoAddNewActivity.class.getSimpleName();


    static final int PICK_CONTACT_REQUEST = 1;


    private MenuItem itemCancel;
    private MenuItem itemSave;

    private ArrayAdapter<Contact> contactListViewAdapter;
    ListView contactlistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail_activity_add_view);

        Toolbar toolbar = (Toolbar)findViewById(R.id.todo_form_toolbar);
        setActionBar(toolbar);
        getActionBar().setTitle("Neues TODO");


        View isDoneView = findViewById(R.id.todo_detail_view_done);
        isDoneView.setVisibility(View.INVISIBLE);


        final Switch isFav = (Switch)findViewById(R.id.todo_detail_view_favourite_switch);
        final TextView isFavText = (TextView)findViewById(R.id.todo_detail_view_favourite_textview);
        isFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isFavText.setText("Hohe Priorität");
                }else {
                    isFavText.setText("Normale Priorität");
                }
            }
        });


        setListener();

        initContactListAdapter();

        contactlistView = (ListView)findViewById(R.id.todo_detail_view_contacts_listview);
        contactlistView.setAdapter(contactListViewAdapter);
        contactListViewAdapter.setNotifyOnChange(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        createNewTodoMenu(menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

                case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void createNewTodoMenu(Menu menu){

        itemSave = menu.add(Menu.NONE,
                R.id.action_todo_save,
                1, "Speichern");
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemSave.setIcon(R.drawable.save_icon_345);

        itemSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                TodoItem newItem = readInputDataFromComponents();

                Intent intent = new Intent();
                intent.putExtra("TODO_ITEM", newItem);
                setResult(R.integer.SAVE_TODO, intent);
                finish();

                return false;
            }
        });


        itemCancel = menu.add(Menu.NONE,
                R.id.action_cancel_todo,
                2, "Abbrechen");
        itemCancel.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemCancel.setIcon(R.drawable.ic_cancel_white_24dp);

        itemCancel.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                finish();
                return false;
            }
        });
    }


    private void setListener(){

        final TextView dateTextview = (TextView) findViewById(R.id.todo_detail_view_date_textview);
        dateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               DateAndTimePicker.startDatePickerDailog(dateTextview, 0);
            }
        });

        final TextView timeTextview = (TextView) findViewById(R.id.todo_detail_view_time_textview);
        timeTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               DateAndTimePicker.startTimePickerDialog(timeTextview, 0);
            }
        });


        final ImageView imageViewNewContact = (ImageView) findViewById(R.id.todo_detail_view_contact_add_icon);
        imageViewNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addContact();
            }
        });

    }


    private TodoItem readInputDataFromComponents(){

        TodoItem item = new TodoItem();

        EditText name = (EditText)findViewById(R.id.todo_detail_view_name_edittext);
        EditText descr = (EditText)findViewById(R.id.todo_detail_view_description_edittext);
        TextView date = (TextView)findViewById(R.id.todo_detail_view_date_textview);
        TextView time = (TextView)findViewById(R.id.todo_detail_view_time_textview);
        Switch isFav = (Switch)findViewById(R.id.todo_detail_view_favourite_switch);

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


        String contacts = null;
        if(contactListViewAdapter.getCount() > 0){
            for(int i = 0; i < contactListViewAdapter.getCount(); i++){
                contacts += contactListViewAdapter.getItem(i).getUri() + ";";
            }
        }

        item.setContacts(contacts);

        return item;
    }


    public void addContact() {

        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PICK_CONTACT_REQUEST && resultCode ==  RESULT_OK){

            TodoContactProvider contactProvider = new TodoContactProvider(this);
            Log.i(TAG, "URI: " + data.getData().toString());
            Contact selectedContact = contactProvider.getContactDataFromUri(data.getData());
            Log.i(TAG, "selected Contact: " + selectedContact.toString());

            contactListViewAdapter.add(selectedContact);
            contactListViewAdapter.notifyDataSetChanged();
        }
    }


    public class ItemViewHolder {

        public Contact contact;
        public TextView contactName;

        public ImageView sendSMS;
        public ImageView sendMail;
    }


    private void initContactListAdapter(){

        contactListViewAdapter = new ArrayAdapter<Contact>(this, R.layout.contact_list_item) {

            @NonNull
            @Override
            public View getView(int position, View itemView, ViewGroup parent) {

                //Initialsierung der View-Elemente
                if (itemView == null) {

                    itemView = getLayoutInflater().inflate(R.layout.contact_list_item, null);
                    TextView itemNameView = (TextView) itemView.findViewById(R.id.textview_contact_name);
                    ImageView sms = (ImageView)itemView.findViewById(R.id.imageview_sms);
                    ImageView mail = (ImageView)itemView.findViewById(R.id.imageview_mail);

                    ItemViewHolder itemViewHolder = new ItemViewHolder();
                    itemViewHolder.contactName = itemNameView;
                    itemViewHolder.sendMail = mail;
                    itemViewHolder.sendSMS = sms;

                    itemView.setTag(itemViewHolder);
                }

                final Contact contact = getItem(position);
                final ItemViewHolder viewHolder = (ItemViewHolder) itemView.getTag();

                viewHolder.contact = contact;
                viewHolder.contactName.setText(contact.getName());


                if(contact.getEmail() != null) {
                    viewHolder.sendMail.setVisibility(View.VISIBLE);

                    //TODO Mail versenden


                }else{
                    viewHolder.sendMail.setVisibility(View.INVISIBLE);
                }

                if(contact.getPhone() != null) {
                    viewHolder.sendSMS.setVisibility(View.VISIBLE);

                    //TODO SMS versenden


                }else{
                    viewHolder.sendSMS.setVisibility(View.INVISIBLE);
                }

                return itemView;
            }
        };
    }


}
