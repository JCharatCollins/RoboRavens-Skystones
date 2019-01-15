package Team7159.Utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

import Team7159.Enums.Direction;

/**
 * Created by William on 12/5/2016.
 * Used for managing Bitmaps and manipulating them appropriately
 */

public class BitmapManip {

    public static Bitmap RotateBitmap180(Bitmap source) {
        Matrix tempMatrix = new Matrix();
        tempMatrix.preScale(-1, -1);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), tempMatrix, false);
    }

    public static boolean inRangeBlue(int[] RGBValues){
        if(RGBValues[0]<200){
            if(RGBValues[1]>240){
                if(RGBValues[2]>240){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean inRangeRed(int[] RGBValues){
        if(RGBValues[0]>240){
            if(RGBValues[1]<(RGBValues[0]-10)){
                if(RGBValues[2]<RGBValues[1]){
                    return true;
                }
            }
        }
        return false;
    }

    public static Direction findSide(Bitmap bitmap){
        int AVGBlue = 0;
        int AVGRed = 0;
        int NUMBlue = 0;
        int NUMRed = 0;
        for(int i =0; i < bitmap.getHeight()/5; i++){
            for(int j = 2*bitmap.getWidth()/5; j < 3*bitmap.getWidth()/5;j++){
                int PixelInt = bitmap.getPixel(j,i);
                int[] RGBValues = PixelToRGB(PixelInt);
                if(inRangeBlue(RGBValues)&&!inRangeRed(RGBValues)){
                    NUMBlue++;
                    AVGBlue = AVGBlue + j;
                }else if(inRangeRed(RGBValues)&&!inRangeBlue(RGBValues)){
                    NUMRed++;
                    AVGRed = AVGRed + j;
                }
            }
        }
        if(AVGBlue/NUMBlue < AVGRed/NUMRed){
            return Direction.LEFT;
        }else{
            return Direction.RIGHT;
        }
    }

    public static Bitmap colorSide(Bitmap source){
        Bitmap upload = source;
        for(int i =0; i < source.getHeight()/5; i++){
            for(int j = 2*source.getWidth()/5; j < 3*source.getWidth()/5;j++){
                int PixelInt = source.getPixel(i,j);
                int[] RGBValues = PixelToRGB(PixelInt);
                if(inRangeBlue(RGBValues)&&!inRangeRed(RGBValues)){
                    upload.setPixel(j,i,Color.MAGENTA);
                }else if(inRangeRed(RGBValues)&&!inRangeBlue(RGBValues)){
                    upload.setPixel(j,i,Color.BLACK);
                }
            }
        }
        return upload;
    }

    public static Bitmap drawBoundary(Bitmap source){
        Bitmap newBitmap = source;
        for(int w = 2*source.getWidth()/5; w < 3*source.getWidth()/5; w++){
            for(int h = source.getHeight()/5; h < 5+source.getHeight()/5; h++) {
                newBitmap.setPixel(w, h, Color.GREEN);
            }
        }
        for(int h = 0; h < source.getHeight()/5; h++){
            for(int w = 3*source.getWidth()/5; w < 5+ 3*source.getWidth()/5; w++){
                newBitmap.setPixel(w, h, Color.GREEN);
            }
        }
        return newBitmap;
    }

    public static void saveImageToExternalStorage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
//        System.currentTimeMillis();
        String date = String.valueOf(System.currentTimeMillis());
        String fname = "Image-"  +date+ ".jpg";
        File file = new File(myDir, fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int[] PixelToRGB(int pixel){
        int[] RGBValues = new int[3];
        RGBValues[0] = (pixel & 0xff0000) >> 16;
        RGBValues[1] = (pixel & 0xff00) >> 8;
        RGBValues[2] = (pixel & 0xff);
        return RGBValues;
    }
}
