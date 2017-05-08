package com.yokara.testforkara;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.MediaController;

/**
 * Created by zip on 5/5/17.
 */

public class MyMediaController extends MediaController {
    public MyMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public MyMediaController(Context context) {
        super(context);
    }
}
