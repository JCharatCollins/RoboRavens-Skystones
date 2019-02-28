package Team7159.Utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.ArrayList;

/**
 * Created by William on 10/13/2016.
 * This is a custom class I made to make it easier to group motors
 */

public class MotorGroup {

    boolean reversed;

    ArrayList<DcMotor> dcMotors;

    //Creates a MotorGroup with the motors

    public MotorGroup(DcMotor... toAdd) {
        dcMotors = new ArrayList<>();
        for (DcMotor motor : toAdd) {
            dcMotors.add(motor);
        }
    }

    //In case you forgot to put motors in the initialization

    public void addMotor(DcMotor... toAdd){
        dcMotors = new ArrayList<>();
        for(DcMotor motor: toAdd){
            dcMotors.add(motor);
        }
    }

    //Reverses the motors

    public void reverse(){
        if(reversed){
            for (DcMotor motor : dcMotors){
                motor.setDirection(DcMotorSimple.Direction.FORWARD);
            }
        }else{
            for (DcMotor motor : dcMotors){
                motor.setDirection(DcMotorSimple.Direction.REVERSE);
            }
        }
        reversed = true;
    }

    //Checks if the motors are reversed

    public boolean isReversed(){
        if(dcMotors.get(0).getDirection().equals(DcMotorSimple.Direction.REVERSE)){
            return true;
        }else{
            return false;
        }
    }

    public void AddMotors(DcMotor... motors) {
        for (DcMotor motor : motors) {
            dcMotors.add(motor);
        }
    }

    //Resets encoders, hard reset

    public void resetEncoders() {
        for(DcMotor motor: dcMotors){
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    //Sets motor powers to 0, soft reset

    public void stop() {
        setPowers(0);
    }

    //Prints Motors, just a debug thing

    public String PrintMotors() {
        String motors = "";
        for (int i = 0; i < dcMotors.size(); i++) {
            motors = motors + String.valueOf(dcMotors.get(i));
        }
        return motors;
    }

    //Sets the mode of the motors

    public void setMode(DcMotor.RunMode Mode) {
        for(DcMotor motor: dcMotors){
            motor.setMode(Mode);
        }
    }

    //Checks if busy for all motors

    public boolean isBusy() {
        return dcMotors.get(0).isBusy();
    }

    //Gets position

    public int getPosition(){
        return dcMotors.get(0).getCurrentPosition();
    }

    //Sets the target position for each of the motors

    public void setTargetPosition(int position) {
        for(DcMotor motor: dcMotors){
            motor.setTargetPosition(position);
        }
    }

    //Sets motor powers

    public void setPowers(double power) {
        for(DcMotor motor: dcMotors){
            motor.setPower(power);
        }
    }
}