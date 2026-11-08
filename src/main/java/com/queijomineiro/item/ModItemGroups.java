package com.queijomineiro.item;

import com.queijomineiro.QueijoMineiro;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public final class ModItemGroups {
	private ModItemGroups() {
	}

	public static final ItemGroup PRINCIPAL = Registry.register(Registries.ITEM_GROUP, QueijoMineiro.id("principal"),
			FabricItemGroup.builder()
					.icon(() -> new ItemStack(ModItems.PAO_DE_QUEIJO))
					.displayName(Text.translatable("itemGroup.queijo_mineiro.principal"))
					.entries((context, entries) -> {
						entries.add(ModItems.MANDIOCA);
						entries.add(ModItems.MANDIOCA_RALADA);
						entries.add(ModItems.POLVILHO);
						entries.add(ModItems.MANDIOCA_COZIDA);
						entries.add(ModItems.COAGULADOR_BOVINO);
						entries.add(ModItems.COALHO);
						entries.add(ModItems.MASSA_DE_QUEIJO);
						entries.add(ModItems.FORMA_DE_QUEIJO);
						entries.add(ModItems.QUEIJO_MINAS_FRESCAL);
						entries.add(ModItems.QUEIJO_MEIA_CURA);
						entries.add(ModItems.QUEIJO_CURADO);
						entries.add(ModItems.MASSA_DE_PAO_DE_QUEIJO);
						entries.add(ModItems.PAO_DE_QUEIJO);
					})
					.build());

	public static void initialize() {
		// Adiciona também nas abas vanilla (Comidas e Ingredientes) para achar fácil!
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
			entries.add(ModItems.MANDIOCA_COZIDA);
			entries.add(ModItems.QUEIJO_MINAS_FRESCAL);
			entries.add(ModItems.QUEIJO_MEIA_CURA);
			entries.add(ModItems.QUEIJO_CURADO);
			entries.add(ModItems.PAO_DE_QUEIJO);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
			entries.add(ModItems.MANDIOCA);
			entries.add(ModItems.MANDIOCA_RALADA);
			entries.add(ModItems.POLVILHO);
			entries.add(ModItems.COAGULADOR_BOVINO);
			entries.add(ModItems.COALHO);
			entries.add(ModItems.MASSA_DE_QUEIJO);
			entries.add(ModItems.MASSA_DE_PAO_DE_QUEIJO);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
			entries.add(ModItems.FORMA_DE_QUEIJO);
		});
	}
}
