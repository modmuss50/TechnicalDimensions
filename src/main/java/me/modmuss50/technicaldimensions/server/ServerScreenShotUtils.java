package me.modmuss50.technicaldimensions.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FileUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mark on 04/08/2016.
 */
public class ServerScreenShotUtils {

    public static BufferedImage image;

    public static ScreenShotSaver data = new ScreenShotSaver();

    public static final String savename = "TechnicalDimensions-Views.json";
    private static File file;
    private static World world;

    public static void registerScreenShot(String imageID, String imageData) {
        if (data.screenshots == null) {
            data.screenshots = new ArrayList<>();
        }
        if (getScreenshotData(imageID) == null) {
            data.screenshots.add(new ScreenShotSaver.ScreenShotData(imageID, imageData));
        }
        saveFile();
    }

    public static String getScreenshotData(String imageID) {
        if (data.screenshots == null || data.screenshots.isEmpty()) {
            return null;
        }
        for (ScreenShotSaver.ScreenShotData shot : data.screenshots) {
            if (shot.imageID.equals(imageID)) {
                return shot.imageData;
            }
        }
        return null;
    }


    //Call when anything is added to the database
    public static void saveFile() {
        if (file == null) {
            file = new File(world.getSaveHandler().getWorldDirectory(), savename);
        }
        if (data.screenshots == null || data.screenshots.isEmpty()) {
            return; //No need to save nothing
        }
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(data);
        try {
            if (file.exists()) {
                file.delete();
            }
            FileUtils.writeStringToFile(file, json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Call on mod load
    public static void load() {
        saveFile();
        if (file.exists()) {
            try {
                String json = FileUtils.readFileToString(file);
                Gson gson = new Gson();
                data = gson.fromJson(json, ScreenShotSaver.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load load) {
        if (load.getWorld().provider.getDimension() == 0) {
            world = load.getWorld();
            load();
        }
    }

    @SubscribeEvent
    public static void worldSave(WorldEvent.Save save) {
        if (save.getWorld().provider.getDimension() == 0) {
            saveFile();
        }
    }

    @SubscribeEvent
    public static void worldClose(WorldEvent.Unload unload) {
        if (unload.getWorld().provider.getDimension() == 0) {
            saveFile();
        }
    }


}
