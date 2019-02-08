package Team7159.OpModes.RoverRuckus;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Team7159.ComplexRobots.VacuumBotV2;
import Team7159.Enums.Direction;

@Autonomous(name = "DownAuto")
public class DownAuto extends LinearOpMode {

    VacuumBotV2 robot = new VacuumBotV2();

    @Override
    public void runOpMode(){

        robot.init(hardwareMap);

        waitForStart();

       robot.strafe(Direction.LEFT, 25);
       sleep(500);
       robot.strafe(Direction.RIGHT, 25);

    }

    private void strafe(Direction direction, double power, double time){
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
        }
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

    private void stopMotors(){
        robot.stop();
    }
}
