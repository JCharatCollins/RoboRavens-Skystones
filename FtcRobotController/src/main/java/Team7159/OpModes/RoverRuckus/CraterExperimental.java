package Team7159.OpModes.RoverRuckus;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.Frame;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.nio.ByteBuffer;
import java.util.List;

import Team7159.ComplexRobots.VacuumBotV2;
import Team7159.Enums.Direction;
import Team7159.Enums.Side;

import static Team7159.Utils.BitmapManip.RotateBitmap180;
import static Team7159.Utils.BitmapManip.colorSide;
import static Team7159.Utils.BitmapManip.drawBoundary;
import static Team7159.Utils.BitmapManip.saveImageToExternalStorage;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_GOLD_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_SILVER_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.TFOD_MODEL_ASSET;


@Autonomous(name = "Crater Experimental")
public class CraterExperimental extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";


    //Used for vuforia
    VuforiaLocalizer vuforia;

    private static final String VUFORIA_KEY = "AQ8rpDD/////AAABmRNIMKzPaEhBgamlRTL2RRMKI6zA+IW8Qqd6l0v65fwa8N2l" +
            "n17xMthqidBc7uWyTNA1pYUodjK8ngEvudjz4FeoJbQpXxwYm2/H5XXwlWywZfUHa74lGuma90fRmTuEeFwAsDTZ4JfXojf719" +
            "wTliDCdlKCOkQuuvU0Cx0JyzdYT/NnOYZWroHx2maby73wQW1T76aSlKsHE/cZ1FmVoOokP+HqIfaOPpUR/gVkDgqyB7XAOaBd" +
            "kHzY3FOv7oCC7Ybn7jbAdVuJX8uow08atIH0dwmS/LC6BqpakDGFNj4JAyyd9cnz43UWBMEz6cTBk9Um2+a/5XLfV+W7RaHDEF" +
            "fs726qLAIagk9Nd2CzIg/x";

    private TFObjectDetector tfod;

    VacuumBotV2 robot = new VacuumBotV2();

    int goldMineralX = 0;

    //If pos = 0, it is in the center, then goes to 1 to left and 2 for right
    int pos = 0;

    List<Recognition>  updatedRecognitions;

    //tells whether or not it completed sampling
    private boolean comp = false;

    @Override
    public void runOpMode(){

        //Initializes the robot with hardwareMap
        robot.init(hardwareMap);


        initVuforia();

        //Sets up vuforia to take pictures
        vuforia.setFrameQueueCapacity(6);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);

        //Initializes the object detector
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
       // robot.liftServo.setPosition(0.3);

        waitForStart();

        if (tfod != null) {
            tfod.activate();
        }

        //Goes down from lander
        robot.liftMotor.setPower(0.6);
        sleep(2000);
        robot.liftMotor.setPower(0);
        sleep(250);

        //Moves out of lander and orients in front of center block
        moveStraight(Direction.BACKWARDS,0.4,0.7);
        strafe(Direction.LEFT,0.3,2);
        moveStraight(Direction.FORWARDS,0.3,0.5);
        turn(Direction.LEFT,0.5,0.92);
        sleep(500);

        //Checks the center location for mineral and determines what it is
        //If it determines it is gold, drives forward to knock if off, else increments pos
        center();

        //Takes picture
        takePic();

        //If is gold, will move forwards again and sets comp to true
        if(pos == 0){
            moveStraight(Direction.FORWARDS,0.5,1);
            comp = true;
        }else{
            //pos will be equal to 1, meaning was either silver or not found.
            //Check to strafe left
            strafe(Direction.LEFT,0.5,1.2);
            sleep(200);
            center();
        }

        //Takes picture
        takePic();

        //If it's 1 this position or last was gold, so if its not completed its this position
        if(pos == 1 && !comp){
            moveStraight(Direction.FORWARDS,0.5,1);
            comp = true;
        } else if(pos == 2){
            //If position is 2 then it means it must be the last one
            strafe(Direction.RIGHT,0.5,2.3);
            sleep(1000);
            center();
        }

        takePic();

        if(!comp){
            //If we're not complete yet it must be right position
            moveStraight(Direction.FORWARDS,0.5,1);
        }

        //Sets down the vacuum to get above the "vertical barrier"
        robot.vacuumMotor.setPower(-0.3);
        sleep(1000);
        robot.vacuumMotor.setPower(0);
    }

    public void stopMotors(){
        robot.stop();
    }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public void strafe(Direction dir, double pow, double time){
        double t = time*1000;
        int t1 = (int)t;
        if(dir == Direction.LEFT){
            robot.LFMotor.setPower(-pow);
            robot.RFMotor.setPower(pow);
            robot.LBMotor.setPower(pow);
            robot.RBMotor.setPower(-pow);
            sleep(t1);
            stopMotors();
        }else if(dir == Direction.RIGHT){
            robot.LFMotor.setPower(pow);
            robot.RFMotor.setPower(-pow);
            robot.LBMotor.setPower(-pow);
            robot.RBMotor.setPower(pow);
            sleep(t1);
            stopMotors();
        }else{
            //Throw an exception about the wrong side
        }
    }

    private void turn(Direction dir, double pow, double time){
        double t = time*1000;
        int t1 = (int)t;
        if(dir == Direction.LEFT){
            robot.RFMotor.setPower(pow);
            robot.RBMotor.setPower(pow);
            robot.LFMotor.setPower(-pow);
            robot.LBMotor.setPower(-pow);
            sleep(t1);
        }else if(dir == Direction.RIGHT){
            robot.RFMotor.setPower(-pow);
            robot.RBMotor.setPower(-pow);
            robot.LFMotor.setPower(pow);
            robot.LBMotor.setPower(pow);
            sleep(t1);
        }else{
            //Error
        }
        stopMotors();
    }

    private void moveStraight(Direction dir, double pow, double time){
        double t = time*1000;
        int t1 = (int)t;
        if(dir == Direction.FORWARDS){
            robot.RFMotor.setPower(pow);
            robot.RBMotor.setPower(pow);
            robot.LFMotor.setPower(pow);
            robot.LBMotor.setPower(pow);
            sleep(t1);
        }else if(dir == Direction.BACKWARDS){
            robot.RFMotor.setPower(-pow);
            robot.RBMotor.setPower(-pow);
            robot.LFMotor.setPower(-pow);
            robot.LBMotor.setPower(-pow);
            sleep(t1);
        }else{
            //Error
        }
        stopMotors();
    }

    public void runUntilCenter(int pos){
        int gMX = pos;
        while(gMX <=350 && gMX >=475){
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    if (updatedRecognitions.size() >=1) {
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                int left = (int)recognition.getTop();
                                telemetry.addData("Gold Mineral left: ", left);
                                gMX = left;
                            }
                        }
                    }
                    telemetry.update();
                }
            }
            if(goldMineralX>=550){
                strafe(Direction.LEFT,0.3,0.25);
            }else if(goldMineralX<=350) {
                strafe(Direction.RIGHT, 0.3, 0.25);
            }
        }
    }

    public void center(){
        updatedRecognitions = tfod.getUpdatedRecognitions();
        if(updatedRecognitions.size() == 1){
            Recognition rec = updatedRecognitions.get(0);
            if(rec.getLabel()==LABEL_GOLD_MINERAL){
                telemetry.addData("found","gold mineral found");
                telemetry.update();
                runUntilCenter((int)rec.getTop());
                moveStraight(Direction.FORWARDS,0.5,0.5);
            }else{
                telemetry.addData("found","silver mineral found");
                telemetry.update();
                pos++;
            }
        }else{
            telemetry.addData("Size",updatedRecognitions.size());
            pos++;
            telemetry.addData("center","nothing found");
            telemetry.update();
//            moveStraight(Direction.FORWARDS,0.5,1);
        }
    }

    public Bitmap getBitmap() throws InterruptedException{
        Frame frame;
        Bitmap BM0 = Bitmap.createBitmap(new DisplayMetrics(), 100, 100, Bitmap.Config.RGB_565);
        if (vuforia.getFrameQueue().peek() != null) {
            frame = vuforia.getFrameQueue().take();
            for (int i = 0; i < frame.getNumImages(); i++) {
                if (frame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
                    Image image = frame.getImage(i);
                    ByteBuffer pixels = image.getPixels();
                    Matrix matrix = new Matrix();
                    matrix.preScale(-1, -1);
                    Bitmap bitmap = Bitmap.createBitmap(new DisplayMetrics(), image.getWidth(), image.getHeight(), Bitmap.Config.RGB_565);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                    bitmap.copyPixelsFromBuffer(pixels);
                    return bitmap;
                }
            }
        }
        return BM0;
    }

    public void takePic(){
        try {
            Bitmap bitmap = getBitmap();
            Bitmap newBitmap = RotateBitmap180(bitmap);
            saveImageToExternalStorage(newBitmap);
        }catch(Exception e){}
    }

}
