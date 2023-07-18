package com.origins_eternity.ercore.gen;

import net.minecraft.block.state.pattern.BlockMatcher;
import com.origins_eternity.ercore.config.Configuration;
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

public class GenOres implements IWorldGenerator
{
	private final WorldGenerator copper_ore;
	private final WorldGenerator tin_ore;
	private final WorldGenerator iridium_ore;
	private final WorldGenerator rutile_ore;
	private final WorldGenerator sulphur_ore;
	private final WorldGenerator tungsten_ore;

	public GenOres() {
		copper_ore = new WorldGenMinable(Copper_Ore.getDefaultState(), Configuration.copperVeinSize, BlockMatcher.forBlock(Blocks.STONE));
		tin_ore = new WorldGenMinable(Tin_Ore.getDefaultState(), Configuration.tinVeinSize, BlockMatcher.forBlock(Blocks.STONE));
		iridium_ore = new WorldGenMinable(Iridium_Ore.getDefaultState(), Configuration.iridiumVeinSize, BlockMatcher.forBlock(Blocks.STONE));
		rutile_ore = new WorldGenMinable(Rutile_Ore.getDefaultState(), Configuration.rutileVeinSize, BlockMatcher.forBlock(Blocks.STONE));
		sulphur_ore = new WorldGenMinable(Sulphur_Ore.getDefaultState(), Configuration.sulphurVeinSize, BlockMatcher.forBlock(Blocks.STONE));
		tungsten_ore = new WorldGenMinable(Tungsten_ore.getDefaultState(), Configuration.tungstenVeinSize, BlockMatcher.forBlock(Blocks.STONE));
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		int dimension = world.provider.getDimension();
		if (dimension == 0) {
			runGenerator(copper_ore, world, random, chunkX, chunkZ, Configuration.copperChance, Configuration.copperMinHeight, Configuration.copperMaxHeight);
			runGenerator(tin_ore, world, random, chunkX, chunkZ, Configuration.tinChance, Configuration.tinMinHeight, Configuration.tinMaxHeight);
			runGenerator(iridium_ore, world, random, chunkX, chunkZ, Configuration.iridiumChance, Configuration.iridiumMinHeight, Configuration.iridiumMaxHeight);
			runGenerator(rutile_ore, world, random, chunkX, chunkZ, Configuration.rutileChance, Configuration.rutileMinHeight, Configuration.rutileMaxHeight);
			runGenerator(tungsten_ore, world, random, chunkX, chunkZ, Configuration.tungstenChance, Configuration.tungstenMinHeight, Configuration.tungstenMaxHeight);
		} else if (dimension == -1) {
			runGenerator(sulphur_ore, world, random, chunkX, chunkZ, Configuration.sulphurChance, Configuration.sulphurMinHeight, Configuration.sulphurMaxHeight);
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