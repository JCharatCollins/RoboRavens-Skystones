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

import static Team7159.Utils.BitmapManip.RotateBitmap180;
import static Team7159.Utils.BitmapManip.saveImageToExternalStorage;


@Autonomous(name = "Crater Experimental")
public class CraterExperimental extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    boolean lower = true;

    //Used for vuforia
    private VuforiaLocalizer vuforia;

    private static final String VUFORIA_KEY = "AQ8rpDD/////AAABmRNIMKzPaEhBgamlRTL2RRMKI6zA+IW8Qqd6l0v65fwa8N2l" +
            "n17xMthqidBc7uWyTNA1pYUodjK8ngEvudjz4FeoJbQpXxwYm2/H5XXwlWywZfUHa74lGuma90fRmTuEeFwAsDTZ4JfXojf719" +
            "wTliDCdlKCOkQuuvU0Cx0JyzdYT/NnOYZWroHx2maby73wQW1T76aSlKsHE/cZ1FmVoOokP+HqIfaOPpUR/gVkDgqyB7XAOaBd" +
            "kHzY3FOv7oCC7Ybn7jbAdVuJX8uow08atIH0dwmS/LC6BqpakDGFNj4JAyyd9cnz43UWBMEz6cTBk9Um2+a/5XLfV+W7RaHDEF" +
            "fs726qLAIagk9Nd2CzIg/x";

    private TFObjectDetector tfod;
    boolean[] sPos = new boolean[3];

    private VacuumBotV2 robot = new VacuumBotV2();

    //If pos = 0, it is in the center, then goes to 1 to left and 2 for right
    private int pos = 0;

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

        robot.liftServo.setPosition(0.225);
        robot.liftMotor.setPower(0.9);
        sleep(1600);
        robot.liftMotor.setPower(0);
        sleep(50);
        //Moves out of lander and orients in front of center block
        // moveStraight(Direction.BACKWARDS,1,2);


        robot.driveDir(Direction.BACKWARDS, 4);

