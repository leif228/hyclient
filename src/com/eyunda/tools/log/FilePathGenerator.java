package com.eyunda.tools.log;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Decide the absolute path of the file which log will be written to.
 * 
 */
public abstract class FilePathGenerator {
    private String path = null;
    protected String dir = "/mnt/sdcard/eyunda/log";
    protected File file = null;

    /**
     * if not set, "app" will be used.
     */
    protected String filename = "eyunda";

    /**
     * if not set, ".log" will be used.
     */
    protected String suffix = ".log";

    //Supress default constructor for noninstantiability
    private FilePathGenerator() {
        throw new AssertionError();
    }

    /**
     * dir will be context.getExternalFilesDir("null").getAbsolutePath() + File.pathSeparator + "snowdream" + File.pathSeparator + "log"
     * filename will be decided by the param filename and suffix together.
     *
     * @param context
     * @param filename
     * @param suffix
     */
    public FilePathGenerator(Context context, String filename, String suffix) {
        if (context == null) {
            throw new NullPointerException("The Context should not be null.");
        }

        dir = context.getExternalFilesDir("null").getAbsolutePath() + File.pathSeparator + "snowdream" + File.pathSeparator + "log";

        if (!TextUtils.isEmpty(filename)) {
            this.filename = filename;
        }

        if (!TextUtils.isEmpty(suffix)) {
            this.suffix = suffix;
        }
    }

    /**
     * dir is from the param dir.
     * filename will be decided by the param filename and suffix together.
     *
     * @param dir
     * @param filename
     * @param suffix
     */
    public FilePathGenerator(String dir, String filename, String suffix) {
        if (dir != null) {
            this.dir = dir;
        }

        if (!TextUtils.isEmpty(filename)) {
            this.filename = filename;
        }

        if (!TextUtils.isEmpty(suffix)) {
            this.suffix = suffix;
        }
    }

    public FilePathGenerator(String filename, String suffix) {
        if (!TextUtils.isEmpty(filename)) {
            this.filename = filename;
        }

        if (!TextUtils.isEmpty(suffix)) {
            this.suffix = suffix;
        }
	}

	/**
     * Generate the file path of the log.
     * 
     * The file path should be absolute.
     *
     * @return the file path of the log
     */
    public abstract String generateFilePath();

    /**
     * Whetether to generate the file path of the log.
     *
     * @return if true,generate the file path of the log, otherwise not.
     */
    public abstract Boolean isGenerate();

    /**
     * It is time to generate the new file path of the log.
     * You can get the new and the old file path of the log.
     * 
     * The file path should be absolute.
     *
     * @param newPath the new file path
     * @param oldPath the old file path
     */
    public abstract void onGenerate(String newPath, String oldPath);

    /**
     * Get the file path of the log. generate a new file path  if needed.
     *
     * @return the file path of the log.
     */
    public final String getPath() {
        if (isGenerate()) {
            String newPath = generateFilePath();

            onGenerate(newPath, path);
            path = newPath;
        }

        return path;
    }

    /**
     * Default FilePathGenerator
     */
    public static class DefaultFilePathGenerator extends FilePathGenerator {

        /**
         * dir will be context.getExternalFilesDir("null").getAbsolutePath() + File.pathSeparator + "snowdream" + File.pathSeparator + "log"
         * filename will be decided by the param filename and suffix together.
         *
         * @param context
         * @param filename
         * @param suffix
         */
        public DefaultFilePathGenerator(Context context, String filename, String suffix) {
            super(context, filename, suffix);

        }

        /**
         * dir is from the param dir.
         * filename will be decided by the param filename and suffix together.
         *
         * @param dir
         * @param filename
         * @param suffix
         */
        public DefaultFilePathGenerator(String dir, String filename, String suffix) {
            super(dir, filename, suffix);
        }
        /**
         * filename will be decided by the param filename and suffix together.
         *
         * @param dir
         * @param filename
         * @param suffix
         */
        public DefaultFilePathGenerator(String filename, String suffix) {
            super(filename, suffix);
        }
        @Override
        public String generateFilePath() {
            String path = null;

            if (TextUtils.isEmpty(dir)) {
                return path;
            }

            File logDir = new File(dir);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            file = new File(logDir, filename+suffix);

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return file.getAbsolutePath();
        }

