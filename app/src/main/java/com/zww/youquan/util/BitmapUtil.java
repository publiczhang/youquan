package com.zww.youquan.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * BitmapUtil
 * @author Administrator
 */
public class BitmapUtil {

    /**
     * 保存base64 图片
     *
     * @param context       context
     * @param base64DataStr base64DataStr
     * @return boolean
     */
    public static boolean savePicture(Context context, String base64DataStr) {
        // 1.去掉base64中的前缀
        String base64Str = base64DataStr.substring(base64DataStr.indexOf(",") + 1, base64DataStr.length());
        // 首先保存图片
        // "abc":图片保存的文件夹的名称
        File appDir = new File(Environment.getExternalStorageDirectory(), "meishuwa");
        if (!appDir.exists()) {
            boolean mkdir = appDir.mkdir();
        }
        // 2.拼接图片的后缀，根据自己公司的实际情况拼接，也可从base64中截取图片的格式。
        String imgName = System.currentTimeMillis() + ".png";
        File fileTest = new File(appDir, imgName);
        // 3. 解析保存图片
        byte[] data = Base64.decode(base64Str, Base64.DEFAULT);

        for (int i = 0; i < data.length; i++) {
            if (data[i] < 0) {
                //调整异常数据
                data[i] += 256;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(fileTest);
            os.write(data);
            os.flush();
            os.close();

            // 4. 其次通知系统刷新图库
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileTest)));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
