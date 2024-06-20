package utils;

import java.net.URL;

public class ImageUtils {

    private static final String IMAGE_FOLDER_PATH = "img/";

    public static String GetImageFolderPath() {
        return IMAGE_FOLDER_PATH;
    }

    public static String GetImagePath(String fileName) {
        return IMAGE_FOLDER_PATH + fileName + ".png";
    }

    public URL GetImageURL(String fileName){
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        return classLoader.getResource(GetImagePath(fileName));
    }
}
