package android.media;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by  yahuigao
 * Date: 2019-12-11
 * Time: 16:27
 * Description:
 */
public class MediaFileHelper {


    interface HOLDER {
        MediaFileHelper f = new MediaFileHelper();
    }


    private HashMap<String, Integer> sFileTypeMap;
    private HashMap<String, Integer> sMimeTypeMap;

    private MediaFileHelper() {
        try {
            Class<?> clazz = Class.forName("android.media.MediaFile");

            Field sFileTypeMapF = clazz.getDeclaredField("sFileTypeToFormatMap");
            Field sMimeTypeMapF = clazz.getDeclaredField("sMimeTypeToFormatMap");

            sFileTypeMapF.setAccessible(true);
            sMimeTypeMapF.setAccessible(true);
            sFileTypeMap = (HashMap<String, Integer>) sFileTypeMapF.get(clazz);
            sMimeTypeMap = (HashMap<String, Integer>) sMimeTypeMapF.get(clazz);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static MediaFileHelper getInstance() {
        return HOLDER.f;
    }

    public String getFileType(String mineType) {
        if (sFileTypeMap == null || sMimeTypeMap == null) return null;
        if (!sMimeTypeMap.containsKey(mineType)) return null;


        if (!mineType.contains("video") || !mineType.contains("/")) {
            return null;
        }
        int index = mineType.indexOf("/");
        return mineType.substring(index + 1);
    }


}
