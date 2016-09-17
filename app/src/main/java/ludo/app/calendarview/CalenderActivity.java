
package ludo.app.calendarview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class CalenderActivity extends Activity {

    private TextView mDateTextView;
    private ImageButton mPreviousButton, mNextButton;
    private GridView mGridView;

    private List<String> mDayList;
    private CalendarAdapter2 mCalendarAdapter2;

    private Calendar mGregorianCalendar;
    private GregorianCalendar mPreviousMonth;
    private GregorianCalendar mPreviousMonthMaxSet;

    private DateFormat mDateFormat;
    private String mCurrentDate;
    private int mFirstDay;
    private int mLastSelectedPosition = -1;
    private String mSelectedDate;

    private int getMaxP() {
        int maxP;
        if (mGregorianCalendar.get(GregorianCalendar.MONTH) == mGregorianCalendar
                .getActualMinimum(GregorianCalendar.MONTH)) {
            mPreviousMonth.set((mGregorianCalendar.get(GregorianCalendar.YEAR) - 1),
                    mGregorianCalendar.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            mPreviousMonth.set(GregorianCalendar.MONTH,
                    mGregorianCalendar.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = mPreviousMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        return maxP;
    }

    private void setNextMonth() {
        if (mGregorianCalendar.get(GregorianCalendar.MONTH) == mGregorianCalendar
                .getActualMaximum(GregorianCalendar.MONTH)) {
            mGregorianCalendar.set((mGregorianCalendar.get(GregorianCalendar.YEAR) + 1),
                    mGregorianCalendar.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            mGregorianCalendar.set(GregorianCalendar.MONTH,
                    mGregorianCalendar.get(GregorianCalendar.MONTH) + 1);
        }
    }

    private void setPreviousMonth() {
        if (mGregorianCalendar.get(GregorianCalendar.MONTH) == mGregorianCalendar
                .getActualMinimum(GregorianCalendar.MONTH)) {
            mGregorianCalendar.set((mGregorianCalendar.get(GregorianCalendar.YEAR) - 1),
                    mGregorianCalendar.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            mGregorianCalendar.set(GregorianCalendar.MONTH,
                    mGregorianCalendar.get(GregorianCalendar.MONTH) - 1);
        }
    }

    private void refreshCalendar() {
        mDayList.clear();
        for (String s : getResources().getStringArray(R.array.day_of_week)) {
            mDayList.add(s);
        }
        mPreviousMonth = (GregorianCalendar) mGregorianCalendar.clone();
        mFirstDay = mGregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) - 1;
        if (mFirstDay == 0) {
            mFirstDay = 7;
        }
        int maxWeeknumber = mFirstDay == 7
                ? mGregorianCalendar.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH) + 1
                : mGregorianCalendar.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        int monthLength = maxWeeknumber * 7;
        int maxP = getMaxP();
        int calMaxP = maxP - (mFirstDay - 1);
        mPreviousMonthMaxSet = (GregorianCalendar) mPreviousMonth.clone();
        mPreviousMonthMaxSet.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);
        String value;
        for (int i = 0; i < monthLength; i++) {
            value = mDateFormat.format(mPreviousMonthMaxSet.getTime());
            mPreviousMonthMaxSet.add(GregorianCalendar.DATE, 1);
            mDayList.add(value);
        }
        if (mDayList.size() <= 42) {
            for (int i = 0; i <= 6; i++) {
                mDayList.add(CalendarAdapter2.DEFAULT_VALUE);
            }
        }
        mCalendarAdapter2.setFirstDay(mFirstDay);
        mDateTextView
                .setText(android.text.format.DateFormat.format("yyyy MMMM", mGregorianCalendar));
        if (!TextUtils.isEmpty(mSelectedDate)) {
            for (int i = 0; i < mDayList.size(); i++) {
                if (mDayList.get(i).equals(mSelectedDate)) {
                    mCalendarAdapter2.setLastSelecteDay(mDayList.get(i));
                    break;
                }
            }
        }
        mCalendarAdapter2.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calender);

        Locale.setDefault(Locale.US);

        mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        mDateTextView = (TextView) findViewById(R.id.tv_month);
        mPreviousButton = (ImageButton) findViewById(R.id.ib_prev);
        mNextButton = (ImageButton) findViewById(R.id.Ib_next);
        mGridView = (GridView) findViewById(R.id.gv_calendar);

        mDayList = new ArrayList<String>();
        mCalendarAdapter2 = new CalendarAdapter2(getApplicationContext(), mDayList);
        mGridView.setAdapter(mCalendarAdapter2);

        mGregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
        mCurrentDate = mDateFormat.format(mGregorianCalendar.getTime());
        mCalendarAdapter2.setCurrentDay(mCurrentDate);
        mGregorianCalendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
        refreshCalendar();

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 6) {
                    String selectedGridDate = mDayList.get(position);
                    if (!selectedGridDate.equalsIgnoreCase(CalendarAdapter2.DEFAULT_VALUE)) {
                        String[] separatedTime = selectedGridDate.split("-");
                        String day = separatedTime[2].replaceFirst("^0*", "");
                        if ((Integer.parseInt(day) > 1) && (position < mFirstDay + 7)) {
                        } else if ((Integer.parseInt(day) <= 7) && (position > 28)) {
                        } else {
                            if (mLastSelectedPosition != -1) {
                                parent.getChildAt(mLastSelectedPosition)
                                        .setBackgroundColor(ContextCompat
                                                .getColor(getApplicationContext(),
                                                        android.R.color.white));
                            }
                            for (int i = 0; i < mDayList.size(); i++) {
                                if (mDayList.get(i).equals(mCurrentDate)) {
                                    parent.getChildAt(i).setBackgroundColor(Color.BLUE);
                                    break;
                                }
                            }
                            view.setBackgroundColor(Color.RED);
                            mLastSelectedPosition = position;
                            mSelectedDate = selectedGridDate;
                            //TODO set selected date here
                        }
                    }
                }
            }
        });

        ((Button) findViewById(R.id.get)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CalenderActivity.this, mSelectedDate, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getWeekDay(String date) {
        String[] part = date.split("-");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(part[0]), Integer.parseInt(part[1].replaceFirst("^0*", "")),
                Integer.parseInt(part[2].replaceFirst("^0*", "")));
        return c.get(Calendar.DAY_OF_WEEK);
    }
}
