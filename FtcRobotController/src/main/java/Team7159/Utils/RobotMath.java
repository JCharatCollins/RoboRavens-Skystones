package Team7159.Utils;

/**
 * Created by William on 11/23/2016.
 */

public class RobotMath {
    //To Radians

    public static double toRadians(int degrees)
    {
        return degrees*Math.PI/180.0;
    }

    //This converts inches to meters, helpful for computePosition

    public static double toMeters(double inches)
    {
        return inches*0.0254;
    }

    //Converts angle to a value between 0 and 359

    public int convertAngle(int angle)
    {
        return ((angle%360)+360)%360;
    }

    //Converts from distance given in Vuforia to inches

    public double convertVDistance(int Distance){
        return Distance/20.0;
    }



}
