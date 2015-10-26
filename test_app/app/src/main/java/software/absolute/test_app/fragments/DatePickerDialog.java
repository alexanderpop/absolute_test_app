package software.absolute.test_app.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by pop on 10/6/2014.
 */
public class DatePickerDialog extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {

    private int year, month, day;
    private MyDialogFragmentListener dialogCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, +1);
        long maxDate = c.getTime().getTime();

        final android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(getActivity(), STYLE_NORMAL, getConstructorListener(), year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate(maxDate);
        if (hasJellyBeanAndAbove()) {
            datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    getActivity().getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatePicker dp = datePickerDialog.getDatePicker();
                            onDateSet(dp,
                                    dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
                        }
                    });
            datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                    getActivity().getString(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
        }
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
        year = selectedYear;
        month = selectedMonth;
        day = selectedDay;

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, +1);

        Calendar minCalendar = Calendar.getInstance();
        c.setTime(date);


        if (selectedYear >= minCalendar.get(Calendar.YEAR) && selectedYear <= c.get(Calendar.YEAR)) {
            if (selectedMonth == minCalendar.get(Calendar.MONTH)) {
                if (selectedDay >= minCalendar.get(Calendar.DAY_OF_MONTH)) {
                    dialogCallback.onReturnValue(year, month, day);
                } else {
//                    dialogCallback.onReturnValue(minCalendar.get(Calendar.YEAR), minCalendar.get(Calendar.MONTH), minCalendar.get(Calendar.DAY_OF_MONTH));
                    Toast.makeText(getActivity(), "Minimum date has to be today's date", Toast.LENGTH_SHORT).show();
                }
            } else if (selectedMonth == c.get(Calendar.MONTH) + 1) {
                if (selectedDay <= c.get(Calendar.DAY_OF_MONTH)) {
                    dialogCallback.onReturnValue(year, month, day);
                } else {
//                    dialogCallback.onReturnValue(minCalendar.get(Calendar.YEAR), minCalendar.get(Calendar.MONTH), minCalendar.get(Calendar.DAY_OF_MONTH));
                    Toast.makeText(getActivity(), "You can select only one month in advance", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (selectedMonth < minCalendar.get(Calendar.MONTH) && selectedDay <= c.get(Calendar.DAY_OF_MONTH)) {
                    dialogCallback.onReturnValue(year, month, day);
                } else {
//                    dialogCallback.onReturnValue(minCalendar.get(Calendar.YEAR), minCalendar.get(Calendar.MONTH), minCalendar.get(Calendar.DAY_OF_MONTH));
                    Toast.makeText(getActivity(), "You can select only one month in advance", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private static boolean hasJellyBeanAndAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    private android.app.DatePickerDialog.OnDateSetListener getConstructorListener() {
        return hasJellyBeanAndAbove() ? null : this;
    }

    public interface MyDialogFragmentListener {
        void onReturnValue(final int year, final int month, final int day);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogCallback = (MyDialogFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MyDialogFragmentListener");
        }
    }
}
