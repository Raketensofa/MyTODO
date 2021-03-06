package cgellner.mytodo.elements;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Carolin on 09.06.2017.
 */
public abstract class DateAndTimePicker {

    private static String date;
    private static String time;

    public static void startDatePickerDailog(final TextView v, final long expiry){

            final Calendar myCalendar = Calendar.getInstance();

            if(expiry  > 0 ) {
                Date date = new Date(expiry);
                myCalendar.set(Calendar.YEAR, date.getYear());
                myCalendar.set(Calendar.MONTH, date.getMonth());
                myCalendar.set(Calendar.DAY_OF_MONTH, date.getDay());
            }

            final DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {


                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    String myFormat = "dd.MM.yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

                    date = sdf.format(myCalendar.getTime());
                    v.setText(date);
                }
            };

            new DatePickerDialog(v.getContext(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }



    public static void startTimePickerDialog(final TextView v, final long expiry){

        final Calendar myCalendar = Calendar.getInstance();

        final TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {

                        myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCalendar.set(Calendar.MINUTE, selectedMinute);

                        String myFormat = "HH:mm";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

                        time = sdf.format(myCalendar.getTime());
                        v.setText(time);
                    }
                };


        if(expiry  > 0 ) {
            Date date = new Date(expiry);
            myCalendar.set(Calendar.HOUR_OF_DAY, date.getHours());
            myCalendar.set(Calendar.MINUTE, date.getMinutes());
        }

        TimePickerDialog timePickerDialog= new TimePickerDialog(v.getContext(), timePickerListener, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();

    }

}
