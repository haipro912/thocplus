package com.vttm.mochaplus.feature.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by thanhnt72 on 09/04/2015.
 */
public class FileUtils {

    public static final String TAG = FileUtils.class.getSimpleName();

    public static int getDurationMediaFile(Context mContext, String filePath) {
        int duration = 0;
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            int tmp = MediaPlayer.create(mContext, Uri.fromFile(new File(filePath))).getDuration();
            duration = Math.round(tmp / 1000.0f);
            mediaPlayer.release();
            Log.i(TAG, " duration: " + duration);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return duration; //
    }

    public static float getSizeMediaFileInMB(String filePath) {
        float fileSize = 0L;
        File file = new File(filePath);
        if (file.isFile()) {
            fileSize = (float) (file.length() / (1024.0 * 1024.0));
        }
        return fileSize;
    }

    public static float getSizeInMbFromByte(long sizeInByte) {
        return (float) (sizeInByte / (1024.0 * 1024.0));
    }

    public static String getFileSize(long sizeInByte) {
        String strFileSizeFormat = "%.1f %s";
        if (sizeInByte < AppConstants.FILE.ONE_KILOBYTE) {
            return sizeInByte + " B";
        } else if (sizeInByte < AppConstants.FILE.ONE_MEGABYTE) {
            return String.format(strFileSizeFormat, (float) (sizeInByte / 1024.0), "kB");
        } else {
            return String.format(strFileSizeFormat, (float) (sizeInByte / (1024.0 * 1024.0)), "MB");
        }
    }

    public static String getVideoContentUri(Context context, String filePath) {
        Log.i(TAG, "filePath: " + filePath);
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Media._ID},
                MediaStore.Video.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Log.i(TAG, "id: " + id);
            cursor.close();
            return "content://media/external/video/media/" + id;
        }
        return null;
    }

    public static String getVideoResolution(Context context, String filePath) {
        Log.i(TAG, "filePath: " + filePath);
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Media.RESOLUTION},
                MediaStore.Video.Media.DATA + "=? ",
                new String[]{filePath},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            String res = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));

            Log.i(TAG, "id: " + res);
            return res;
        }
        return null;
    }

    public static String addNameFileDuplicate(String filePath, int number) {
        int pos = filePath.lastIndexOf(".");
        if (pos > 0) {
            String fileName = filePath.substring(0, pos);
            String extension = filePath.substring(pos, filePath.length());
            Log.i(TAG, "filePath: " + fileName + " extension: "
                    + extension);
            if (filePath.charAt(pos - 1) == ')') { // File co dang abc(n).mp4
                int posNumb = filePath.lastIndexOf("(");
                String numberFile = filePath.substring(posNumb + 1, pos - 1);
                Log.i(TAG, "numberFile: " + numberFile);
                try {
                    int numberToFileName = Integer.parseInt(numberFile);
                    Log.i(TAG, "number to file name: " + numberToFileName);
                    fileName = filePath.substring(0, posNumb) + "("
                            + String.valueOf(number) + ")";
                    Log.i(TAG, "fileName new: " + fileName);
                    return fileName + extension;
                } catch (NumberFormatException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            return fileName + "(" + String.valueOf(number) + ")" + extension;
        }
        return filePath + "(" + String.valueOf(number) + ")";
    }

    public static String getRealPathVideoFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null); //Since manageQuery is deprecated
        if (cursor != null && cursor.moveToFirst()) {
            try {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                return cursor.getString(column_index);
            } catch (Exception e) {
                return null;
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public static String getRealPathImageFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null); //Since manageQuery is deprecated
        if (cursor != null && cursor.moveToFirst()) {
            try {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(column_index);
            } catch (Exception e) {
                return null;
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public static String getExtensionFile(String filePath) {
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }

    public static String getNameFileFromURL(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    public static void deleteFile(Context context, String filePath) {
        try {
            if (!TextUtils.isEmpty(filePath)) {
                File file = new File(filePath);
                if (file != null) {
                    file.delete();
                    refreshGallery(context, file);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void refreshGallery(Context context, File file) {
       /* MediaScannerConnection.scanFile(
                context,
                filePahs,
                null, null);*/
        Uri uri = Uri.fromFile(file);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static void testZipLogFile() {
        String LOG_FOLDER_PATH = Environment.getExternalStorageDirectory().getPath() +
                File.separator + "Mocha" + File.separator + ".logs" + File.separator;
        File home = new File(LOG_FOLDER_PATH);
        File[] listFiles = home.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File file : listFiles) {
                Log.d(TAG, "zipFile: " + file.getAbsolutePath());
                File zipFile = new File(home, "zipFile" + System.currentTimeMillis() + ".zip");
                zipFile(file, zipFile);
            }
        }
    }

    public static void zipFile(File inputFile, File zipFile) {
        BufferedInputStream origin = null;
        ZipOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
            byte data[] = new byte[AppConstants.FILE.BUFFER_SIZE_DEFAULT];
            in = new FileInputStream(inputFile);
            origin = new BufferedInputStream(in, AppConstants.FILE.BUFFER_SIZE_DEFAULT);
            ZipEntry entry = new ZipEntry("11");
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, AppConstants.FILE.BUFFER_SIZE_DEFAULT)) != -1) {
                out.write(data, 0, count);
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            try {
                if (origin != null)
                    origin.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}