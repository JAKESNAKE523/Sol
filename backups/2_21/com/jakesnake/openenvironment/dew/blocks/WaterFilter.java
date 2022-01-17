package com.jakesnake.openenvironment.dew.blocks;

import com.jakesnake.openenvironment.dew.Dew;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.rmi.registry.Registry;
import java.util.Random;

public class WaterFilter extends Block implements BlockEntityProvider {
    /* Properties
        0 - Empty
        1 - Top Filled
        2 - Top Filled & lit
        3 - Bottom Filled
     */



    public static final IntProperty FILL_STATE = IntProperty.of("fill_state",0, 3);

    public WaterFilter(Settings settings) {
        super(settings.nonOpaque());
        setDefaultState(getStateManager().getDefaultState().with(FILL_STATE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(FILL_STATE);
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        player.shouldCancelInteraction();

        String bottleType = player.getStackInHand(hand).getOrCreateTag().get("Potion") != null ? player.getStackInHand(hand).getOrCreateTag().get("Potion").asString() : "";
        switch (state.get(FILL_STATE)){
            case 0:
                if(player.getStackInHand(hand).getItem() != null && player.getStackInHand(hand).getItem() == Items.POTION && player.getStackInHand(hand).getOrCreateTag().get("Potion").asString().equals("minecraft:water")){
                    player.getStackInHand(hand).setCount(player.getStackInHand(hand).getCount()-1);
                    player.giveItemStack(new ItemStack(Items.GLASS_BOTTLE, 1));
                    world.setBlockState(pos, state.with(FILL_STATE, 1));
                }
                break;
            case 1:
                if(player.getStackInHand(hand).getItem() != null && player.getStackInHand(hand).getItem() == Items.FLINT_AND_STEEL){
                    world.setBlockState(pos, state.with(FILL_STATE, 2));
                    player.getStackInHand(hand).damage(1, player, livingEntity -> {

                    });
                } else if (player.getStackInHand(hand).getItem() == Items.GLASS_BOTTLE) {
                    world.setBlockState(pos, state.with(FILL_STATE, 0));
                    player.getStackInHand(hand).setCount(player.getStackInHand(hand).getCount()-1);
                    ItemStack water = new ItemStack(Items.POTION, 1);
                    water.getOrCreateTag().putString("Potion", "minecraft:water");
                    player.giveItemStack(water);
                }
                break;
            case 3:
                if(player.getStackInHand(hand).getItem() != null && player.getStackInHand(hand).getItem() == Items.GLASS_BOTTLE){
                    player.getStackInHand(hand).setCount(player.getStackInHand(hand).getCount()-1);
                    player.giveItemStack(new ItemStack(Dew.PURIFIED_WATER, 1));
                    world.setBlockState(pos, state.with(FILL_STATE, 0));
                }
                break;
            default:
                break;

        }
        return ActionResult.SUCCESS;
    }


    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new WaterFilterEntity();
    }

}
