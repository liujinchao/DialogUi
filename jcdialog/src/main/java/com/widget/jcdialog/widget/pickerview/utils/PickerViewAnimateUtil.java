package com.widget.jcdialog.widget.pickerview.utils;

import android.view.Gravity;

import com.widget.jcdialog.R;


public class PickerViewAnimateUtil {
    private static final int INVALID = -1;
    /**
     * Get default animation resource when not defined by the user
     *
     * @param gravity       the gravity of the dialog
     * @param isInAnimation determine if is in or out animation. true when is is
     * @return the id of the animation resource
     */
    public static int getAnimationResource(int gravity, boolean isInAnimation) {
        switch (gravity) {
            case Gravity.BOTTOM:
                return isInAnimation ? R.anim.timepicker_anim_enter_bottom : R.anim.timepicker_anim_exit_bottom;
        }
        return INVALID;
    }
}
