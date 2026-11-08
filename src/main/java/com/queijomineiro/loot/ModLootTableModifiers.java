package com.queijomineiro.loot;

import com.queijomineiro.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

/**
 * Como obter os ingredientes "de origem":
 * <ul>
 *   <li>Coalho: vacas (e mooshrooms) têm 35% de chance de dropar.</li>
 *   <li>Mandioca: grama baixa tem 5% de chance de dropar.</li>
 * </ul>
 */
public final class ModLootTableModifiers {
	private ModLootTableModifiers() {
	}

	private static final Identifier VACA = Identifier.ofVanilla("entities/cow");
	private static final Identifier MOOSHROOM = Identifier.ofVanilla("entities/mooshroom");
	private static final Identifier GRAMA = Identifier.ofVanilla("blocks/short_grass");

	public static void initialize() {
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (!source.isBuiltin()) {
				return;
			}
			Identifier id = key.getValue();

			if (VACA.equals(id) || MOOSHROOM.equals(id)) {
				tableBuilder.pool(LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.conditionally(RandomChanceLootCondition.builder(0.35f))
						.with(ItemEntry.builder(ModItems.COAGULADOR_BOVINO)));
			}

			if (GRAMA.equals(id)) {
				tableBuilder.pool(LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.conditionally(RandomChanceLootCondition.builder(0.05f))
						.with(ItemEntry.builder(ModItems.MANDIOCA)));
			}
		});
	}
}
