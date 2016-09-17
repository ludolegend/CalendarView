
package ludo.app.calendarview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Ludo on 9/12/16.
 */
public class RectangleLayout extends LinearLayout {
    public RectangleLayout(Context context) {
        super(context);
    }

    public RectangleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // divide width by 2 and set to height for creating a rectangle layout
        super.onMeasure(widthMeasureSpec, (int) (widthMeasureSpec*0.5));
    }
}
