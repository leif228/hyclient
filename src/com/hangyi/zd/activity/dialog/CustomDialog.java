package com.hangyi.zd.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import com.hy.client.R;

public class CustomDialog extends Dialog
{

    private boolean mCanceledOnTouchOutside = true;
    private boolean mIsMenu;


    protected CustomDialog(Context context, int theme)
    {
        super(context, theme);
    }


    protected CustomDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);
    }


    protected CustomDialog(Context context)
    {
        this(context, R.style.custom_dialog);
    }


    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);

    }


    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    /**
     * 捕获系统菜单键响应事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_MENU && mIsMenu)
        {
            cancel();
        }
        return super.onKeyDown(keyCode, event);
    }


    public boolean onTouchEvent(MotionEvent event)
    {
        if (mCanceledOnTouchOutside
            && event.getAction() == MotionEvent.ACTION_DOWN
            && isOutOfBounds(event))
        {
            cancel();
            return true;
        }

        return false;
    }


    private boolean isOutOfBounds(MotionEvent event)
    {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final View contentView = findViewById(R.id.content);
        return (x < contentView.getLeft()) || (y < contentView.getTop())
               || (x > contentView.getRight()) || (y > contentView.getBottom());
    }

    public static class Builder
    {

        private CustomDialog mDialog;
        private LinearLayout mContent;
        private View customView;


        public Builder(Context context)
        {
            mDialog = new CustomDialog(context);
            mDialog.setContentView(R.layout.custom_dialog);
            LayoutParams params = mDialog.getWindow().getAttributes();
            params.width = LayoutParams.FILL_PARENT;
            params.height = LayoutParams.FILL_PARENT;
            mDialog.getWindow().setAttributes(params);
            mContent = (LinearLayout) mDialog.findViewById(R.id.content);
            mDialog.setCanceledOnTouchOutside(true);
        }


        public void setOnCancelListener(OnCancelListener listener)
        {
            mDialog.setOnCancelListener(listener);
        }


        public void setCanceledOnTouchOutside(boolean flag)
        {
            mDialog.mCanceledOnTouchOutside = flag;
        }


        public void setOnDismissListener(OnDismissListener listener)
        {
            mDialog.setOnDismissListener(listener);
        }


        public void dismiss()
        {
            mDialog.dismiss();
        }


        public void show()
        {
            if (customView != null)
            {
                mContent.addView(customView);
            }
            mDialog.show();
        }


        public void setView(View view)
        {
            customView = view;
        }


        public void setIsMenu(boolean isMenu)
        {
            mDialog.mIsMenu = isMenu;
        }

    }

}
