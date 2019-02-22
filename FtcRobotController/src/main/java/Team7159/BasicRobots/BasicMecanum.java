package Team7159.BasicRobots;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import Team7159.Enums.Direction;
import Team7159.Enums.Version;
import Team7159.Utils.MotorGroup;
import Team7159.Utils.RobotComp;
import Team7159.Utils.RobotMath;

public class BasicMecanum {

    public RobotComp Comp = new RobotComp();

    MotorGroup Left;
    MotorGroup Right;
    double strafeConstant= 7/6;
    public DcMotor LFMotor;
    public DcMotor RFMotor;
    public DcMotor LBMotor;
    public DcMotor RBMotor;

    public void init(HardwareMap Map) {

        LFMotor = Map.dcMotor.get("LF");
        RFMotor = Map.dcMotor.get("RF");
        LBMotor = Map.dcMotor.get("LB");
        RBMotor = Map.dcMotor.get("RB");

        LFMotor.setPower(0);
        RFMotor.setPower(0);
        LBMotor.setPower(0);
        RBMotor.setPower(0);

        LFMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        LBMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        LFMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RFMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LBMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RBMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        LFMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RFMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LBMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RBMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Left = new MotorGroup();
        Right = new MotorGroup();
        Left.AddMotors(LFMotor,LBMotor);
        Right.AddMotors(RFMotor,RBMotor);
    }


    public void moveStraight(double power) {
        LFMotor.setPower(power);
        RFMotor.setPower(power);
        LBMotor.setPower(power);
        RBMotor.setPower(power);
    }

    public void stop() {
        LFMotor.setPower(0);
        RFMotor.setPower(0);
        LBMotor.setPower(0);
        RBMotor.setPower(0);
    }

    public void turn(Direction direction, int degrees) {
        Right.resetEncoders();
        Left.resetEncoders();
        int LeftDistance = Comp.computeTurningPos(direction, degrees, Direction.LEFT, 26.5, Version.TWO);
        int RightDistance = Comp.computeTurningPos(direction, degrees, Direction.RIGHT, 26.5, Version.TWO);
        Left.setTargetPosition(LeftDistance);
        Right.setTargetPosition(RightDistance);
        Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        moveStraight(1);
        while (Left.isBusy() && Right.isBusy()) {
        }
        stop();
    }

    public void driveDir(Direction direction, double distance) {
        Right.resetEncoders();
        Left.resetEncoders();
        Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int pos = Comp.computePositionD(RobotMath.toMeters(distance), Version.TWO);
        switch (direction) {
            case BACKWARDS:
             //   pos = -pos;
                Right.setTargetPosition(-pos);
                Left.setTargetPosition(-pos);
                break;
            case FORWARDS:
                Right.setTargetPosition(pos);
                Left.setTargetPosition(pos);
                break;
        }
       moveStraight(1);
        while (Right.isBusy() && Left.isBusy()) {
        }
        stop();
    }
    public void strafe(Direction direction, int distance)
    {

        Right.resetEncoders();
        Left.resetEncoders();
        Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        double pos = (double)Comp.computePositionD(RobotMath.toMeters(distance), Version.TWO)*7/6;

        switch (direction) {
            case LEFT:

                LFMotor.setTargetPosition((int)-pos);
                LBMotor.setTargetPosition((int)pos);
                RFMotor.setTargetPosition((int)pos);
                RBMotor.setTargetPosition((int)-pos);
                break;
            case RIGHT:
                LFMotor.setTargetPosition((int)pos);
                LBMotor.setTargetPosition((int)-pos);
                RFMotor.setTargetPosition((int)-pos);
                RBMotor.setTargetPosition((int)pos);
                break;
        }
        moveStraight(0.7);

        while (Right.isBusy() && Left.isBusy()) {

        }
        stop();

    }

    public void strafe(Direction direction, double power, double time) throws InterruptedException{

        if(direction == Direction.LEFT){
            LFMotor.setPower(-power);
            RFMotor.setPower(power);
            LBMotor.setPower(power);
            RBMotor.setPower(-power);
            wait((int)time * 1000);
            stop();
        }else if(direction == Direction.RIGHT){
            LFMotor.setPower(power);
            RFMotor.setPower(-power);
            LBMotor.setPower(-power);
            RBMotor.setPower(power);
            wait((int)time * 1000);
            stop();
        }else{
            //Throw an exception about the wrong side
        }
    }
}