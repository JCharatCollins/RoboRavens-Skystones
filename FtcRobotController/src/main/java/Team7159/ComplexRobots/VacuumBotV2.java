package Team7159.ComplexRobots;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import Team7159.BasicRobots.BasicMecanum;

public class VacuumBotV2 extends BasicMecanum {

    //The motor controlling the chain lift
    public DcMotor liftMotor;

    //The motor controlling rotating the vacuum
    public DcMotor chainMotor;

    //The motor controlling the chain input of the vacuum
    public DcMotor vacuumMotor;

    //The left servo used for moving vacuum in and out
    public DcMotor intakeMotor;

    //The servo for moving the arm back into the lander
    public Servo liftServo;

    public void init(HardwareMap Map){

        super.init(Map);

        intakeMotor = Map.dcMotor.get("iM");
        liftMotor = Map.dcMotor.get("lM");
        chainMotor = Map.dcMotor.get("cM");
        vacuumMotor = Map.dcMotor.get("vM");

        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        vacuumMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        vacuumMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        liftServo = Map.servo.get("liftServo");
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
