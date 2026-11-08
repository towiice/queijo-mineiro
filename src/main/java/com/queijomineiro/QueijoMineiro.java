package com.queijomineiro;

import com.queijomineiro.block.ModBlocks;
import com.queijomineiro.item.ModItemGroups;
import com.queijomineiro.item.ModItems;
import com.queijomineiro.loot.ModLootTableModifiers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueijoMineiro implements ModInitializer {
	public static final String MOD_ID = "queijo_mineiro";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModItems.initialize();
		ModItemGroups.initialize();
		ModLootTableModifiers.initialize();

		CompostingChanceRegistry.INSTANCE.add(ModItems.MANDIOCA, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.MANDIOCA_RALADA, 0.5f);

		LOGGER.info("Uai! Queijo Mineiro carregado. Bora fazer pão de queijo!");
	}
}
