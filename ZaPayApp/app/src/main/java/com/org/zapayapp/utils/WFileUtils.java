package com.org.zapayapp.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.net.Uri.fromFile;

public class WFileUtils {

    /**
     * The constant AUTHORITY.
     */
// TODO : Please change the file provider
    private static final String AUTHORITY = "com.bookiebot.fileprovider";

    /**
     * The constant DEBUG.
     */
    private static final boolean DEBUG = false; // Set to true to enable logging

    /**
     * The constant IMAGE_DIR.
     */
    private static final String IMAGE_DIR = "/Images/";
    /**
     * The constant VIDEO_DIR.
     */
    private static final String VIDEO_DIR = "/Videos/";
    /**
     * The constant BACKUP_DIR.
     */
    private static final String BACKUP_DIR = "/Backup/";
    /**
     * The constant OTHERS_DIR.
     */
    private static final String OTHERS_DIR = "/Others/";
    /**
     * The constant DOWNLOAD_DIR.
     */
    private static final String DOWNLOAD_DIR = "/Downloads/";
    private static final String DOC_DIR = "/Document/";
    /**
     * The constant TEMP_DIR.
     */
    private static final String TEMP_DIR = "/Temp/";
    /**
     * The constant TAG.
     */
    private static final String TAG = "WFileUtils";

    /**
     * The constant image.
     */
    public static int image = 1;
    /**
     * The constant video.
     */
    public static int video = 2;
    /**
     * The constant APP_NAME.
     */
    private static String APP_NAME = "BookieBot";
    /**
     * The constant DIR_IMAGE.
     */
    public static final String DIR_IMAGE = Environment.getExternalStorageDirectory() + File.separator + APP_NAME + IMAGE_DIR;
    /**
     * The constant DIR_VIDEO.
     */
    public static final String DIR_VIDEO = Environment.getExternalStorageDirectory() + File.separator + APP_NAME + VIDEO_DIR;
    /**
     * The constant DIR_TEMP.
     */
    public static final String DIR_TEMP = Environment.getExternalStorageDirectory() + File.separator + APP_NAME + TEMP_DIR;
    /**
     * The constant DIR_BACKUP.
     */
    public static final String DIR_BACKUP = Environment.getExternalStorageDirectory() + File.separator + APP_NAME + BACKUP_DIR;
    /**
     * The constant DIR_OTHERS.
     */
    public static final String DIR_OTHERS = Environment.getExternalStorageDirectory() + File.separator + APP_NAME + OTHERS_DIR;
    /**
     * The constant DIR_Downloads.
     */
    public static final String DIR_DOWNLOADS = Environment.getExternalStorageDirectory() + File.separator + APP_NAME + DOWNLOAD_DIR;
    /* The constant DIR_DOC.
     */
    public static final String DIR_DOC = Environment.getExternalStorageDirectory() + File.separator + APP_NAME + DOC_DIR;

    /**
     * Instantiates a new W file utils.
     */
    public WFileUtils() {
    }

