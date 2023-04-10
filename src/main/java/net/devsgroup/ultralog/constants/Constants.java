package net.devsgroup.ultralog.constants;

public class Constants {
    public static String TAG = "TAG";
    private static String fileName = "logs";

    public static String getFileName() {
        return fileName + ".txt";
    }

    public static void setFileName(String rawFileName) {
        fileName = clearExtension(rawFileName);
    }

    private static String clearExtension(String fileName) {
        return fileName.replace(".txt", "");
    }
    public static final int v = 0;
    public static final int d = 1;
    public static final int i = 2;
    public static final int w = 3;
    public static final int e = 4;
    public static final int wtf = 5;
}
