package Team7159.LegacyRobots;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import Team7159.Enums.Direction;
import Team7159.Enums.Version;
import Team7159.Utils.MotorGroup;
import Team7159.Utils.RobotComp;
import Team7159.Utils.RobotMath;


/**
 * Created by William on 12/13/2016.
 * Legacy code, use only as an example
 */



public class MecanumV2 {

    HardwareMap HW;

    public ColorSensor AdafruitSensor;

    public DcMotor RFMotor;
    public DcMotor RBMotor;
    public DcMotor LFMotor;
    public DcMotor LBMotor;

    public Servo RServo;
    public Servo LServo;

    RobotComp Comp = new RobotComp();

    public void Stop(MotorGroup Right, MotorGroup Left){
        Right.stop();
        Left.stop();
    }

    public void strafe(Direction direction, DcMotor RF, DcMotor RB, DcMotor LF, DcMotor LB){
        //Write in to strafe at degrees to right or left.
    }

    public void turn(Direction direction, int degrees, MotorGroup Right, MotorGroup Left){
        Right.resetEncoders();
        Left.resetEncoders();
        int LeftDistance = Comp.computeTurningPos(direction, degrees,Direction.LEFT, 19.0, Version.TWO);
        int RightDistance = Comp.computeTurningPos(direction, degrees,Direction.RIGHT, 19.0, Version.TWO);
        Left.setTargetPosition(LeftDistance);
        Right.setTargetPosition(RightDistance);
        Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Left.setPowers(0.5);
        Right.setPowers(0.5);
        while(Left.isBusy()&&Right.isBusy()){
        }
        Left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Left.setPowers(0);
        Right.setPowers(0);
    }

    public void init(HardwareMap Map){
        HW = Map;

        AdafruitSensor = HW.colorSensor.get("Adafruit_Sensor");

        RFMotor = HW.dcMotor.get("Right_Front_Motor");
        RBMotor = HW.dcMotor.get("Right_Back_Motor");
        LFMotor = HW.dcMotor.get("Left_Front_Motor");
        LBMotor = HW.dcMotor.get("Left_Back_Motor");

        RServo = HW.servo.get("Right_Servo");
        LServo = HW.servo.get("Left_Servo");

        RFMotor.setPower(0);
        RBMotor.setPower(0);
        LFMotor.setPower(0);
        LBMotor.setPower(0);

        RFMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RBMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LFMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LBMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        RFMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RBMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //1 side will be reversed, we are assuming that it is the right side

//        L1Motor.setDirection(DcMotorSimple.Side.REVERSE);
//        L2Motor.setDirection(DcMotorSimple.Side.REVERSE);
    }

    //Drives Forwards or Backwards

    public void driveDir(Direction direction, double distance, MotorGroup Right, MotorGroup Left){
        Right.resetEncoders();
        Left.resetEncoders();
        Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        switch(direction){
            case FORWARDS:
                Right.setTargetPosition(Comp.computePositionD(RobotMath.toMeters(distance),Version.TWO));
                Left.setTargetPosition(Comp.computePositionD(RobotMath.toMeters(distance), Version.TWO));
                break;
            case BACKWARDS:
                Right.setTargetPosition(-Comp.computePositionD(RobotMath.toMeters(distance), Version.TWO));
                Left.setTargetPosition(-Comp.computePositionD(RobotMath.toMeters(distance), Version.TWO));
                break;
        }
        Right.setPowers(0.3);
        Left.setPowers(0.3);
        while(Right.isBusy()&&Left.isBusy()){}
        Right.setPowers(0);
        Left.setPowers(0);
        Right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    private ElapsedTime period = new ElapsedTime();

    public void waitForTick(long periodMs) throws InterruptedException {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }

}