        @Override
        public Boolean isGenerate() {
            return (file == null) || !file.exists();
        }

        @Override
        public void onGenerate(String newPath, String oldPath) {
        }
    }

    /**
     * Date FilePathGenerator
     */
    public static class DateFilePathGenerator extends FilePathGenerator {

        /**
         * dir will be context.getExternalFilesDir("null").getAbsolutePath() + File.pathSeparator + "snowdream" + File.pathSeparator + "log"
         * filename will be decided by the param filename and suffix together.
         *
         * @param context
         * @param filename
         * @param suffix
         */
        public DateFilePathGenerator(Context context, String filename, String suffix) {
            super(context, filename, suffix);

        }

        /**
         * dir is from the param dir.
         * filename will be decided by the param filename and suffix together.
         *
         * @param dir
         * @param filename
         * @param suffix
         */
        public DateFilePathGenerator(String dir, String filename, String suffix) {
            super(dir, filename, suffix);
        }
        /**
         * dir is from the param dir.
         * filename will be decided by the param filename and suffix together.
         *
         * @param dir
         * @param filename
         * @param suffix
         */
        public DateFilePathGenerator(String filename, String suffix) {
        	super(filename, suffix);
        }
        @Override
        public String generateFilePath() {
            String path = null;

            if (TextUtils.isEmpty(dir)) {
                return path;
            }

            File logDir = new File(dir);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            Date myDate = new Date();
            SimpleDateFormat fdf =new SimpleDateFormat("yyyy-MM-dd");
            String myDateString = fdf.format(myDate);

            StringBuffer buffer = new StringBuffer();
            buffer.append(filename);
            buffer.append("-");
            buffer.append(myDateString);
            buffer.append(suffix);

            file = new File(logDir, buffer.toString());

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return file.getAbsolutePath();
        }

        @Override
        public Boolean isGenerate() {
            return (file == null) || !file.exists();
        }

        @Override
        public void onGenerate(String newPath, String oldPath) {
        }
    }

    /**
     * LimitSize FilePathGenerator
     */
    public static class LimitSizeFilePathGenerator extends FilePathGenerator {
        private int maxSize = 0;

        /**
         * dir is from the param dir.
         * filename will be decided by the param filename and suffix together.
         *
         * @param context
         * @param filename
         * @param suffix
         * @param maxSize
         */
        public LimitSizeFilePathGenerator(Context context, String filename, String suffix, int maxSize) {
            super(context, filename, suffix);
            this.maxSize = maxSize;
        }

        /**
         * dir will be context.getExternalFilesDir("null").getAbsolutePath() + File.pathSeparator + "snowdream" + File.pathSeparator + "log"
         * filename will be decided by the param filename and suffix together.
         *
         * @param dir
         * @param filename
         * @param suffix
         * @param maxSize
         */
        public LimitSizeFilePathGenerator(String dir, String filename, String suffix, int maxSize) {
            super(dir, filename, suffix);
            this.maxSize = maxSize;
        }

        @Override
        public String generateFilePath() {
            String path = null;

            if (TextUtils.isEmpty(dir)) {
                return path;
            }

            File logDir = new File(dir);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            Date myDate = new Date();
            SimpleDateFormat fdf =new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String myDateString = fdf.format(myDate);

            StringBuffer buffer = new StringBuffer();
            buffer.append(filename);
            buffer.append("-");
            buffer.append(myDateString);
            buffer.append(suffix);

            file = new File(logDir, buffer.toString());

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return file.getAbsolutePath();
        }

        @Override
        public Boolean isGenerate() {
            return (file == null) || !file.exists() || file.length() >= maxSize;
        }

        @Override
        public void onGenerate(String newPath, String oldPath) {
        }
    }
}
