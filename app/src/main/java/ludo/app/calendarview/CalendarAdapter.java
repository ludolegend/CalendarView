
package ludo.app.calendarview;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ludo on 9/16/16.
 */
public class CalendarAdapter extends BaseAdapter {

    public static String DEFAULT_VALUE = "default";

    private Context mContext;

    private List<String> mDayList;

    private int mFirstDay;

    private String mCurrentDay;

    private String mLastSelecteDay;

    public void setLastSelecteDay(String mLastSelecteDay) {
        this.mLastSelecteDay = mLastSelecteDay;
    }

    public void setFirstDay(int mFirstDay) {
        this.mFirstDay = mFirstDay;
    }

    public void setCurrentDay(String mCurrentDay) {
        this.mCurrentDay = mCurrentDay;
    }

    public CalendarAdapter(Context mContext, List<String> mDayList) {
        this.mContext = mContext;
        this.mDayList = mDayList;
    }

    @Override
    public int getCount() {
        return mDayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_calendar, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.day = (TextView) view.findViewById(R.id.date);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String date = mDayList.get(position);
        // ignore first 6 postion since there are day of week title
        if (position > 6) {
            if (!date.equalsIgnoreCase(DEFAULT_VALUE)) {
                String[] separatedTime = date.split("-");
                String day = separatedTime[2].replaceFirst("^0*", "");
                // check days of previous month and days of next month then set them invisible
                if ((Integer.parseInt(day) > 1) && (position < mFirstDay + 7)) {
                    viewHolder.day.setVisibility(View.INVISIBLE);
                } else if ((Integer.parseInt(day) <= 7) && (position > 28)) {
                    viewHolder.day.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.day.setVisibility(View.VISIBLE);
                }
                // check current day and set blue color for it, others will be white color
                if (date.equals(mCurrentDay)) {
                    view.setBackgroundColor(Color.BLUE);
                } else {
                    view.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
                }
                viewHolder.day.setText(day);
            } else {
                viewHolder.day.setVisibility(View.INVISIBLE);
            }
        } else {
            viewHolder.day.setText(date);
        }
        // check for last selected day
        if (!TextUtils.isEmpty(mLastSelecteDay)) {
            if (date.equals(mLastSelecteDay)) {
                // only display for day that was displaying visible
                if (viewHolder.day.getVisibility() == View.VISIBLE) {
                    view.setBackgroundColor(Color.RED);
                    // clear it after found in order to ignore duplicate
                    mLastSelecteDay = "";
                }
            }
        }
        return view;
    }

    private class ViewHolder {
        private TextView day;
    }
}
