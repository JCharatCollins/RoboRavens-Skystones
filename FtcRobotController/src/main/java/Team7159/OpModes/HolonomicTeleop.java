package Team7159.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Team7159.BasicRobots.BasicHolonomic;

/**
 * Created by william on 8/31/2017.
 * This is a holonomic drive
 */

@TeleOp(name = "HolonomicTele")
@Disabled
public class HolonomicTeleop extends LinearOpMode {
    BasicHolonomic Robot = new BasicHolonomic();

    @Override
    public void runOpMode(){
        Robot.init(hardwareMap);

        waitForStart();

        while(opModeIsActive()) {

            double rF = -gamepad1.left_stick_x - gamepad1.left_stick_y + gamepad1.right_stick_x;
            double rB = gamepad1.left_stick_x - gamepad1.left_stick_y + gamepad1.right_stick_x;
            double lF = gamepad1.left_stick_x + gamepad1.left_stick_y + gamepad1.right_stick_x;
            double lB = -gamepad1.left_stick_x + gamepad1.left_stick_y + gamepad1.right_stick_x;

            double maxLeft = Math.max(Math.abs(lF), Math.abs(lB));
            double maxRight = Math.max(Math.abs(rF), Math.abs(rB));
            double max = Math.max(Math.abs(maxLeft), Math.abs(maxRight));
            if(max != 0) {
                rF /= max;
                rB /= max;
                lF /= max;
                lB /= max;
            }
            if (Math.abs(rF) < 0.4) rF = 0;
            if (Math.abs(rB) < 0.4) rB = 0;
            if (Math.abs(lF) < 0.4) lF = 0;
            if (Math.abs(lB) < 0.4) lB = 0;

            Robot.RFMotor.setPower(rF);
            Robot.RBMotor.setPower(rB);
            Robot.LFMotor.setPower(lF);
            Robot.LBMotor.setPower(lB);
        }
    }
}