package nhannt.musicplayer.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by nhannt on 20/03/2017.
 * This seek bar can only show progress,user cant change by touching seek bar
 */

public class CustomSeekBar extends android.support.v7.widget.AppCompatSeekBar {
    public CustomSeekBar(Context context) {
        super(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
