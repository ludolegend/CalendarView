
package ludo.app.calendarview;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends BaseAdapter {

    private Context mContext;

    private Calendar mMonth;

    private GregorianCalendar mPMonth;

    private GregorianCalendar mPMonthMaxSet;

    private GregorianCalendar mSelectedDate;

    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;

    private ArrayList<String> items;
    private List<String> mDayList;
    private View previousView;

    public CalendarAdapter(Context mContext, GregorianCalendar monthCalendar) {
        Locale.setDefault(Locale.US);
        mDayList = new ArrayList<String>();
        mMonth = monthCalendar;
        this.mContext = mContext;
        mSelectedDate = (GregorianCalendar) monthCalendar.clone();
        mMonth.set(GregorianCalendar.DAY_OF_MONTH, 1);
        items = new ArrayList<String>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(mSelectedDate.getTime());
        refreshDays();
    }

    public int getCount() {
        return mDayList.size();
    }

    public Object getItem(int position) {
        return mDayList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_calendar, null);
        }
        dayView = (TextView) v.findViewById(R.id.date);
        String value = mDayList.get(position);
        if (position <= 6) {
            dayView.setText(value);
        } else {
            if (!TextUtils.isEmpty(value)) {
                String[] separatedTime = value.split("-");
                String gridvalue = separatedTime[2].replaceFirst("^0*", "");

                if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay + 7)) {
                    dayView.setVisibility(View.GONE);
                } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
                    dayView.setVisibility(View.GONE);
                } else {
                    dayView.setVisibility(View.VISIBLE);
                    dayView.setTextColor(Color.BLACK);
                }
                if (mDayList.get(position).equals(curentDateString)) {
                    v.setBackgroundColor(Color.BLUE);
                } else {
                    v.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
                }
                dayView.setText(gridvalue);
            } else {
                dayView.setVisibility(View.GONE);
            }
        }
        return v;
    }

    public View setSelected(View view, int position, int gridvalue) {
        if (previousView != null) {
            previousView
                    .setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
        }
        if ((gridvalue > 10) && (position < 15)) {
        } else if ((gridvalue < 7) && (position > 28)) {
        } else {
            view.setBackgroundColor(Color.RED);
        }
        int len = mDayList.size();
        if (len > position) {
            if (!mDayList.get(position).equals(curentDateString)) {
                previousView = view;
            }
        }
        return view;
    }

    public void refreshDays() {
        items.clear();
        mDayList.clear();
        for (String s : mContext.getResources().getStringArray(R.array.day_of_week)) {
            mDayList.add(s);
        }
        Locale.setDefault(Locale.US);
        mPMonth = (GregorianCalendar) mMonth.clone();
        firstDay = mMonth.get(GregorianCalendar.DAY_OF_WEEK);
        maxWeeknumber = mMonth.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP();
        calMaxP = maxP - (firstDay - 1);
        mPMonthMaxSet = (GregorianCalendar) mPMonth.clone();
        mPMonthMaxSet.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);
        for (int n = 0; n < mnthlength; n++) {
            itemvalue = df.format(mPMonthMaxSet.getTime());
            mPMonthMaxSet.add(GregorianCalendar.DATE, 1);
            mDayList.add(itemvalue);
        }
        int size = mDayList.size();
        if (size <= 42) {
            for (int i = 0; i <= 6; i++) {
                mDayList.add("");
            }
        }
    }

    private int getMaxP() {
        int maxP;
        if (mMonth.get(GregorianCalendar.MONTH) == mMonth
                .getActualMinimum(GregorianCalendar.MONTH)) {
            mPMonth.set((mMonth.get(GregorianCalendar.YEAR) - 1),
                    mMonth.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            mPMonth.set(GregorianCalendar.MONTH,
                    mMonth.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = mPMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }

    public List<String> getDayString() {
        return mDayList;
    }

    public void checkLastSelected(GridView gridView, String date) {
        for (int i=0;i<mDayList.size();i++) {
            String temp = mDayList.get(i);
            if (!TextUtils.isEmpty(temp)) {
                if (date.equalsIgnoreCase(temp)) {
                    Toast.makeText(mContext, "Pos: "+i, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }
}
