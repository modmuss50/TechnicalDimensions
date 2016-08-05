package me.modmuss50.technicaldimensions.world;

import net.minecraft.world.DimensionType;

public class DimData {

    public String name;

    public DimData(String name) {
        this.name = name;
    }

    public transient DimensionType type;

    public int id;

}
