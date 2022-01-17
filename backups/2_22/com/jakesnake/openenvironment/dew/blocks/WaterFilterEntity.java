package com.jakesnake.openenvironment.dew.blocks;

import com.jakesnake.openenvironment.dew.Dew;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Tickable;

public class WaterFilterEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {
    private int ticks;

    public static final IntProperty FILL_STATE = IntProperty.of("fill_state",0, 3);

    public WaterFilterEntity() {
        super(Dew.WATER_FILTER_ENTITY);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag){
        super.toTag(tag);
        tag.putInt("ticks", ticks);
        return tag;
    }
    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        ticks = tag.getInt("ticks");
    }

    @Override
    public void tick() {
        int state = this.getWorld().getBlockState(this.getPos()).get(WaterFilter.FILL_STATE);

        if(state == 2) ticks++;
        if(state == 2 && ticks > 900) {
            this.getWorld().setBlockState(this.getPos(), this.getWorld().getBlockState(this.getPos()).with(WaterFilter.FILL_STATE, 3));
            ticks = 0;
        }
        this.markDirty();
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        this.fromTag(this.getCachedState(), compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        return toTag(compoundTag);
    }

}
