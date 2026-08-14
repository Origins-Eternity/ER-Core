package com.origins_eternity.ercore.compat.pyrotech;

import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockCampfire;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static com.origins_eternity.ercore.ERCore.MOD_ID;
import static com.origins_eternity.ercore.compat.pyrotech.RockHelper.*;
import static com.origins_eternity.ercore.content.tab.CreativeTab.ERCORE;

public class BlockRock extends Block {
    public static String name = "rock";

    public static final PropertyBool BOUND = PropertyBool.create("bound");
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 7);

    public BlockRock() {
        super(Material.ROCK);
        setHardness(1.4f);
        setResistance(6.0f);
        setRegistryName(name);
        setSoundType(SoundType.STONE);
        setTranslationKey(MOD_ID + "." + name);
        setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, 0).withProperty(BOUND, false));

        setCreativeTab(ERCORE);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn, pos);
    }

    @Override
    public boolean isFullCube(IBlockState state) { return false; }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (!world.isRemote) {
            if (!canBlockStay(world, pos)) {
                world.destroyBlock(pos, false);
            }
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int rotation = state.getValue(ROTATION);
        boolean bound = state.getValue(BOUND);
        return rotation | (bound ? 8 : 0);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABBS[state.getValue(ROTATION) % 8];
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int rotation = meta & 7;
        boolean bound = (meta & 8) != 0;
        return this.getDefaultState().withProperty(ROTATION, rotation).withProperty(BOUND, bound);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ROTATION, BOUND);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = this.getDefaultState();
        for (int i = 0; i < OFFSETS.length; i++) {
            BlockPos center = getCenter(pos, i);
            if (isCampfire(world, center)) {
                return state.withProperty(ROTATION, i).withProperty(BOUND, true);
            }
        }
        int rotation = Math.abs(pos.getX() * 23 + pos.getZ() * 46) % 8;
        return state.withProperty(ROTATION, rotation);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && !state.getValue(BOUND)) {
            if (hand == EnumHand.MAIN_HAND && player.getHeldItem(hand).isEmpty()) {
                int rotation = state.getValue(ROTATION);
                if (!player.isSneaking()) {
                    rotation = (rotation + 1) % 8;
                } else {
                    rotation = (rotation + 7) % 8;
                }

                BlockPos center = getCenter(pos, rotation);
                if (!isCampfire(world, center)) {
                    world.setBlockState(pos, state.withProperty(ROTATION, rotation), 3);
                } else {
                    world.setBlockState(pos, state.withProperty(ROTATION, rotation).withProperty(BOUND, true), 3);
                    if (checkSurround(world, center)) {
                        convertCampfire(world, center);
                    }
                }
                world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.6f, 1.4f);
                return true;
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote && state.getValue(BOUND)) {
            BlockPos center = getCenter(pos, state.getValue(ROTATION));
            if (isCampfire(worldIn, center)) {
                if (checkSurround(worldIn, center)) {
                    convertCampfire(worldIn, center);
                }
            }
        }
        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote && state.getValue(BOUND)) {
            BlockPos center = getCenter(pos, state.getValue(ROTATION));
            if (world.getBlockState(center).getBlock() instanceof BlockCampfireRock) {
                convertCampfire(world, center);
            }
        }
        super.breakBlock(world, pos, state);
    }

    private boolean canBlockStay(World world, BlockPos pos) {
        return world.isSideSolid(pos.down(), EnumFacing.UP);
    }

    private boolean isCampfire(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof BlockCampfire;
    }

    private BlockPos getCenter(BlockPos pos, int rotation) {
        return pos.add(OFFSETS[rotation][0], 0, OFFSETS[rotation][1]);
    }
}