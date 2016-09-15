
package ludo.app.calendarview;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalenderActivity extends Activity {

    private GregorianCalendar mGregorianCalendar;

    private CalendarAdapter mCalendarAdapter;

    private TextView mDateTextView;

    private GridView mGridView;

    private String mDate;

    private DateFormat mDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        mDate = mDateFormat.format(Calendar.getInstance().getTime());

        mDateTextView = (TextView) findViewById(R.id.tv_month);

        mGregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();

        mCalendarAdapter = new CalendarAdapter(this, mGregorianCalendar);

        mDateTextView
                .setText(android.text.format.DateFormat.format("MMMM yyyy", mGregorianCalendar));

        ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);
        previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
            }
        });

        mGridView = (GridView) findViewById(R.id.gv_calendar);
        mGridView.setAdapter(mCalendarAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
                int size = mCalendarAdapter.getDayString().size();
                if (position > 6) {
                    String curentDateString = mDateFormat.format(Calendar.getInstance().getTime());
                    String selectedGridDate = mCalendarAdapter.getDayString().get(position);
                    if (!TextUtils.isEmpty(selectedGridDate)) {
                        String[] separatedTime = selectedGridDate.split("-");
                        String gridvalueString = separatedTime[2].replaceFirst("^0*", "");
                        int gridvalue = Integer.parseInt(gridvalueString);
                        if (!curentDateString.equalsIgnoreCase(selectedGridDate)) {
                            if ((gridvalue > 10) && (position < 15)) {
                            } else if ((gridvalue < 7) && (position > 28)) {
                            } else {
                                mDate = selectedGridDate;
                                mCalendarAdapter.setSelected(v, position, gridvalue);
                            }
                        } else {
                            mDate = curentDateString;
                            refreshCalendar();
                        }
                    }
                }
            }

        });

        ((Button) findViewById(R.id.get)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CalenderActivity.this, mDate, Toast.LENGTH_SHORT).show();
            }
        });
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
        mCalendarAdapter.refreshDays();
        mCalendarAdapter.notifyDataSetChanged();
        mDateTextView
                .setText(android.text.format.DateFormat.format("MMMM yyyy", mGregorianCalendar));
        //mCalendarAdapter.checkLastSelected(mGridView, mDate);
    }

    private int getWeekDay(String date) {
        String[] part = date.split("-");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(part[0]), Integer.parseInt(part[1].replaceFirst("^0*", "")),
                Integer.parseInt(part[2].replaceFirst("^0*", "")));
        return c.get(Calendar.DAY_OF_WEEK);
    }
}
