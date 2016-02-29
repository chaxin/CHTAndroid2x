package com.damenghai.chahuitong2.utils;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.ui.activity.LeaderActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sgun on 15/8/31.
 */
public class StringUtils {

    public static SpannableString getSpannableContent(final Context context, TextView tv, String source) {
        String regexAt = "@[\u04ee-\u9fa5\\w]+";

        SpannableString spannableString = new SpannableString(source);

        Pattern pattern = Pattern.compile(regexAt);
        Matcher matcher = pattern.matcher(spannableString);

        if(matcher.find()) {
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            matcher.reset();
        }

        while (matcher.find()) {
            String strAt = matcher.group();

            if(strAt != null) {
                int start = matcher.start();
                HodorClickableSpan clickableSpan = new HodorClickableSpan(context) {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, LeaderActivity.class);
                        context.startActivity(intent);
                    }
                };
                spannableString.setSpan(clickableSpan, start, start + strAt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spannableString;
    }

    public static class HodorClickableSpan extends ClickableSpan {
        private Context mContext;

        public HodorClickableSpan(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(mContext.getResources().getColor(R.color.text_at_blue));
            ds.setUnderlineText(false);
        }
    }

}
