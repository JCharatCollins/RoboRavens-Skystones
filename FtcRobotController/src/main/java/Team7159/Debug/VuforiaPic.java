package Team7159.Debug;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.Frame;
import com.vuforia.HINT;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import static Team7159.Utils.BitmapManip.RotateBitmap180;
import static Team7159.Utils.BitmapManip.colorSide;
import static Team7159.Utils.BitmapManip.drawBoundary;
import static Team7159.Utils.BitmapManip.saveImageToExternalStorage;

@Autonomous(name = "VuforiaPic")
/**
 * Created by William on 9/26/2016, used to test Vuforia taking pictures
 */
@Disabled

public class VuforiaPic extends LinearOpMode {
    VuforiaLocalizer vuforia;
    Frame frame;
    Bitmap bitmap;

    public Bitmap getBitmap(){
        if(vuforia.getFrameQueue().peek() != null){
            try {
                frame = vuforia.getFrameQueue().take();
            }catch(InterruptedException e){}
        }
        for (int i = 0; i < frame.getNumImages(); i++) {
            if (frame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
                Image image = frame.getImage(i);
                ByteBuffer pixels = image.getPixels();
                bitmap = Bitmap.createBitmap(new DisplayMetrics(),image.getWidth(),image.getHeight(),Bitmap.Config.RGB_565);
                bitmap.copyPixelsFromBuffer(pixels);
                return bitmap;
            }
        }
        return bitmap;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(com.qualcomm.ftcrobotcontroller.R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AVktbtv/////AAAAGY1koTqeTUyKsH17S4sxg5FdzjlL4sab4r1TteImHLQZaxsQP96TVimg0LSECJMSTY" +
                "/hMmyl4Ko8WqEFHdESFWl5CNgqDIkVJsLD4ivpj1OAwtHu6z1Me1lnhV/DlBshYL9nsfqWCvVyPPpMkYBj3DRGGI" +
                "6OHwD29CokKIxnknH8sV/k8xdVFSAmsRqBney+t4+c7vmUw39q7qrsE63Pf6wnFxYLkDz4uFvjy3IHbX3/OLojTN" +
                "Gk4/sHOWnME0c8EEVXUZAoXPM/7jJK/ksBrYMPyJTZOeMPhcTMtjsPNMVx54p5yICLcIGjqPwTih1Z88RGDGKIuY" +
                "vIrnSMjUnJNZtshuuqadeAXk2HyGS6DR3K";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        vuforia.setFrameQueueCapacity(6);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);
        VuforiaTrackables Images = this.vuforia.loadTrackablesFromAsset("FTC_2016-17");
        VuforiaTrackable Wheels = Images.get(0);
        Wheels.setName("WheelsImage");  // Wheel Images, Blue #1

        VuforiaTrackable Tools = Images.get(1);
        Tools.setName("ToolsImage");  // Tool Images, Red #2

        VuforiaTrackable Legos = Images.get(2);
        Legos.setName("LegosImage");  // Lego Images, Blue #2

        VuforiaTrackable Gears = Images.get(3);
        Gears.setName("GearsImage");  // Gear Images, Red #1

        ArrayList<VuforiaTrackable> BlueImages = new ArrayList<VuforiaTrackable>();
        BlueImages.add(Wheels);
        BlueImages.add(Legos);

        waitForStart();

        int count = 0;

        while (opModeIsActive()) {
            count++;
            Bitmap bitmap = getBitmap();
            Bitmap newBitmap = RotateBitmap180(bitmap);
            Bitmap newerBitmap = drawBoundary(newBitmap);
            Bitmap SideColor = colorSide(newerBitmap);
            saveImageToExternalStorage(SideColor);
        }
    }
}