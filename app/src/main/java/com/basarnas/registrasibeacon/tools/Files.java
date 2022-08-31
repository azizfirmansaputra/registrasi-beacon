package com.basarnas.registrasibeacon.tools;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Files {

    public static String getFilePath(Context context, Uri uri){
        if(DocumentsContract.isDocumentUri(context, uri)){
            if(isMediaDocuments(uri)){
                String documentID       = DocumentsContract.getDocumentId(uri);
                String[] docSplit       = documentID.split(":");
                String[] selectionArgs  = new String[]{docSplit[1]};
                String documentType     = docSplit[0];
                String selection        = "_id=?";
                Uri contentUri          = null;

                switch(documentType){
                    case "image":
                        contentUri  = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        contentUri  = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        contentUri  = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                }

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
            else if(isDownloadsDocuments(uri)){
                return getFileTemp(context, uri);
            }
            else if(isExternalStorageDocuments(uri)){
                String documentID   = DocumentsContract.getDocumentId(uri);
                String[] docSplit   = documentID.split(":");
                String documentType = docSplit[0];

                if(documentType.equalsIgnoreCase("primary")){
                    return getFileTemp(context, uri);
                }
                else{
                    return "/storage/" + documentID.replace(":", "/");
                }
            }
            else if(isGoogleDriveDocsStorage(uri)){
                return getFileTemp(context, uri);
            }
        }
        else if(uri.getScheme().equalsIgnoreCase("content")){
            if(isGooglePhotosContent(uri)){
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        else if(uri.getScheme().equalsIgnoreCase("file")){
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs){
        Cursor cursor       = null;
        String column       = "_data";
        String[] projection = {column};

        try{
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

            if(cursor != null && cursor.moveToFirst()){
                return cursor.getString(cursor.getColumnIndexOrThrow(column));
            }
        }
        finally{
            if(cursor != null){
                cursor.close();
            }
        }

        return null;
    }

    public static String getFileTemp(Context context, Uri uri){
        String name = getFileName(context, uri);
        File file   = new File(context.getCacheDir(), name + "");

        try{
            InputStream inputStream     = context.getContentResolver().openInputStream(uri);
            OutputStream outputStream   = new FileOutputStream(file);
            byte[] bufferStream         = new byte[Math.min(inputStream.available(), 1024 * 1024)];
            int readFileStream;

            while((readFileStream = inputStream.read(bufferStream)) != -1){
                outputStream.write(bufferStream, 0, readFileStream);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            return file.getPath();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }

    public static String getFileName(Context context, Uri uri){
        Cursor cursor   = context.getContentResolver().query(uri, null, null, null, null, null);
        int indexName   = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

        try{
            if(cursor.moveToFirst()){
                return cursor.getString(indexName);
            }
        }
        finally{
            cursor.close();
        }

        return null;
    }

    @SuppressWarnings("unused")public static String getDuration(Context context, Uri uri){
        MediaMetadataRetriever MMR = new MediaMetadataRetriever();

        MMR.setDataSource(context, uri);

        String extractTimes = MMR.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long durationMS     = Long.parseLong(extractTimes);

        MMR.release();

        long hours      = TimeUnit.MILLISECONDS.toHours(durationMS);
        long minutes    = TimeUnit.MILLISECONDS.toMinutes(durationMS) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(durationMS));
        long seconds    = TimeUnit.MILLISECONDS.toSeconds(durationMS) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durationMS));

        if(hours > 0){
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        }
        else{
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }

    public static boolean isMediaDocuments(Uri uri){
        return uri.getAuthority().equals("com.android.providers.media.documents");
    }

    public static boolean isDownloadsDocuments(Uri uri){
        return uri.getAuthority().equals("com.android.providers.downloads.documents");
    }

    public static boolean isExternalStorageDocuments(Uri uri){
        return uri.getAuthority().equals("com.android.externalstorage.documents");
    }

    public static boolean isGoogleDriveDocsStorage(Uri uri){
        return uri.getAuthority().equals("com.google.android.apps.docs.storage");
    }

    public static boolean isGooglePhotosContent(Uri uri){
        return uri.getAuthority().equals("com.google.android.apps.photos.content");
    }
}