package org.test.client.mcopclient;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
// import android.widget.ImageButton;

public class CustomImageButton extends androidx.appcompat.widget.AppCompatImageButton {  // android.support.v7.widget.AppCompatImageButton {
    public CustomImageButton(Context context) {
        super(context);
    }

    public CustomImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public CustomImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    public void setEnabled(boolean enabled) {
        if(this.isEnabled() != enabled) {
            this.setImageAlpha(enabled ? 0xFF : 0x3F);
        }
        super.setEnabled(enabled);
    }
}
