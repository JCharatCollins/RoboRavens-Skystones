package Team7159.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import Team7159.ComplexRobots.VacuumBotV2;
import Team7159.Enums.Direction;
import Team7159.Enums.Side;

@Autonomous(name="Depot Experimental")
public class DepotExperimental extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    Side side;

    private static final String VUFORIA_KEY = "AQ8rpDD/////AAABmRNIMKzPaEhBgamlRTL2RRMKI6zA+IW8Qqd6l0v65fwa8N2l" +
            "n17xMthqidBc7uWyTNA1pYUodjK8ngEvudjz4FeoJbQpXxwYm2/H5XXwlWywZfUHa74lGuma90fRmTuEeFwAsDTZ4JfXojf719" +
            "wTliDCdlKCOkQuuvU0Cx0JyzdYT/NnOYZWroHx2maby73wQW1T76aSlKsHE/cZ1FmVoOokP+HqIfaOPpUR/gVkDgqyB7XAOaBd" +
            "kHzY3FOv7oCC7Ybn7jbAdVuJX8uow08atIH0dwmS/LC6BqpakDGFNj4JAyyd9cnz43UWBMEz6cTBk9Um2+a/5XLfV+W7RaHDEF" +
            "fs726qLAIagk9Nd2CzIg/x";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;

    VacuumBotV2 robot = new VacuumBotV2();

    int goldMineralX = 0;

    @Override
    public void runOpMode(){

        robot.init(hardwareMap);

        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        waitForStart();

        robot.liftMotor.setPower(0.6);
        sleep(2250);
        robot.liftMotor.setPower(0);
        turn(Direction.LEFT,0.3,1);
        strafe(Direction.RIGHT, 0.4, 1.5);
        turn(Direction.RIGHT,0.3,1.25);
        robot.liftMotor.setPower(-0.6);
        sleep(1500);
        robot.liftMotor.setPower(0);
        strafe(Direction.RIGHT,0.4,1);
        robot.moveStraight(0.2);
        sleep(250);
        stopMotors();
        telemetry.addData("Ended time loop","yes");
        telemetry.update();
        if (tfod != null) {
            tfod.activate();
        }

        while(goldMineralX <=350 && goldMineralX >=475){
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
                                goldMineralX = left;
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

        robot.moveStraight(0.4);
        sleep(900);
        stopMotors();
        robot.vacuumMotor.setPower(-0.5);
        sleep(500);
        robot.vacuumMotor.setPower(0);
        robot.chainMotor.setPower(0.5);
        sleep(800);
        robot.chainMotor.setPower(0);
    }

    public void strafe(Direction direction, double power, double time){
        if(direction == Direction.LEFT){
            robot.LFMotor.setPower(-power);
            robot.RFMotor.setPower(power);
            robot.LBMotor.setPower(power);
            robot.RBMotor.setPower(-power);
            sleep((int)time * 1000);
            stopMotors();
        }else if(direction == Direction.RIGHT){
            robot.LFMotor.setPower(power);
            robot.RFMotor.setPower(-power);
            robot.LBMotor.setPower(-power);
            robot.RBMotor.setPower(power);
            sleep((int)time * 1000);
            stopMotors();
        }else{
            //Throw an exception about the wrong side
        }
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

    private void turn(Direction dir, double pow, double time){
        if(dir == Direction.LEFT){
            robot.RFMotor.setPower(pow);
            robot.RBMotor.setPower(pow);
            robot.LFMotor.setPower(-pow);
            robot.LBMotor.setPower(-pow);
            sleep((int)time*1000);
        }else if(dir == Direction.RIGHT){
            robot.RFMotor.setPower(-pow);
            robot.RBMotor.setPower(-pow);
            robot.LFMotor.setPower(pow);
            robot.LBMotor.setPower(pow);
            sleep((int)time*1000);
        }else{
            //Error
        }
        stopMotors();
    }
}
