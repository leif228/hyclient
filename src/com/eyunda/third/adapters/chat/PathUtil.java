package com.eyunda.third.adapters.chat;

import android.content.Context;
import android.os.Environment;
import java.io.File;

public class PathUtil
{

    private PathUtil()
    {
        voicePath = null;
        imagePath = null;
        historyPath = null;
        videoPath = null;
    }

    public static PathUtil getInstance()
    {
        if(instance == null)
            instance = new PathUtil();
        return instance;
    }

    public void initDirs(String s, String s1, Context context)
    {
        String s2 = context.getPackageName();
        pathPrefix = (new StringBuilder()).append("/Android/data/").append(s2).append("/").toString();
        voicePath = generateVoicePath(s, s1, context);
        if(!voicePath.exists())
            voicePath.mkdirs();
        imagePath = generateImagePath(s, s1, context);
        if(!imagePath.exists())
            imagePath.mkdirs();
        historyPath = generateHistoryPath(s, s1, context);
        if(!historyPath.exists())
            historyPath.mkdirs();
        videoPath = generateVideoPath(s, s1, context);
        if(!videoPath.exists())
            videoPath.mkdirs();
        filePath = generateFiePath(s, s1, context);
        if(!filePath.exists())
            filePath.mkdirs();
    }

    public File getImagePath()
    {
        return imagePath;
    }

    public File getVoicePath()
    {
        return voicePath;
    }

    public File getFilePath()
    {
        return filePath;
    }

    public File getVideoPath()
    {
        return videoPath;
    }

    public File getHistoryPath()
    {
        return historyPath;
    }

    private static File getStorageDir(Context context)
    {
        if(storageDir == null)
        {
            File file = Environment.getExternalStorageDirectory();
            if(file.exists())
                return file;
            storageDir = context.getFilesDir();
        }
        return storageDir;
    }

    private static File generateImagePath(String s, String s1, Context context)
    {
        String s2 = null;
        if(s == null)
            s2 = (new StringBuilder()).append(pathPrefix).append(s1).append("/image/").toString();
        else
            s2 = (new StringBuilder()).append(pathPrefix).append(s).append("/").append(s1).append("/image/").toString();
        return new File(getStorageDir(context), s2);
    }

    private static File generateVoicePath(String s, String s1, Context context)
    {
        String s2 = null;
        if(s == null)
            s2 = (new StringBuilder()).append(pathPrefix).append(s1).append("/voice/").toString();
        else
            s2 = (new StringBuilder()).append(pathPrefix).append(s).append("/").append(s1).append("/voice/").toString();
        return new File(getStorageDir(context), s2);
    }

    private static File generateFiePath(String s, String s1, Context context)
    {
        String s2 = null;
        if(s == null)
            s2 = (new StringBuilder()).append(pathPrefix).append(s1).append("/file/").toString();
        else
            s2 = (new StringBuilder()).append(pathPrefix).append(s).append("/").append(s1).append("/file/").toString();
        return new File(getStorageDir(context), s2);
    }

    private static File generateVideoPath(String s, String s1, Context context)
    {
        String s2 = null;
        if(s == null)
            s2 = (new StringBuilder()).append(pathPrefix).append(s1).append("/video/").toString();
        else
            s2 = (new StringBuilder()).append(pathPrefix).append(s).append("/").append(s1).append("/video/").toString();
        return new File(getStorageDir(context), s2);
    }

    private static File generateHistoryPath(String s, String s1, Context context)
    {
        String s2 = null;
        if(s == null)
            s2 = (new StringBuilder()).append(pathPrefix).append(s1).append("/chat/").toString();
        else
            s2 = (new StringBuilder()).append(pathPrefix).append(s).append("/").append(s1).append("/chat/").toString();
        return new File(getStorageDir(context), s2);
    }

    private static File generateMessagePath(String s, String s1, Context context)
    {
        File file = new File(generateHistoryPath(s, s1, context), (new StringBuilder()).append(s1).append(File.separator).append("Msg.db").toString());
        return file;
    }

    public static File getTempPath(File file)
    {
        File file1 = new File((new StringBuilder()).append(file.getAbsoluteFile()).append(".tmp").toString());
        return file1;
    }

    public static String pathPrefix;
    public static final String historyPathName = "/chat/";
    public static final String imagePathName = "/image/";
    public static final String voicePathName = "/voice/";
    public static final String filePathName = "/file/";
    public static final String videoPathName = "/video/";
    public static final String netdiskDownloadPathName = "/netdisk/";
    public static final String meetingPathName = "/meeting/";
    private static File storageDir = null;
    private static PathUtil instance = null;
    private File voicePath;
    private File imagePath;
    private File historyPath;
    private File videoPath;
    private File filePath;

}