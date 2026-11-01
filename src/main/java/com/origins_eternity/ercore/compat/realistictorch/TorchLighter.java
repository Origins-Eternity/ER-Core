package com.origins_eternity.ercore.compat.realistictorch;

import com.chaosthedude.realistictorches.blocks.BlockRealisticTorch;
import com.chaosthedude.realistictorches.blocks.RealisticTorchesBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.origins_eternity.ercore.config.Configuration.Compat;
import static com.origins_eternity.ercore.utils.Utils.stackMatched;

public class TorchLighter {
    @SubscribeEvent
    public static void onLivingEntityUseItem(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            World world = player.world;
            if (!world.isRemote) {
                int num = stackMatched(event.getItem(), Compat.lighters);
                if (num != -1) {
                    double reach = player.capabilities.isCreativeMode ? 6.0D : 5.0D;

                    Vec3d start = player.getPositionEyes(1.0F);
                    Vec3d end = start.add(player.getLook(1.0F).scale(reach));
                    RayTraceResult result = world.rayTraceBlocks(start, end, false, false, true);

                    if (result == null || result.typeOfHit != RayTraceResult.Type.BLOCK) return;
                    BlockPos pos = result.getBlockPos();
                    Block block = world.getBlockState(pos).getBlock();

                    if (block instanceof BlockRealisticTorch) {
                        BlockRealisticTorch torch = (BlockRealisticTorch) block;
                        if (!world.isRainingAt(pos) && !torch.isLit()) {
                            world.setBlockState(pos, torch.getState(world, pos, RealisticTorchesBlocks.torchLit), 2);
                            torch.playIgniteSound(world, pos);

                            int damage = Integer.parseInt(Compat.lighters[num].split(";")[1]);
                            if (damage > 0) {
                                event.getItem().damageItem(damage, player);
                            }
                        }
                    }
                }
            }
        }
    }
}