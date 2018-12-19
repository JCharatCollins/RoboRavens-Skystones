package Team7159.Debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by WILLIAM LIN on 10/19/2017 for the Android Robot Controller.
 * THIS IS PROBABLY A USEFUL CLASS
 */

@Disabled
@Autonomous(name ="VuforiaBased Opmode")
public class VumarkSkeleton extends LinearOpMode {

    RelicRecoveryVuMark vuMark;
    VuforiaLocalizer.Parameters parameters;
    VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() throws InterruptedException {
        parameters = new VuforiaLocalizer.Parameters(com.qualcomm.ftcrobotcontroller.R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AVktbtv/////AAAAGY1koTqeTUyKsH17S4sxg5FdzjlL4sab4r1TteImHLQZaxsQP96TVimg0LSECJMSTY" +
                "/hMmyl4Ko8WqEFHdESFWl5CNgqDIkVJsLD4ivpj1OAwtHu6z1Me1lnhV/DlBshYL9nsfqWCvVyPPpMkYBj3DRGGI" +
                "6OHwD29CokKIxnknH8sV/k8xdVFSAmsRqBney+t4+c7vmUw39q7qrsE63Pf6wnFxYLkDz4uFvjy3IHbX3/OLojTN" +
                "Gk4/sHOWnME0c8EEVXUZAoXPM/7jJK/ksBrYMPyJTZOeMPhcTMtjsPNMVx54p5yICLcIGjqPwTih1Z88RGDGKIuY" +
                "vIrnSMjUnJNZtshuuqadeAXk2HyGS6DR3K";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 1);

        VuforiaTrackables Marks = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable marker = Marks.get(0);


        waitForStart();

        Marks.activate();

        vuMark = RelicRecoveryVuMark.from(marker);
        while(vuMark == null || vuMark == RelicRecoveryVuMark.UNKNOWN){
            //Do some movement or adjustment to where it would be more likely to find it
            vuMark = RelicRecoveryVuMark.from(marker);
        }


        while(opModeIsActive()){
            //Do some stuff


            if(vuMark==RelicRecoveryVuMark.CENTER){
                //It's on the middle
            }else if(vuMark == RelicRecoveryVuMark.LEFT){
                //Its on the left
            }else if(vuMark == RelicRecoveryVuMark.RIGHT){
                //It's on the right
            }
        }


    }

}