//        robot.liftMotor.setPower(-1);
//        sleep(1000);
//        robot.liftMotor.setPower(0);
//        sleep(50);
//        robot.driveDir(Direction.FORWARDS, 2);

        robot.strafe(Direction.LEFT, 13);


        //robot.liftMotor.setPower(-0.8);
        //sleep(1500);
        /*robot.liftMotor.setPower(0);
        sleep(50);*/

        robot.driveDir(Direction.FORWARDS, 4);


        robot.turn(Direction.LEFT,88);
        // turn(Direction.LEFT,0.5,1.1);
        sleep(500);

        //set all sleeps before vuforia to 200, if this doesnt work then change back to 500, then 750
        // sleep(750);
        //Checks the center location for mineral and determines what it is
        //If it determines it is gold, drives forward to knock if off, else increments pos
        takePic();

        center(1);
        //Takes picture

        //If is gold, will move forwards again and sets comp to true
        if(pos == 0){

            comp = true;
        }else {
            Thread thread = new Thread() {
                @Override
                public void run() {

                    try {


                        robot.liftMotor.setPower(-0.8);
                        sleep(1500);
                        robot.liftMotor.setPower(0);
                        sleep(200);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();


            lower = false;
            //pos will be equal to 1, meaning was either silver or not found.
            //Check to strafe left
            robot.strafe(Direction.RIGHT, 15);
            //moveRight(14);
            // robot.strafe(Direction.LEFT, 12);
            sleep(500);
            takePic();

            center(2);
        }
        //Takes picture

        //If it's 1 this position or last was gold, so if its not completed its this position
        if(pos == 1 && !comp){

            comp = true;
        } else if(pos == 2){
            //If position is 2 then it means it must be the last one
            robot.strafe(Direction.LEFT, 28);
            //robot.driveDir(Direction.FORWARDS, 5);
            sleep(500);
            takePic();
            center(3);
            //If it finds it will drive forward 20s


            //  takePic();
        }

        telemetry.addData("Mrs. obama", "get down");
        telemetry.update();


        //WHATEVER IS BELOW HERE HAS NOT BEEN TESTED, HENCE EXPERIMENTAL

        if(pos == 3) {
            telemetry.addData("test", "pos ==3");
            int sCount = 0;
            int fLocation = 0;
            for (int i = 0; i < 3; i++) {
                if (sPos[i]) {
                    sCount++;
                } else {
                    fLocation = i + 1;
                }
            }
            telemetry.addData("test", "stop flaming me aidan " + String.valueOf(sCount) + " " + String.valueOf(fLocation));
            telemetry.update();
            if (sCount == 2) {
                if (fLocation == 1) {
                    //gold is in first position, strafe back
                    moveRight(12);
                    robot.driveDir(Direction.FORWARDS, 15);
                    pos = 0;
                } else if (fLocation == 2) {
                    robot.strafe(Direction.RIGHT, 26);
                    //moveRight(26);
                    robot.driveDir(Direction.FORWARDS, 15);
                    comp = true;
                    pos = 1;

                } else if (fLocation == 3) {
                    robot.driveDir(Direction.FORWARDS, 15);
                    // robot.driveDir(Direction.FORWARDS, 15);
                    comp = true;
                    pos=2;
                }
            } else {
                robot.driveDir(Direction.FORWARDS, 15);
                robot.driveDir(Direction.BACKWARDS, 8);
                robot.turn(Direction.LEFT, 90);
                robot.driveDir(Direction.FORWARDS, 35);
                robot.turn(Direction.LEFT, 32);
                comp = true;
            }
        }
        //This should equalize positions to against the wall, back facing the crater
        if(pos == 0) {
            //Center
            robot.driveDir(Direction.BACKWARDS, 10);
            robot.turn(Direction.LEFT, 85);
            robot.driveDir(Direction.FORWARDS, 56);
            robot.turn(Direction.LEFT, 33);



        }else if(pos == 1){
            //Right
            robot.driveDir(Direction.BACKWARDS, 14);
            robot.turn(Direction.LEFT, 85);
            robot.driveDir(Direction.FORWARDS, 65);
            robot.turn(Direction.LEFT, 33);


        }else if(pos == 2){
            //Left
            robot.driveDir(Direction.BACKWARDS, 8);
            robot.turn(Direction.LEFT, 90);
            robot.driveDir(Direction.FORWARDS, 35);
            robot.turn(Direction.LEFT, 32);
            //robot.driveDir(Direction.FORWARDS, 10);


            //Lower here
        }

        if(pos==2||pos==3) {
            robot.driveDir(Direction.FORWARDS, 25);

        }
        else {
            robot.driveDir(Direction.FORWARDS, 20);
        }
        //Sets down vacuumMotor to get above crater
        lower(0.9,0.3);
        robot.chainMotor.setPower(0.8);
        sleep(200);
        robot.chainMotor.setPower(0);
        if(pos==2||pos==3) {
            robot.driveDir(Direction.BACKWARDS, 45);

        }
        else {
            robot.driveDir(Direction.BACKWARDS, 35);
        }
       // robot.driveDir(Direction.BACKWARDS, 35);
        raise(0.9, 0.3);
        robot.turn(Direction.LEFT, 170);
        robot.driveDir(Direction.FORWARDS, 10);
        lower(0.7, 0.5);

        robot.liftMotor.setPower(-0.9);
        sleep(1500);
        robot.liftMotor.setPower(0);
       sleep(50);

    }

    private void stopMotors(){
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


    private void strafe(Direction dir, double pow, double time){
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
        }
        stopMotors();
    }

    private void raise(double power, double time){
        double t = time*1000;
        int t1 = (int)t;
        robot.vacuumMotor.setPower(Math.abs(power));
        sleep(t1);
        robot.vacuumMotor.setPower(0);
    }

    private void lower(double power, double time){
        double t = time*1000;
        int t1 = (int)t;
        robot.vacuumMotor.setPower(-Math.abs(power));
        sleep(t1);
        robot.vacuumMotor.setPower(0);
    }

    private void runUntilCenter(int pos){
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
            if(gMX>=550){
                strafe(Direction.LEFT,0.3,0.25);
            }else if(gMX<=350) {
                strafe(Direction.RIGHT, 0.3, 0.25);
            }
        }
    }

    public void center(int cPos){
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if(updatedRecognitions.size() == 1){
            Recognition rec = updatedRecognitions.get(0);
            if(rec.getLabel().equals(LABEL_GOLD_MINERAL)){
                telemetry.addData("found","gold mineral found");
                telemetry.update();
                //uncomment these when you want the gold detection to work, delete the moveStraight
                runUntilCenter((int)rec.getTop());
                robot.driveDir(Direction.FORWARDS, 15);

            }else{
                sPos[cPos-1] = true;
                telemetry.addData("found","silver mineral found" + String.valueOf(cPos));
                telemetry.update();
                pos++;
            }
        }else if(updatedRecognitions.size()>1) {
            telemetry.addData("Size is greater than 1 ",updatedRecognitions.size());
            telemetry.addData("checking for gold","balls");
            telemetry.update();
            //Additional redundancy, treating bigger size as gold
            boolean goldF = false;
            for(Recognition rec: updatedRecognitions){
                if(rec.getLabel().equals(LABEL_GOLD_MINERAL)){
                    telemetry.addData("Size is greater than 1 ",updatedRecognitions.size());
                    telemetry.addData(" gold"," found");
                    telemetry.update();
                    if(rec.getConfidence()>=0.9) goldF = true;
                }
            }
            if(goldF) {
                robot.driveDir(Direction.FORWARDS, 15);
            }else {
                telemetry.addData("Size is greater than 1 ",updatedRecognitions.size());
                telemetry.addData(" gold not found"," assuming is silver");
                telemetry.update();
                sPos[cPos-1] = true;

                pos++;
            }
        }else {
            telemetry.addData("Size",updatedRecognitions.size());

            pos++;
            telemetry.addData("center","nothing found");
            telemetry.update();
//            moveStraight(Direction.FORWARDS,0.5,1);
        }
    }

    private Bitmap getBitmap() throws InterruptedException{
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

    private void takePic(){
        try {
            Bitmap bitmap = getBitmap();
            Bitmap newBitmap = RotateBitmap180(bitmap);
            saveImageToExternalStorage(newBitmap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void moveRight(int distance)
    {
        robot.turn(Direction.RIGHT, 90);
        robot.driveDir(Direction.FORWARDS, distance);
        robot.turn(Direction.LEFT, 90);
    }
}
