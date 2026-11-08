package com.queijomineiro.item;

import com.queijomineiro.QueijoMineiro;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

public final class ModItemGroups {
	private ModItemGroups() {
	}

	public static final CreativeModeTab PRINCIPAL = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, QueijoMineiro.id("principal"),
			FabricCreativeModeTab.builder()
					.icon(() -> new ItemStack(ModItems.PAO_DE_QUEIJO))
					.title(Component.translatable("itemGroup.queijo_mineiro.principal"))
					.displayItems((context, entries) -> {
						entries.accept(ModItems.MANDIOCA);
						entries.accept(ModItems.MANDIOCA_RALADA);
						entries.accept(ModItems.POLVILHO);
						entries.accept(ModItems.MANDIOCA_COZIDA);
						entries.accept(ModItems.COAGULADOR_BOVINO);
						entries.accept(ModItems.COALHO);
						entries.accept(ModItems.MASSA_DE_QUEIJO);
						entries.accept(ModItems.FORMA_DE_QUEIJO);
						entries.accept(ModItems.QUEIJO_MINAS_FRESCAL);
						entries.accept(ModItems.QUEIJO_MEIA_CURA);
						entries.accept(ModItems.QUEIJO_CURADO);
						entries.accept(ModItems.MASSA_DE_PAO_DE_QUEIJO);
						entries.accept(ModItems.PAO_DE_QUEIJO);
					})
					.build());

	public static void initialize() {
		// Adiciona também nas abas vanilla (Comidas e Ingredientes) para achar fácil!
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
			entries.accept(ModItems.MANDIOCA_COZIDA);
			entries.accept(ModItems.QUEIJO_MINAS_FRESCAL);
			entries.accept(ModItems.QUEIJO_MEIA_CURA);
			entries.accept(ModItems.QUEIJO_CURADO);
			entries.accept(ModItems.PAO_DE_QUEIJO);
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
			entries.accept(ModItems.MANDIOCA);
			entries.accept(ModItems.MANDIOCA_RALADA);
			entries.accept(ModItems.POLVILHO);
			entries.accept(ModItems.COAGULADOR_BOVINO);
			entries.accept(ModItems.COALHO);
			entries.accept(ModItems.MASSA_DE_QUEIJO);
			entries.accept(ModItems.MASSA_DE_PAO_DE_QUEIJO);
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
			entries.accept(ModItems.FORMA_DE_QUEIJO);
		});
	}
}
