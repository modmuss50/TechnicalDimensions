package me.modmuss50.technicaldimensions.server;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Mark on 04/08/2016.
 */
public class ServerScreenShotUtils {

    public static BufferedImage image;

    public static ScreenShotSaver data = new ScreenShotSaver();

    public void registerScreenShot(String imageID, byte[] imageData){
        if(data.screenshots == null){
            data.screenshots = new ArrayList<>();
        }
        data.screenshots.add(new ScreenShotSaver.ScreenShotData(imageID, imageData));
    }

    public byte[] getScreenshot(String imageID){
        if(data.screenshots == null){
            return null;
        }
        for(ScreenShotSaver.ScreenShotData shot : data.screenshots){
            if(shot.imageID.equals(imageID)){
                return shot.imageData;
            }
        }
        return null;
    }

    public static void saveScreenShotsToFile(){

    }


}
