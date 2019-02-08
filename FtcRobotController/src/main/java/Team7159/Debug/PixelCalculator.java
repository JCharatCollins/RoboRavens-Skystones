package Team7159.Debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import Team7159.ComplexRobots.VacuumBotV2;
import Team7159.Enums.Side;

import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_GOLD_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_SILVER_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.TFOD_MODEL_ASSET;

@TeleOp(name="PixelCalculator")
public class PixelCalculator extends LinearOpMode {
    private static final String VUFORIA_KEY = "AQ8rpDD/////AAABmRNIMKzPaEhBgamlRTL2RRMKI6zA+IW8Qqd6l0v65fwa8N2l" +
            "n17xMthqidBc7uWyTNA1pYUodjK8ngEvudjz4FeoJbQpXxwYm2/H5XXwlWywZfUHa74lGuma90fRmTuEeFwAsDTZ4JfXojf719" +
            "wTliDCdlKCOkQuuvU0Cx0JyzdYT/NnOYZWroHx2maby73wQW1T76aSlKsHE/cZ1FmVoOokP+HqIfaOPpUR/gVkDgqyB7XAOaBd" +
            "kHzY3FOv7oCC7Ybn7jbAdVuJX8uow08atIH0dwmS/LC6BqpakDGFNj4JAyyd9cnz43UWBMEz6cTBk9Um2+a/5XLfV+W7RaHDEF" +
            "fs726qLAIagk9Nd2CzIg/x";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;

    Side side;

    @Override
    public void runOpMode(){

        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        waitForStart();


        if (tfod != null) {
            tfod.activate();
        }



        while (opModeIsActive()) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() >= 1) {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getTop();
                                telemetry.addData("recog percent",recognition.getConfidence());
                            } else if (silverMineral1X == -1) {
                                telemetry.addData("recog percent",recognition.getConfidence());
                                silverMineral1X = (int) recognition.getLeft();
                            } else {
                                telemetry.addData("recog percent",recognition.getConfidence());
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }
                        telemetry.addData("Pixel value: ", goldMineralX);
                    }
                    telemetry.update();
                }
            }
        }
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


}
