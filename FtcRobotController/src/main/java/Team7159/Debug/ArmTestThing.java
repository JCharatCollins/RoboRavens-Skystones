package Team7159.Debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Team7159.ComplexRobots.ArmTest;

@TeleOp(name = "armtest")
@Disabled
public class ArmTestThing extends LinearOpMode {

    ArmTest robot = new ArmTest();

    int up = 0;
    boolean fast = true;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        while(opModeIsActive()){

            if(gamepad1.left_bumper){
                fast = true;
            }else if(gamepad1.right_bumper){
                fast = false;
            }

            if(gamepad1.y){
                up = 1;
            } else
            if(gamepad1.a){
                up = 2;
            }else{
                up = 0;
            }

            if(up == 0){
                robot.armMotor.setPower(0);
            }else if(up == 1){
                robot.armMotor.setPower(fast?1:0.3);
            }else if(up == 2){
                robot.armMotor.setPower(fast?-1:-0.3);
            }

        }

    }
}