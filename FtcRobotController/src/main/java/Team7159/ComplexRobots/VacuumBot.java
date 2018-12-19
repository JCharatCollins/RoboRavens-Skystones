package Team7159.ComplexRobots;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import Team7159.BasicRobots.BasicMecanum;
import Team7159.BasicRobots.BasicTank;
import Team7159.Utils.RobotComp;

public class VacuumBot extends BasicMecanum {

    HardwareMap HW;

    public ColorSensor AdafruitSensor;

    public Servo RServo;
    public Servo LServo;

    RobotComp Comp = new RobotComp();

    public DcMotor AMotor;

    public void init(HardwareMap Map){

        super.init(Map);

        RServo = Map.servo.get("RS");
        LServo = Map.servo.get("LS");

        AdafruitSensor = Map.colorSensor.get("cSensor");


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