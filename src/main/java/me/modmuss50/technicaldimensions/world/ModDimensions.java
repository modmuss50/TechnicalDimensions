package me.modmuss50.technicaldimensions.world;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class ModDimensions {


    //Create new ones
    //Save to a file to be loaded at world start
    //Unload old dims

    //List of our dims with dim ids
    public static HashMap<Integer, DimData> dimDataHashMap = new HashMap<>();
    public static List<DimData> dimDataList = new ArrayList<>();

    private static final String fileName = "TechnicalDimensions-dimData.json";


    public static void worldLoad(FMLServerStartedEvent serverStartedEvent) throws IOException {
        File worldFile = new File(FMLCommonHandler.instance().getSavesDirectory(), FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName());
        File dataFile = new File(worldFile, fileName);

        if (!dataFile.exists()) {
            return;
        }

        String json = FileUtils.readFileToString(dataFile);
        Gson gson = new Gson();
        Type typeOfHashMap = new TypeToken<List<DimData>>() {
        }.getType();
        List<DimData> tempDataList = gson.fromJson(json, typeOfHashMap);
        for (DimData data : tempDataList) {
            DimensionType type = DimensionType.register("technicaldimensions", data.name, data.id, CustomWorldProvider.class, false);
            DimensionManager.registerDimension(data.id, type);
            data.type = type;
            dimDataHashMap.put(data.id, data);
            dimDataList.add(data);
        }
    }

    public static void unloadAll(FMLServerStoppingEvent serverStoppingEvent) {
        File worldFile = new File(FMLCommonHandler.instance().getSavesDirectory(), FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName());
        File dataFile = new File(worldFile, fileName);

        if (dimDataList == null || dimDataList.isEmpty()) {
            return;
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(dimDataList);
        try {
            if (dataFile.exists()) {
                dataFile.delete();
            }
            FileUtils.writeStringToFile(dataFile, json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (DimData data : dimDataList) {
            DimensionManager.unloadWorld(data.id);
            DimensionManager.unregisterDimension(data.id);
        }
        dimDataList.clear();
        dimDataHashMap.clear();
    }

    public static int createOrGetDim(DimData dimData) {
        for (DimData data : dimDataList) {
            if (data.name.equals(dimData)) {
                return data.id;
            }
        }
        return createDim(dimData);
    }

    public static int createDim(DimData dimData) {
        int dimId = DimensionManager.getNextFreeDimId();
        DimensionType type = DimensionType.register("technicaldimensions", dimData.name, dimId, CustomWorldProvider.class, false);
        dimData.type = type;
        dimData.id = dimId;
        Random random = new Random();
        dimData.seed = random.nextLong();
        DimensionManager.registerDimension(dimId, type);
        dimDataHashMap.put(dimId, dimData);
        dimDataList.add(dimData);

        return dimId;
    }

    public static DimensionType getDimTypeFromID(int id) {
        if (!dimDataHashMap.containsKey(id)) {
            return DimensionType.OVERWORLD; //VERY BAD THINGS HAVE HAPPENED
        }
        return dimDataHashMap.get(id).type;
    }

    public static DimData getDimDataFromID(int id) {
        if (dimDataHashMap.containsKey(id)) {
            return dimDataHashMap.get(id);
        }
        return null;
    }


    public static String getNextDimName() {
        return "Dim" + (dimDataList.size() + 1);
    }

}
