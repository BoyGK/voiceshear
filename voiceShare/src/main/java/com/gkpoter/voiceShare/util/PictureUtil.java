package com.gkpoter.voiceShare.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dy on 2016/10/26.
 */
public class PictureUtil {

    public void savePicture(Bitmap bitmap, String Path, String PhotoName){
        File file=new File(Path,PhotoName);
        try {
            FileOutputStream out=new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)){
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Bitmap getPicture(String Path, String name){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap bitmap;
        options.inJustDecodeBounds = false;

        int be = (int)(options.outHeight / (float)200);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        bitmap=BitmapFactory.decodeFile(Path+"/"+name,options);
        return bitmap;
    }

}
