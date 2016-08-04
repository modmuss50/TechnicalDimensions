package me.modmuss50.technicaldimensions.server;

import java.util.List;

/**
 * Created by Mark on 04/08/2016.
 */
public class ScreenShotSaver {

    List<ScreenShotData> screenshots;

    public static class ScreenShotData {
        public String imageID;
        public String imageData;

        public ScreenShotData(String imageID, String imageData) {
            this.imageID = imageID;
            this.imageData = imageData;
        }
    }

}
