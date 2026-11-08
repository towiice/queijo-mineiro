package com.queijomineiro.block;

import com.queijomineiro.QueijoMineiro;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class ModBlocks {
	private ModBlocks() {
	}

	public static final Block PLANTACAO_DE_MANDIOCA = register("plantacao_de_mandioca", MandiocaCropBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.PLANT)
					.noCollision()
					.randomTicks()
					.instabreak()
					.sound(SoundType.CROP)
					.pushReaction(PushReaction.POPPED));

	public static final Block FORMA_DE_QUEIJO = register("forma_de_queijo", FormaDeQueijoBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.strength(0.8f)
					.sound(SoundType.WOOD)
					.noOcclusion()
					.randomTicks());

	private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, QueijoMineiro.id(name));
		Block block = factory.apply(settings.setId(key));
		return Registry.register(BuiltInRegistries.BLOCK, key, block);
	}

	public static void initialize() {
		// Força o carregamento da classe (registros estáticos).
	}
}
