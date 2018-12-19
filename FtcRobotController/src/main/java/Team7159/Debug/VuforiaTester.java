package Team7159.Debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by William on 10/4/2016.
 * JK this is new haha 9/14/2017
 */
@Disabled
@TeleOp(name = "VuforiaTest")
public class VuforiaTester extends LinearOpMode {
    VuforiaLocalizer vuforia;
    VuforiaLocalizer.Parameters parameters;


    @Override
    public void runOpMode() throws InterruptedException {
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
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);

        VuforiaTrackables Images = this.vuforia.loadTrackablesFromAsset("RelicVuMark");

        VuforiaTrackable template = Images.get(0);

        waitForStart();
        Images.activate();


        while (opModeIsActive()) {
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(template);
            OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) template.getListener()).getPose();
            if(pose!= null) {
                if(vuMark.equals(RelicRecoveryVuMark.CENTER)){
                    telemetry.addData("thing ", "center go fuck yourself");
                }
                if(vuMark.equals(RelicRecoveryVuMark.LEFT)){
                    telemetry.addData("thing ", "left go fuck yourself");
                }
                if(vuMark.equals(RelicRecoveryVuMark.RIGHT)){
                    telemetry.addData("thing ", "right go fuck yourself");
                }
                telemetry.update();
//                VectorF translation = pose.getTranslation();
//                double distance = Math.sqrt(Math.pow(translation.get(0), 2) + Math.pow(translation.get(1), 2) + Math.pow(translation.get(2), 2));
            }
        }
    }
}