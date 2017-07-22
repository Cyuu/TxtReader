package com.scanbook.read;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.net.URISyntaxException;

/**
 * Created by Administrator on 2017/7/21.
 */

public class FileUtils {

    /**
     * 从Uri中获取文件路径<br/>
     * 1 判断协议是不是content://开头，有的话就用ContentResolver去query查询文件真实位置。<br/>
     * 2 判断协议是不是file://开头，如果是，那么uri.getPath()就是文件的真实路径。<br/>
     */
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it  Or Log it.
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

}
