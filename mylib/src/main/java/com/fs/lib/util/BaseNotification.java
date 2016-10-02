package com.fs.lib.util;

import android.app.Notification;
import android.content.Context;


/**
 * Created by milton on 17/02/16.
 */
public class BaseNotification extends Notification.Builder {
    /**
     * Constructs a new Builder with the defaults:
     * <p/>
     * <p/>
     * <table>
     * <tr><th align=right>priority</th>
     * <td>{@link #PRIORITY_DEFAULT}</td></tr>
     * <tr><th align=right>when</th>
     * <td>now ({@link System#currentTimeMillis()})</td></tr>
     * <tr><th align=right>audio stream</th>
     * <td>{@link #STREAM_DEFAULT}</td></tr>
     * </table>
     *
     * @param context A {@link Context} that will be used by the Builder to construct the
     *                RemoteViews. The Context will not be held past the lifetime of this Builder
     *                object.
     */
    public BaseNotification(Context context,int smallIcon, int title,int content) {
        super(context);
        setSmallIcon(smallIcon);
        setContentTitle(context.getResources().getString(title));
        setContentText(context.getResources().getString(content));

    }
}
