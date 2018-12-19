package Team7159.Utils;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;

import Team7159.Enums.Colors;

/**
 * Created by William on 12/9/2016.
 * Mainly used in accordance with the Adafruit Sensors.
 * Helpful for managing stuff.
 */

public class ColorManip {

    ColorSensor adafruit;

    public ColorManip(ColorSensor AdafruitSensor){
        adafruit = AdafruitSensor;
    }

    public static int getRed(ColorSensor AdafruitSensor){
        return AdafruitSensor.red();
    }

    public int getRed(){
        return adafruit.red();
    }

    public static int getBlue(ColorSensor AdafruitSensor){
        return AdafruitSensor.blue();
    }

    public int getBlue(){
        return adafruit.blue();
    }

    public static int getGreen(ColorSensor AdafruitSensor){
        return AdafruitSensor.green();
    }

    public int getGreen(){
        return adafruit.green();
    }

    public static float[] getHSV(ColorSensor AdafruitSensor){
        float[] hsvValues = new float[3];
        Color.RGBToHSV((AdafruitSensor.red() * 255) / 800, (AdafruitSensor.green() * 255) / 800, (AdafruitSensor.blue() * 255) / 800, hsvValues);
        return hsvValues;
    }

    public float[] getHSV(){
        float[] hsvValues = new float[3];
        Color.RGBToHSV((adafruit.red() * 255) / 800, (adafruit.green() * 255) / 800, (adafruit.blue() * 255) / 800, hsvValues);
        return hsvValues;
    }

    public static Colors getColor(float[] HSVValues){
        if((HSVValues[0] >20 && HSVValues[0] < 50)||(HSVValues[0] > 340 && HSVValues[0] < 360)){
       //     if(HSVValues[1] > 30 && HSVValues[2] > 70){
            return Colors.RED;
         //   }
        }else if(HSVValues[0] >200 && HSVValues[0] < 250){
       //     if(HSVValues[1] > 30 && HSVValues[2] > 70){
            return Colors.BLUE;
        //    }
        }
        return Colors.IRRELEVANT;
    }
}