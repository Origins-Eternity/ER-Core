package com.origins_eternal.ercore.gen;

import com.origins_eternal.ercore.config.Config;
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

import static com.origins_eternal.ercore.content.block.Ores.*;

public class GenOres implements IWorldGenerator
{
	private final WorldGenerator copper_ore;
	private final WorldGenerator tin_ore;
	private final WorldGenerator iridium_ore;
	private final WorldGenerator rutile_ore;
	private final WorldGenerator sulphur_ore;
	private final WorldGenerator tungsten_ore;

	public GenOres() {
		copper_ore = new WorldGenMinable(Copper_Ore.getDefaultState(), Config.copperVeinSize, BlockMatcher.forBlock(Blocks.STONE));
		tin_ore = new WorldGenMinable(Tin_Ore.getDefaultState(), Config.tinVeinSize, BlockMatcher.forBlock(Blocks.STONE));
		iridium_ore = new WorldGenMinable(Iridium_Ore.getDefaultState(), Config.iridiumVeinSize, BlockMatcher.forBlock(Blocks.STONE));
		rutile_ore = new WorldGenMinable(Rutile_Ore.getDefaultState(), Config.rutileVeinSize, BlockMatcher.forBlock(Blocks.STONE));
		sulphur_ore = new WorldGenMinable(Sulphur_Ore.getDefaultState(), Config.sulphurVeinSize, BlockMatcher.forBlock(Blocks.STONE));
		tungsten_ore = new WorldGenMinable(Tungsten_ore.getDefaultState(), Config.tungstenVeinSize, BlockMatcher.forBlock(Blocks.STONE));
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		int dimension = world.provider.getDimension();
		if (dimension == 0) {
			runGenerator(copper_ore, world, random, chunkX, chunkZ, Config.copperChance, Config.copperMinHeight, Config.copperMaxHeight);
			runGenerator(tin_ore, world, random, chunkX, chunkZ, Config.tinChance, Config.tinMinHeight, Config.tinMaxHeight);
			runGenerator(iridium_ore, world, random, chunkX, chunkZ, Config.iridiumChance, Config.iridiumMinHeight, Config.iridiumMaxHeight);
			runGenerator(rutile_ore, world, random, chunkX, chunkZ, Config.rutileChance, Config.rutileMinHeight, Config.rutileMaxHeight);
			runGenerator(tungsten_ore, world, random, chunkX, chunkZ, Config.tungstenChance, Config.tungstenMinHeight, Config.tungstenMaxHeight);
		} else if (dimension == -1) {
			runGenerator(sulphur_ore, world, random, chunkX, chunkZ, Config.sulphurChance, Config.sulphurMinHeight, Config.sulphurMaxHeight);
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