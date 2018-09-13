package com.ala.populermovies.utilities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;

import com.ala.populermovies.R;

public final class AppUtils {
    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.youtube_url_vnd) + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format(context.getString(R.string.youtube_url_web), id)));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public static void shareText(Activity activity, String text, String title) {
        String mimeType = activity.getString(R.string.mime_type_text);
        Intent shareIntent = ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(text)
                .getIntent();
        if (shareIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(shareIntent);
        }
    }
}
