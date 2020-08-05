package com.org.zapayapp.webservices;

import android.content.Context;
import android.os.Environment;

import com.org.zapayapp.utils.WFileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FileCache {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" +
            ".SSS", Locale.getDefault());
    /**
     * The constant MAIN_DIRECOTRY.
     */
    public static String MAIN_DIRECOTRY = "";
    private File file, tmpFile, exportFile;
    private TmpName tmpName;

    /**
     * Instantiates a new File cache.
     *
     * @param appName the app name
     * @param context the context
     */
    public FileCache(String appName, Context context) {
        MAIN_DIRECOTRY = appName;
        file = getFile(appName);
        /*new File(Environment.getExternalStorageDirectory() + File.separator + MAIN_DIRECOTRY);
        if (!file.exists()) {
            file.mkdir();
        }
        tmpFile = new File(Environment.getExternalStorageDirectory() + File.separator + MAIN_DIRECOTRY +
                File.separator + "tmp");
        file=tmpFile;
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
        exportFile = new File(Environment.getExternalStorageDirectory() + File.separator + MAIN_DIRECOTRY +
                File.separator + "export");
        if (!exportFile.exists()) {
            exportFile.mkdir();
        }*/
    }

    /**
     * Delete all files boolean.
     *
     * @param directory the directory
     * @return the boolean
     */
    public static boolean deleteAllFiles(File directory) {
        if (null != directory && directory.isDirectory()) {
            String[] children = directory.list();
            int count = 0;
            for (int i = 0; i < children.length; i++) {
                File file = new File(directory, children[i]);
                if (file.exists()) {
                    file.delete();
                    count++;
                }
            }
            return children.length == count;
        }
        return false;
    }

    /**
     * Copy file.
     *
     * @param sourceLocation the source location
     * @param targetLocation the target location
     * @throws IOException the io exception
     */
    public static void copyFile(File sourceLocation, File targetLocation) throws IOException {
        InputStream in = new FileInputStream(sourceLocation);
        OutputStream out = new FileOutputStream(targetLocation);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Write file.
     *
     * @param file   the file
     * @param buffer the buffer
     */
    public static void writeFile(File file, byte[] buffer) {
        if (null != file && null != buffer) {
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Append to file boolean.
     *
     * @param file           the file
     * @param appendContents the append contents
     * @return the boolean
     */
    public static boolean appendToFile(File file, String appendContents) {
        boolean result = false;
        try {
            if (file != null) {
                file.createNewFile();
                Writer writer = new BufferedWriter(new FileWriter(file, true), 1024);
                writer.write(appendContents);
                writer.close();
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Read file string.
     *
     * @param file the file
     * @return the string
     */
    public static String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        if (null != file && file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append('\n');
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * Log.
     *
     * @param data     the data
     * @param trimTo1k the trim to 1 k
     */
    public static void log(String data, boolean trimTo1k) {
        if (null != data) {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + MAIN_DIRECOTRY);
            if (!file.exists()) {
                file.mkdir();
            }
            File logFile = new File(file, "Log.txt");
            data = trimTo1k ? data.substring(0, data.length() > 1024 ? 1024 : data.length()) : data;
            FileCache.appendToFile(logFile, SIMPLE_DATE_FORMAT.format(new Date()) + "=> " + data);
        }
    }

    /**
     * Gets parent.
     *
     * @return the parent
     */
    public File getParent() {
        return file;
    }

    /**
     * Gets file.
     *
     * @param name the name
     * @return the file
     */
    public File getFile(String name) {
        try {
            if (null != name) {
                return new File(WFileUtils.DIR_TEMP, name);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Gets export file.
     *
     * @param name the name
     * @return the export file
     */
    public File getExportFile(String name) {
        if (null != name) {
            return new File(exportFile, name);
        }
        return null;
    }

    /**
     * Clear tmp dir boolean.
     *
     * @return the boolean
     */
    public boolean clearTmpDir() {
        return deleteAllFiles(tmpFile);
    }

    /**
     * Gets next tmp file.
     *
     * @param extension the extension
     * @return the next tmp file
     */
    public File getNextTmpFile(String extension) {
        if (null == tmpName) {
            tmpName = new TmpName();
        }
        return new File(tmpFile, tmpName.getNextName() + "." + extension);
    }

    /**
     * Gets curr tmp file.
     *
     * @param extension the extension
     * @return the curr tmp file
     */
    public File getCurrTmpFile(String extension) {
        if (null == tmpName) {
            tmpName = new TmpName();
        }
        String name = tmpName.getCurrName();
        return null == name ? null : new File(tmpFile, name + "." + extension);
    }

    /**
     * Gets prev tmp file.
     *
     * @param extension the extension
     * @return the prev tmp file
     */
    public File getPrevTmpFile(String extension) {
        if (null == tmpName) {
            tmpName = new TmpName();
        }
        String name = tmpName.getPrevName();
        return null == name ? null : new File(tmpFile, name + "." + extension);
    }

    /**
     * Move curr tmp file to main file.
     *
     * @return the file
     */
    public File moveCurrTmpFileToMain() {
        File source = getCurrTmpFile("png");
        if (null != source) {
            File dest = new File(file, source.getName());
            try {
                copyFile(source, dest);
                return dest;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private class TmpName {
        private final long ID = System.currentTimeMillis();
        private List<String> names;
        private int index;

        /**
         * Instantiates a new Tmp name.
         */
        public TmpName() {
            names = new ArrayList<>();
            index = 0;
        }

        /**
         * Gets next name.
         *
         * @return the next name
         */
        public String getNextName() {
            String name = "tmp_" + ID + "_" + index;
            names.add(name);
            index++;
            return name;
        }

        /**
         * Gets curr name.
         *
         * @return the curr name
         */
        public String getCurrName() {
            if (index > 0) {
                return names.get(index - 1);
            }
            return null;
        }

        /**
         * Gets prev name.
         *
         * @return the prev name
         */
        public String getPrevName() {
            index--;
            if (index > 0) {
                return names.get(index - 1);
            }
            return null;
        }
    }
}