    /**
     * Create application folder.This folder is to initialise in the application oncreate method
     *
     * @param app_name the app name
     */
    public static void createApplicationFolder(String app_name) {

        File f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_NAME);
        boolean app = f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_NAME + VIDEO_DIR);
        boolean video = f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_NAME + IMAGE_DIR);
        boolean image = f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_NAME + TEMP_DIR);
        boolean temp = f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_NAME + BACKUP_DIR);
        boolean backup = f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_NAME + DOWNLOAD_DIR);
        boolean download = f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_NAME + DOC_DIR);
        boolean doc = f.mkdirs();
    }

    /**
     * Create application folder.This folder is to initialise in the application oncreate method
     *
     * @param context the Application context
     */
    public static void createApplicationFolderQ(Context context) {

        File[] externalStorageVolumes =
                ContextCompat.getExternalFilesDirs(context, null);
        File primaryExternalStorage = externalStorageVolumes[0];

        File f = new File(primaryExternalStorage, VIDEO_DIR);
        boolean video = f.mkdirs();
        f = new File(primaryExternalStorage, IMAGE_DIR);
        boolean image = f.mkdirs();
        f = new File(primaryExternalStorage, DOC_DIR);
        boolean temp = f.mkdirs();
        f = new File(primaryExternalStorage, BACKUP_DIR);
        boolean backup = f.mkdirs();
        f = new File(primaryExternalStorage, OTHERS_DIR);
        boolean download = f.mkdirs();
    }

    /**
     * Check if sd-card is present.
     * Use Permission - <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     *
     * @return the boolean
     */
    public static boolean isSdPresent() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    /**
     * Gets actual file in the new versions of android.
     *
     * @param context the context
     * @param uri     the uri
     * @return the file path
     * @throws URISyntaxException the uri syntax exception
     */
    @SuppressLint("NewApi")
    public static String getFilePathOld(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];

            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    try {
                        uri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        // return getDataColumn(context, contentUri, null, null);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Open file.
     *
     * @param context the context
     * @param url     the url
     * @throws IOException the io exception
     */
    public static void openFile(Context context, File url) throws IOException {
        // Create URI
        File file = url;
        Uri uri;
        if (Build.VERSION.SDK_INT >= 23) {
            uri = FileProvider.getUriForFile(context, "com.bookiebot" + ".fileprovider", file);
        } else {
            uri = fromFile(file);
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }


    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~ Updated on Thu March 2, 2019 ~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    public static boolean checkFolderExists(Context context) {

        File[] externalStorageVolumes =
                ContextCompat.getExternalFilesDirs(context, null);
        File primaryExternalStorage = externalStorageVolumes[0];

        File dir = new File(primaryExternalStorage, DOC_DIR);
        File dir_img = new File(primaryExternalStorage, IMAGE_DIR);
        File dir_vid = new File(primaryExternalStorage, VIDEO_DIR);
        File dir_other = new File(primaryExternalStorage, OTHERS_DIR);
        File dir_backup = new File(primaryExternalStorage, BACKUP_DIR);

        boolean isDirectoryCreated = false;
        if (dir_img.exists())
            isDirectoryCreated = true;

        if (!isDirectoryCreated) {
            return true;
        }
        return false;
    }


    /**
     * Directory exist boolean.
     *
     * @param APP_NAME the app name
     * @return the boolean
     */
    public static boolean directoryExist(String APP_NAME) {

        File f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_NAME);
        File f_images = new File(Environment.getExternalStorageDirectory(), File.separator + APP_NAME + IMAGE_DIR);

        // do something here
        return (f.exists() && f.isDirectory() && f_images.exists());
    }


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @return the file path
     * @see #getFile(Context, Uri) #getFile(Context, Uri)#getFile(Context, Uri)
     */
    public static String getFilePath(final Context context, final Uri uri) {

        if (DEBUG)
            Log.d(TAG + " File -",
                    "Authority: " + uri.getAuthority() +
                            ", Fragment: " + uri.getFragment() +
                            ", Port: " + uri.getPort() +
                            ", Query: " + uri.getQuery() +
                            ", Scheme: " + uri.getScheme() +
                            ", Host: " + uri.getHost() +
                            ", Segments: " + uri.getPathSegments().toString()
            );

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider
            if (isLocalStorageDocument(uri)) {
                // The path is the id
                return DocumentsContract.getDocumentId(uri);
            }
            // ExternalStorageProvider
            else if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    try {
                        String path = getDataColumn(context, contentUri, null, null);

                        if (path != null && path.length() > 0) {
                            return path;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                String fileName = getFileName(context, uri);
                File cacheDir = getDocumentCacheDir(context);
                File file = generateFileName(fileName, cacheDir);
                String destinationPath = null;
                if (file != null) {
                    destinationPath = file.getAbsolutePath();
                    saveFileFromUri(context, uri, destinationPath);
                }

                return destinationPath;
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Is local boolean.
     *
     * @param url the url
     * @return Whether the URI is a local one.
     */
    public static boolean isLocal(String url) {
        return url != null && !url.startsWith("http://") && !url.startsWith("https://");
    }

    /**
     * Is media uri boolean.
     *
     * @param uri the uri
     * @return True if Uri is a MediaStore Uri.
     */
    public static boolean isMediaUri(Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * Convert File into Uri.
     *
     * @param file the file
     * @return uri uri
     */
    public static Uri getUri(File file) {
        return (file != null) ? fromFile(file) : null;
    }

    /**
     * Returns the path only (without file name).
     *
     * @param file the file
     * @return path without filename
     */
    public static File getPathWithoutFilename(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                // no file to be split off. Return everything
                return file;
            } else {
                String filename = file.getName();
                String filepath = file.getAbsolutePath();

                // Construct path without file name.
                String pathwithoutname = filepath.substring(0,
                        filepath.length() - filename.length());
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length() - 1);
                }
                return new File(pathwithoutname);
            }
        }
        return null;
    }

    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * Gets mime type.
     *
     * @param file the file
     * @return The MIME type for the given file.
     */
    public static String getMimeType(File file) {

        String extension = getExtension(file.getName());

        if (extension.length() > 0)
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));

        return "application/octet-stream";
    }

    /**
     * Gets mime type.
     *
     * @param context the context
     * @param uri     the uri
     * @return The MIME type for the give Uri.
     */
    public static String getMimeType(Context context, Uri uri) {
        File file = new File(getFilePath(context, uri));
        return getMimeType(file);
    }

    /**
     * Is local storage document boolean.
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is local.
     */
    public static boolean isLocalStorageDocument(Uri uri) {
        return AUTHORITY.equals(uri.getAuthority());
    }

    /**
     * Is external storage document boolean.
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * Is downloads document boolean.
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * Is media document boolean.
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Is google photos uri boolean.
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.MediaColumns.DISPLAY_NAME;
        final String[] projection = {
                column
        };

        /*String path = "";
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DISPLAY_NAME}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String fileName = cursor.getString(0);
                path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                if (!TextUtils.isEmpty(path)) {
                    return path;
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }*/

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                if (DEBUG)
                    DatabaseUtils.dumpCursor(cursor);

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(0);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * Convert Uri into File, if possible.
     *
     * @param context the context
     * @param uri     the uri
     * @return file A local file that the Uri was pointing to, or null if the Uri is unsupported or pointed to a remote resource.
     * @see #getFilePath(Context, Uri) #getFilePath(Context, Uri)#getFilePath(Context, Uri)
     */
    public static File getFile(Context context, Uri uri) {
        if (uri != null) {
            String path = getFilePath(context, uri);
            if (path != null && isLocal(path)) {
                return new File(path);
            }
        }
        return null;
    }

    /**
     * Gets file name.
     *
     * @param context the context
     * @param uri     the uri
     * @return the file name
     */
    public static String getFileName(@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null && context != null) {
            String path = getFilePath(context, uri);
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null,
                    null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }
        return filename;
    }

    /**
     * Gets name.
     *
     * @param filename the filename
     * @return the name
     */
    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri the uri
     * @return Extension including the dot("."); "" if there is no extension; null if uri was null.
     */
    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    /**
     * Gets document cache dir.
     *
     * @param context the context
     * @return the document cache dir
     */
    public static File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), IMAGE_DIR.replaceAll("/", ""));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        logDir(context.getCacheDir());
        logDir(dir);

        return dir;
    }

    private static void logDir(File dir) {
        if (!DEBUG) return;
        Log.d(TAG, "Dir=" + dir);
        File[] files = dir.listFiles();
        for (File file : files) {
            Log.d(TAG, "File=" + file.getPath());
        }
    }

    /**
     * Generate file name file.
     *
     * @param name      the name
     * @param directory the directory
     * @return the file
     */
    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            Log.w(TAG, e);
            return null;
        }

        logDir(directory);

        return file;
    }

    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates View intent for given file
     *
     * @param context the context
     * @param file    the file
     * @return The intent for viewing file
     */
    public static Intent getViewIntent(Context context, File file) {
        //Uri uri = Uri.fromFile(file);
        Uri uri = FileProvider.getUriForFile(context, AUTHORITY, file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = file.toString();
        if (url.contains(".doc") || url.contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.contains(".ppt") || url.contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.contains(".xls") || url.contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.contains(".zip") || url.contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.contains(".wav") || url.contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.contains(".3gp") || url.contains(".mpg") || url.contains(".mpeg") ||
                url.contains(".mpe") || url.contains(".mp4") || url.contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    /**
     * Gets file name.
     *
     * @param uri the uri
     * @return the file name
     */
    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    /**
     * Gets file path from uri.
     *
     * @param context    the context
     * @param contentUri the content uri
     * @return the file path from uri
     */
    public static String getFilePathFromURI(Context context, Uri contentUri) {

        File[] externalStorageVolumes =
                ContextCompat.getExternalFilesDirs(context, null);
        File primaryExternalStorage = externalStorageVolumes[0];
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            String flePath = getFilePath(context, contentUri);
            File copyFile = new File(WFileUtils.DIR_TEMP + File.separator + fileName);

            if (Build.VERSION.SDK_INT >= 29) {
                copyFile = new File(primaryExternalStorage + IMAGE_DIR + File.separator + fileName);
            } else {
                copyFile = new File(WFileUtils.DIR_TEMP + File.separator + fileName);
            }
            //  File copyFile = saveTempFile(fileName, context, contentUri);
            //  Uri uri = Uri.parse(copyFile.getAbsolutePath());
            copy(context, contentUri, copyFile);

            CommonMethods.showLogs(TAG, "copyFile.getAbsolutePath() for Api 29 :- " + copyFile.getAbsolutePath());
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    /**
     * Copy.
     *
     * @param context the context
     * @param srcUri  the src uri
     * @param dstFile the dst file
     */
    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~ Updated on Thu March 2, 2019 ~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    /**
     * Gets output image file uri.
     * Use Permission - <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     *
     * @return the output image file uri
     */
    public Uri getOutputImageFileUri() {
        WFileUtils WFileUtils = new WFileUtils();
        return fromFile(WFileUtils.getOutputImageMediaFile());
    }

    private File getOutputImageMediaFile() {

        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), File.separator + APP_NAME + IMAGE_DIR);
        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(APP_NAME, "Failed to create directory.");
                return null;
            }
        }
        // Create a media file name

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg");
        return mediaFile;
    }

    /**
     * Gets output video file uri.
     * Use Permission - <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     *
     * @return the output video file uri
     */
    public Uri getOutputVideoFileUri() {
        WFileUtils WFileUtils = new WFileUtils();
        return fromFile(WFileUtils.getOutputVideoMediaFile());
    }

    private File getOutputVideoMediaFile() {
        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), File.separator + APP_NAME + VIDEO_DIR);
        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(APP_NAME, "Failed to create directory.");
                return null;
            }
        }
        // Create a media file name
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + System.currentTimeMillis() + ".mp4");
        return mediaFile;
    }

    /**
     * Save temp file.
     * <p/>
     * Use Permission - <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     *
     * @param context the context
     * @param uri     the uri
     * @return the file
     */
    public static File saveTempFile(Context context, Uri uri) {

        File mFile = null;
        ContentResolver resolver = context.getContentResolver();
        InputStream in = null;
        FileOutputStream out = null;

        try {
            in = resolver.openInputStream(uri);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "Images_" + timeStamp + ".jpg";
            mFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + APP_NAME + TEMP_DIR, fileName);
            out = new FileOutputStream(mFile, false);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
        } catch (IOException e) {
            Log.d(TAG, "" + e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.d(TAG, "" + e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Log.d(TAG, "" + e);
                }
            }
        }
        return mFile;
    }

    /**
     * Store image if bitmap is returned.
     *
     * @param imageData the image data
     * @return the string
     */
    public static String storeImage(Bitmap imageData) {
        //get path to external storage (SD card)
        String path = "";
        File f = null;
        try {
            f = null;
            String iconsStoragePath = Environment.getExternalStorageDirectory() + File.separator + APP_NAME + IMAGE_DIR;
            File sdIconStorageDir = new File(iconsStoragePath);

            if (!sdIconStorageDir.exists())
                sdIconStorageDir.mkdirs();

            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String filename = "Images_" + timeStamp + ".jpg";
                String filePath = iconsStoragePath + filename;
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
                //choose another format if PNG doesn't suit you
                imageData.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bos.flush();
                bos.close();

                f = new File(filePath);

            } catch (FileNotFoundException e) {
//                Log.w("TAG", "Error saving image file: " + e.getMessage());
                return "";
            } catch (IOException e) {
//                Log.w("TAG", "Error saving image file: " + e.getMessage());
                return "";
            } catch (Exception e) {
//                Log.w("TAG", "Error saving image file: " + e.getMessage());
                return "";
            }
            path = f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * Save an image in sd-card.
     * add gradle file for this  --- compile 'org.apache.directory.studio:org.apache.commons.io:2.4'
     * Use Permission - <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     *
     * @param picturePath the picture path
     * @return the string
     */
    public String saveInSd(String picturePath) {
        try {
            String iconsStoragePath = Environment.getExternalStorageDirectory() + File.separator + APP_NAME + IMAGE_DIR;
            File sdIconStorageDir = new File(iconsStoragePath);

            //create storage directories, if they don't exist
            if (!sdIconStorageDir.exists())
                sdIconStorageDir.mkdirs();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String filename = "Images_" + timeStamp + ".jpg";

            File source = new File(picturePath);

            String destinationPath = iconsStoragePath + filename;
            File destination = new File(destinationPath);
            try {
                if (source.exists()) {
                    FileUtils.copyFile(source, destination);
                    if (destination.exists()) {
                        return destination.getAbsolutePath();
                    } else {
                        return picturePath;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return picturePath;
    }

    /**
     * Save back up in sd card.
     *
     * @param context the context
     * @param key     the key
     * @throws IOException the io exception
     */
    public void saveBackUpInSdCard(Context context, String key) throws IOException {
        if (isSdPresent()) {
            InputStream in = null;
            OutputStream out = null;
            File src = new File(context.getFilesDir() + "/" + key);
            in = new FileInputStream(src);
            out = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + APP_NAME + BACKUP_DIR + key);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } else {
            CommonMethods.showToast(context, "SD Card not present");
        }
    }

    /**
     * Store image if bitmap is returned.
     *
     * @param filename  the filename
     * @param imageData the image data
     * @return the string
     */
    public String storeTempThumbnail(String filename, Bitmap imageData) {
        //get path to external storage (SD card)
        String path = "";
        File f = null;
        try {
            f = null;
            String iconsStoragePath = DIR_TEMP;
            File sdIconStorageDir = new File(iconsStoragePath);

            if (!sdIconStorageDir.exists())
                sdIconStorageDir.mkdirs();

            try {
                String filePath = iconsStoragePath + filename;
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
                //choose another format if PNG doesn't suit you
                imageData.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bos.flush();
                bos.close();

                f = new File(filePath);

            } catch (FileNotFoundException e) {
                Log.w("TAG", "Error saving image file: " + e.getMessage());
                return "";
            } catch (IOException e) {
                Log.w("TAG", "Error saving image file: " + e.getMessage());
                return "";
            } catch (Exception e) {
                Log.w("TAG", "Error saving image file: " + e.getMessage());
                return "";
            }
            path = f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * Add jpg signature to gallery string.
     *
     * @param context   the context
     * @param signature the signature
     * @return the string
     */
    public String addJpgSignatureToGallery(Context context, Bitmap signature) {
        String signpath = "";
        try {
            String iconsStoragePath = Environment.getExternalStorageDirectory() + File.separator + APP_NAME + IMAGE_DIR;
            File sdIconStorageDir = new File(iconsStoragePath);

            //create storage directories, if they don't exist
            if (!sdIconStorageDir.exists())
                sdIconStorageDir.mkdirs();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String filename = iconsStoragePath + "Signature_" + timeStamp + ".jpg";

            File source = new File(filename);
            signpath = source.getAbsolutePath();
            WFileUtils WFileUtils = new WFileUtils();
            WFileUtils.saveBitmapToJPG(signature, source);
            WFileUtils.scanMediaFile(context, source);
            //result = true;
        } catch (IOException e) {
            e.printStackTrace();
            signpath = "";
        }
        return signpath;
    }

    private void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    private void scanMediaFile(Context context, File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = fromFile(photo);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * File time string.
     *
     * @param filePath the file path
     * @return the string
     */
    public String FileTime(String filePath) {
        String time = null;
        try {
            File file = new File(filePath);
            Date lastModDate = new Date(file.lastModified());
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            CommonMethods.showLogs("File last modified @ : ", "" + lastModDate);
            CommonMethods.showLogs("File last modified @ : ", "" + formatter.format(file.lastModified()));

            Date date = formatter.parse(formatter.format(file.lastModified()));
            CommonMethods.showLogs("Today is ", "" + date.getTime());
            time = String.valueOf(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * Remove older files from particular folder when files are older than 24 hours
     *
     * @param folderPath the folder path
     */
    public void removeOlderFiles(String folderPath) {
        CommonMethods.showLogs("Files", "Path: " + folderPath);
        File directory = new File(folderPath);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            CommonMethods.showLogs("Files", "FileName:" + files[i].getName());
            CommonMethods.showLogs("Files", "file time:" + files[i].lastModified());


            // get time of file and thn convert it into time stamp
            // after that check it is older than 24 hr from current time
            //if it is older than 24hr thn delete it
            // File file = new File(files[i].getName());
            Date lastModDate = new Date(files[i].lastModified());
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            CommonMethods.showLogs("File last modified @ : ", "" + lastModDate);
            CommonMethods.showLogs("File last modified @ : ", "" + formatter.format(files[i].lastModified()));
            try {
                Date date = formatter.parse(formatter.format(files[i].lastModified()));
                CommonMethods.showLogs("file time in timestamp ", "" + date.getTime());
                long l = System.currentTimeMillis();
                long isOlderFile = l - (24 * 60 * 1000);
                CommonMethods.showLogs("isOlderFile difference", "" + isOlderFile);
                // check file is older than 24hr if yes thn delete it
                if (date.getTime() < isOlderFile) {
                    boolean deleted = files[i].delete();
                    CommonMethods.showLogs("is file deleted ", "" + deleted);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create copy of any file to our folder location.
     *
     * @param sourcePath the source path
     * @param filename   the filename
     * @param type       the type
     * @return the string
     */
    public String createCopy(String sourcePath, String filename, int type) {
        try {
            File src = new File(sourcePath);
            //create storage directories, if they don't exist
            if (!src.exists())
                src.mkdirs();
            String destinationPath = "";
            if (type == WFileUtils.image) {
                destinationPath = DIR_IMAGE + filename;
            } else if (type == WFileUtils.video) {
                destinationPath = DIR_VIDEO + filename;
            } else {
                destinationPath = DIR_OTHERS + filename;
            }
            File fdest = new File(destinationPath);
            try {
                if (src.exists()) {
                    FileUtils.copyFile(src, fdest);
                    if (fdest.exists()) {
                        String path = fdest.getAbsolutePath();
                        File newFile = new File(path);
                        return newFile.getAbsolutePath();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Save video from uri to temporary path.
     *
     * @param sourceUri the sourceUri
     * @return the string
     */
    public static String saveVideo(Context context, Uri sourceUri) {
        String sourceFilename = sourceUri.getPath();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "Video_" + timeStamp + ".mp4";
        String destinationFilename = DIR_TEMP + fileName;

        //BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream in = null;

        try {
            in = context.getContentResolver().openInputStream(sourceUri);
            //bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            in.read(buf);
            do {
                bos.write(buf);
            } while (in.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return destinationFilename;
    }

    /**
     * Save video from uri to temporary path.
     *
     * @param sourceUri the sourceUri
     * @return the string
     */
    public static String saveAudio(Context context, Uri sourceUri) {
        String sourceFilename = sourceUri.getPath();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "Audio_" + timeStamp + ".mp3";
        String destinationFilename = DIR_TEMP + fileName;

        //BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream in = null;

        try {
            in = context.getContentResolver().openInputStream(sourceUri);
            //bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            in.read(buf);
            do {
                bos.write(buf);
            } while (in.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return destinationFilename;
    }

    /**
     * Gets type(extension) of the file retrieve from gallery.
     *
     * @param file the url
     * @return the type
     */
    public String getFileType(File file) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
        String type = "";
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (type != null) {
                return type;
            }
        }
        return type;
    }

    /**
     * Delete temp file boolean from the existing file path.
     *
     * @param path the path
     * @return the boolean
     */
    public boolean deleteTempFile(String path) {
        File file = new File(path);
        return file.exists() && file.delete();
    }

    /**
     * If file exist boolean.
     *
     * @param mediaPath the media path
     * @return the boolean
     */
    public boolean ifFileExist(String mediaPath) {
        File file = new File(mediaPath);
        return file.exists();
    }

    /**
     * Gets file duration in long.
     *
     * @param context the context
     * @param pathStr the path str
     * @return the file duration
     */
    public long getFileDuration(Context context, String pathStr) {
        Uri uri = Uri.parse(pathStr);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Long.parseLong(durationStr);
    }

    /**
     * Gets thumbnail from file for image or video.
     *
     * @param f    the f
     * @param type the type
     * @return the thumbnail from file
     */
    public File getThumbnailFromFile(File f, int type) {
        if (WFileUtils.image == type) {
            if (f.getAbsolutePath() != null) {
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(f.getAbsolutePath()), 200, 200);
                String filepath = storeTempThumbnail(f.getName(), ThumbImage);
                File thumb = new File(filepath);
                return thumb;
            }
        } else if (WFileUtils.video == type) {
            Bitmap ThumbImage = ThumbnailUtils.createVideoThumbnail(f.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
            String filepath = storeTempThumbnail(f.getName(), ThumbImage);
            File thumb = new File(filepath);
            return thumb;
        }
        return null;
    }

    /**
     * Rename file to new name.
     *
     * @param oldPath the old path
     * @param newPath the new path
     * @return the string
     */
    public String renameFile(String oldPath, String newPath) {
        File from = new File(oldPath);
        File to = new File(newPath);
        boolean isRename = false;
        if (from.exists()) {
            isRename = from.renameTo(to);
        }
        if (isRename) {
            return newPath;
        } else {
            return oldPath;
        }
    }

    /**
     * Get files from local folder file [ ].
     *
     * @param folderPath the folder path
     * @return the file [ ]
     */
    public File[] getFilesFromLocalFolder(String folderPath) {
        Log.d("Files", "Path: " + folderPath);
        File directory = new File(folderPath);
        File[] files = directory.listFiles();
        return files;
    }

    /**
     * Is file path boolean - to check if the path is actually a file path.
     *
     * @param imagePath the image path
     * @return the boolean
     */
    public boolean isFilePath(String imagePath) {
        if (imagePath != null && imagePath.length() > 0) {
            File file = new File(imagePath);
            if (!file.isDirectory()) {
                file = file.getParentFile();
                return file.exists();
            }
        }
        return false;
    }

    /**
     * Gets main image name from file.
     *
     * @param str the str
     * @return the main image name
     */
    public String getMainImageName(String str) {
        String newstr = "";
        newstr = str.substring(str.lastIndexOf("/") + 1);
        return newstr;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static long checkFileSizeInMB(String filePath) {
        File file = new File(filePath);
        // Get length of file in bytes
        long fileSizeInBytes = file.length();
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        return fileSizeInKB / 1024;
    }


    private static final String LOG_FOLDER_NAME = "log";
    private static final String LOG_FILE_NAME = "agora-rtc.log";

    /**
     * Initialize the log folder
     * @param context Context to find the accessible file folder
     * @return the absolute path of the log file
     */
    public static String initializeLogFile(Context context) {
        File folder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            folder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), LOG_FOLDER_NAME);
        } else {
            String path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator +
                    context.getPackageName() + File.separator +
                    LOG_FOLDER_NAME;
            folder = new File(path);
            if (!folder.exists() && !folder.mkdir()) folder = null;
        }

        if (folder != null && !folder.exists() && !folder.mkdir()) return "";
        else return new File(folder, LOG_FILE_NAME).getAbsolutePath();
    }

    //---------------------------------------------------------------------------------------//

    /*
     * Gets the file path of the given Uri.
     */
    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{ split[1] };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }



    //this code for compress Image ....
    public static String compressImage(String imageUri) {
        String filePath = imageUri;
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = getFilename2();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public static String getFilename2() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Hms/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }


    //this code use for capture image from camara then bitmap data convert into uri ....
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
