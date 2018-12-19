package Team7159.LegacyRobots;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by WILLIAM LIN on 10/26/2017 for the Panabot going to Panama
 * This is a simple robot with a left and right motor for movement and a motor to shoot
 */

public class panabot {

    public DcMotor left;
    public DcMotor right;

    public DcMotor shooter;

    public void init(HardwareMap Map){

        left = Map.dcMotor.get("LF");
        right = Map.dcMotor.get("RF");

        shooter = Map.dcMotor.get("shoot");

        left.setPower(0);
        right.setPower(0);

        shooter.setPower(0);

        left.setDirection(DcMotorSimple.Direction.REVERSE);

        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }
}