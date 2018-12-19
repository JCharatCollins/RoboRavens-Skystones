package Team7159.Utils;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.vuforia.Frame;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;

import java.nio.ByteBuffer;

/**
 * Created by William on 11/9/2016.
 */

public class CameraHelper {

    public Frame Frames;
    Image image;
    Bitmap bitmap;
    VuforiaLocalizer vuforia;
    VuforiaLocalizer.Parameters parameters;
    double degreesToTurn;
    long NumOfImages;

    public void init() {
        parameters = new VuforiaLocalizer.Parameters(com.qualcomm.ftcrobotcontroller.R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AVktbtv/////AAAAGY1koTqeTUyKsH17S4sxg5FdzjlL4sab4r1TteImHLQZaxsQP96TVimg0LSECJMSTY" +
                "/hMmyl4Ko8WqEFHdESFWl5CNgqDIkVJsLD4ivpj1OAwtHu6z1Me1lnhV/DlBshYL9nsfqWCvVyPPpMkYBj3DRGGI" +
                "6OHwD29CokKIxnknH8sV/k8xdVFSAmsRqBney+t4+c7vmUw39q7qrsE63Pf6wnFxYLkDz4uFvjy3IHbX3/OLojTN" +
                "Gk4/sHOWnME0c8EEVXUZAoXPM/7jJK/ksBrYMPyJTZOeMPhcTMtjsPNMVx54p5yICLcIGjqPwTih1Z88RGDGKIuY" +
                "vIrnSMjUnJNZtshuuqadeAXk2HyGS6DR3K";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        vuforia.setFrameQueueCapacity(6);
    }

    public VuforiaLocalizer getVuforia(){
        return vuforia;
    }

    public VuforiaLocalizer.Parameters getParamters(){
        return parameters;
    }

    public OpenGLMatrix getPose(VuforiaTrackable image){
        return ((VuforiaTrackableDefaultListener)image.getListener()).getPose();
    }

    public VectorF getVector(VuforiaTrackable image){
        return getPose(image).getTranslation();
    }

    public double getDegreesToTurn(VuforiaTrackable image){
        degreesToTurn = Math.toDegrees(Math.atan2(getVector(image).get(2),getVector(image).get(1)))+90.0;
        return degreesToTurn;
    }


    public Bitmap getBitmap(){
        bitmap = null;
        if(vuforia.getFrameQueue().peek() != null){
            try {
                Frames = vuforia.getFrameQueue().take();
            }catch(InterruptedException e){}
            NumOfImages = Frames.getNumImages();
            for(int i =0; i < NumOfImages; i++){
                if(Frames.getImage(i).getFormat()== PIXEL_FORMAT.RGB565){
                    image = Frames.getImage(i);
                    ByteBuffer pixels = image.getPixels();
                    bitmap = Bitmap.createBitmap(new DisplayMetrics(),image.getWidth(),image.getHeight(),Bitmap.Config.RGB_565);
                    bitmap.copyPixelsFromBuffer(pixels);
                }
            }
        }
        return bitmap;
    }
}
