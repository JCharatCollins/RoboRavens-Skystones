package Team7159.LegacyRobots;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import Team7159.BasicRobots.BasicTank;
import Team7159.Utils.RobotComp;

/**
 * Created by WILLIAM LIN on 10/26/2017 for the Relic Recovery FBar Robot, non optimized.
 * Legacy code, use only for example
 */

public class FBarRobot extends BasicTank {

    public RobotComp Comp;

    public Servo LAST;
    public Servo RAST;

    public Servo LASB;
    public Servo RASB;

    public DcMotor Winch;

    public DcMotor AMotor;

    public Servo AAST;
    public Servo AASB;

    public ColorSensor colorSensor;

    public Servo RA;

    public void init(HardwareMap Map){

        super.init4Drive(Map);

        Comp = new RobotComp();

        AMotor = Map.dcMotor.get("Arm");

        Winch = Map.dcMotor.get("Winch");

        LAST = Map.servo.get("LAST");
        RAST = Map.servo.get("RAST");
        LASB = Map.servo.get("LASB");
        RASB = Map.servo.get("RASB");

        AAST = Map.servo.get("AAST");
        AASB = Map.servo.get("AASB");

        RA = Map.servo.get("RA");

        colorSensor = Map.colorSensor.get("colorSensor");

        AMotor.setPower(0);

        Winch.setPower(0);

        LAST.setDirection(Servo.Direction.FORWARD);
        LASB.setDirection(Servo.Direction.FORWARD);
        RAST.setDirection(Servo.Direction.REVERSE);
        RASB.setDirection(Servo.Direction.REVERSE);

        AMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        AMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

//    public void turn(Direction direction, int degrees, MotorGroup Right, MotorGroup Left){
//        Right.resetEncoders();
//        Left.resetEncoders();
//        int LeftDistance = Comp.computeTurningPos(direction, degrees, Direction.LEFT, 26.5, Version.TWO);
//        int RightDistance = Comp.computeTurningPos(direction, degrees, Direction.RIGHT, 26.5, Version.TWO);
//        Left.setTargetPosition(LeftDistance);
//        Right.setTargetPosition(RightDistance);
//        Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        Left.setPowers(0.5);
//        Right.setPowers(0.5);
//        while(Left.isBusy()&&Right.isBusy()){}
//        Left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        Right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        Left.setPowers(0);
//        Right.setPowers(0);
//    }
//
//    public void driveDir(Direction direction, double distance, MotorGroup Right, MotorGroup Left){
//        Right.resetEncoders();
//        Left.resetEncoders();
//        Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        switch(direction){
//            case FORWARDS:
//                int pos = -Comp.computePositionD(RobotMath.toMeters(distance), Version.TWO);
//                Right.setTargetPosition(pos);
//                Left.setTargetPosition(pos);
//                break;
//            case BACKWARDS:
//                pos = Comp.computePositionD(RobotMath.toMeters(distance), Version.TWO);
//                Right.setTargetPosition(pos);
//                Left.setTargetPosition(pos);
//                break;
//        }
//        Right.setPowers(0.5);
//        Left.setPowers(0.5);
//        while(Right.isBusy()&&Left.isBusy()){}
//        Right.setPowers(0);
//        Left.setPowers(0);
//        Right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        Left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//    }

}
