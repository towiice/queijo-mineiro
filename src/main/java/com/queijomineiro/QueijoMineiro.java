package com.queijomineiro;

import com.queijomineiro.block.ModBlocks;
import com.queijomineiro.item.ModItemGroups;
import com.queijomineiro.item.ModItems;
import com.queijomineiro.loot.ModLootTableModifiers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueijoMineiro implements ModInitializer {
	public static final String MOD_ID = "queijo_mineiro";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModItems.initialize();
		ModItemGroups.initialize();
		ModLootTableModifiers.initialize();

		LOGGER.info("Uai! Queijo Mineiro carregado. Bora fazer pão de queijo!");
	}
}
