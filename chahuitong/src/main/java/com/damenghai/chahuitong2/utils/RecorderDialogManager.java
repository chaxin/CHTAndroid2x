package com.damenghai.chahuitong2.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;

/**
 * Created by Sgun on 15/10/27.
 */
public class RecorderDialogManager {
    private Context mContext;

    private Dialog mDialog;

    private ImageView mVoice;
    private TextView mLabel;

    private Drawable[] mDrawableLevel;

    public RecorderDialogManager(Context context) {
        mContext = context;
    }

    public void showRecordingDialog() {
        mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
        View view = View.inflate(mContext, R.layout.dialog_recorder, null);
        mDialog.setContentView(view);

        mVoice = (ImageView) mDialog.findViewById(R.id.iv_record_level);
        mLabel = (TextView) mDialog.findViewById(R.id.tv_voice_tips);

        mDialog.show();

        initVoiceLevelRes();
    }

    public void recording() {
        if(mDialog != null && mDialog.isShowing()) {
            mVoice.setImageResource(R.drawable.chat_icon_voice1);
            mLabel.setText(R.string.recorder_recording);
        }
    }

    public void wantToCancel() {
        if(mDialog != null && mDialog.isShowing()) {
            mVoice.setImageResource(R.drawable.chat_icon_cancel);
            mLabel.setText(R.string.recorder_want_to_cancel);
        }
    }

    public void tooShort() {
        if(mDialog != null && mDialog.isShowing()) {
            mVoice.setImageResource(R.drawable.chat_voice_to_short);
            mLabel.setText(R.string.recorder_short_tips);
        }
    }

    public void dismissDialog() {
        if(mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void updateVoiceLevel(int level) {
        if(mDialog != null && mDialog.isShowing()) {
            mVoice.setImageDrawable(mDrawableLevel[level]);
        }
    }

    private void initVoiceLevelRes() {
        mDrawableLevel = new Drawable[] {
                mContext.getResources().getDrawable(R.drawable.chat_icon_voice2),
                mContext.getResources().getDrawable(R.drawable.chat_icon_voice3),
                mContext.getResources().getDrawable(R.drawable.chat_icon_voice4),
                mContext.getResources().getDrawable(R.drawable.chat_icon_voice5),
                mContext.getResources().getDrawable(R.drawable.chat_icon_voice6)
        };
    }
}
