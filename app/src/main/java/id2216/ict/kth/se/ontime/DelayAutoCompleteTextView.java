package id2216.ict.kth.se.ontime;

/**
 * Created by jronn on 2015-02-19.
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class DelayAutoCompleteTextView extends AutoCompleteTextView {

    private static final int AUTO_COMPLETE_DELAY = 2000; //Delay in milliseconds

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DelayAutoCompleteTextView.super.performFiltering((CharSequence)msg.obj, msg.arg1);
        }
    };

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        mHandler.removeMessages(0);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(0,  keyCode, 0, text), AUTO_COMPLETE_DELAY);
    }

}
