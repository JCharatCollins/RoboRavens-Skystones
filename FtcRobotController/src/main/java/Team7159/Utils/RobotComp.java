package Team7159.Utils;

import Team7159.Enums.Direction;
import Team7159.Enums.Version;

/**
 * Does math for the robot
 * Created by William on 10/24/2016.
 */

public class RobotComp {
    private double position;

    //Computes a position. Takes a distance in meters and converts it to position

    public int computePositionD(double distance, Version version)
    {
        if(version.equals(Version.ONE)){
            return (int)(distance*(1120/(Math.PI* RobotMath.toMeters(4))));
        }else if(version.equals(Version.TWO)){
            return (int)(distance * (1120/(Math.PI* RobotMath.toMeters(4))));
        }else{
            return 0;
        }
    }

    //degrees indicates degrees to turn
    //side indicates the side the motors are on, 0 is left, 1 is right
    //direction indicates the direction we turn, 0 is left, 1 is right

    public int computeTurningPos(Direction direction, int degrees, Direction side, double wDistance, Version version) {
        double Circumference = wDistance * Math.PI;
        position = version.equals(Version.ONE)?
                computePositionD(RobotMath.toMeters(Circumference * RobotMath.toRadians(degrees) / (2 * Math.PI)), Version.ONE):
                computePositionD(RobotMath.toMeters(Circumference * RobotMath.toRadians(degrees) / (2 * Math.PI)),Version.TWO);
        if (direction.equals(Direction.RIGHT)) {
            if (side.equals(Direction.RIGHT)) {
                return -(int) position;
            } else {
                return (int) position;
            }
        } else {
            if (side.equals(Direction.RIGHT)) {
                return (int) position;
            } else {
                return -(int) position;
            }
        }
    }
}