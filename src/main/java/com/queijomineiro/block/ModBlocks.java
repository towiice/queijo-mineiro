package com.queijomineiro.block;

import com.queijomineiro.QueijoMineiro;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.Function;

public final class ModBlocks {
	private ModBlocks() {
	}

	public static final Block PLANTACAO_DE_MANDIOCA = register("plantacao_de_mandioca", MandiocaCropBlock::new,
			AbstractBlock.Settings.create()
					.mapColor(MapColor.DARK_GREEN)
					.noCollision()
					.ticksRandomly()
					.breakInstantly()
					.sounds(BlockSoundGroup.CROP)
					.pistonBehavior(PistonBehavior.DESTROY));

	public static final Block FORMA_DE_QUEIJO = register("forma_de_queijo", FormaDeQueijoBlock::new,
			AbstractBlock.Settings.create()
					.mapColor(MapColor.OAK_TAN)
					.strength(0.8f)
					.sounds(BlockSoundGroup.WOOD)
					.nonOpaque()
					.ticksRandomly());

	private static Block register(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
		RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, QueijoMineiro.id(name));
		Block block = factory.apply(settings.registryKey(key));
		return Registry.register(Registries.BLOCK, key, block);
	}

	public static void initialize() {
		// Força o carregamento da classe (registros estáticos).
	}
}
