package com.origins_eternity.ercore.gen;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

import static com.origins_eternity.ercore.content.block.Ores.*;

public class GenOres implements IWorldGenerator {
	private final WorldGenerator copper_ore;
	private final WorldGenerator tin_ore;
	private final WorldGenerator tungsten_ore;
	private final WorldGenerator nickel_ore;

	private final int copperVeinSize = 6;
	private final int copperChance = 32;
	private final int copperMinHeight = 16;
	private final int copperMaxHeight = 112;
	private final int tinVeinSize = 5;
	private final int tinChance = 29;
	private final int tinMinHeight = 16;
	private final int tinMaxHeight = 118;
	private final int tungstenVeinSize  = 3;
	private final int tungstenChance = 35;
	private final int tungstenMinHeight = 0;
	private final int tungstenMaxHeight = 16;
	private final int nickelVeinSize  = 4;
	private final int nickelChance = 29;
	private final int nickelMinHeight = 8;
	private final int nickelMaxHeight = 45;

	public GenOres() {
		copper_ore = new WorldGenMinable(Copper_Ore.getDefaultState(), copperVeinSize, BlockMatcher.forBlock(Blocks.STONE));
		tin_ore = new WorldGenMinable(Tin_Ore.getDefaultState(), tinVeinSize, BlockMatcher.forBlock(Blocks.STONE));
		tungsten_ore = new WorldGenMinable(Tungsten_ore.getDefaultState(), tungstenVeinSize, BlockMatcher.forBlock(Blocks.STONE));
		nickel_ore = new WorldGenMinable(Nickel_ore.getDefaultState(), nickelVeinSize, BlockMatcher.forBlock(Blocks.STONE));
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		int dimension = world.provider.getDimension();
		if (dimension == 0) {
			runGenerator(copper_ore, world, random, chunkX, chunkZ, copperChance, copperMinHeight, copperMaxHeight);
			runGenerator(tin_ore, world, random, chunkX, chunkZ, tinChance, tinMinHeight, tinMaxHeight);
			runGenerator(tungsten_ore, world, random, chunkX, chunkZ, tungstenChance, tungstenMinHeight, tungstenMaxHeight);
			runGenerator(nickel_ore, world, random, chunkX, chunkZ, nickelChance, nickelMinHeight, nickelMaxHeight);
		}
	}

	private void runGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance, int minHeight, int maxHeight) {
		if (minHeight > maxHeight || maxHeight < 0 || minHeight < 0 || maxHeight > 256 || chance > 50 || chance < 0) return;
		int heightDiff = maxHeight - minHeight + 1;
		for (int i = 0; i < chance; i++) {
			int x = chunkX * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunkZ * 16 + rand.nextInt(16);
			gen.generate(world, rand, new BlockPos(x, y, z));
		}
	}
}