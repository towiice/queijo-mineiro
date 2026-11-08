package com.queijomineiro.loot;

import com.queijomineiro.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue;

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

	private static final Identifier VACA = Identifier.withDefaultNamespace("entities/cow");
	private static final Identifier MOOSHROOM = Identifier.withDefaultNamespace("entities/mooshroom");
	private static final Identifier GRAMA = Identifier.withDefaultNamespace("blocks/short_grass");

	public static void initialize() {
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (!source.isBuiltin()) {
				return;
			}
			Identifier id = key.identifier();

			if (VACA.equals(id) || MOOSHROOM.equals(id)) {
				tableBuilder.withPool(LootPool.lootPool()
						.setRolls(Holder.direct(new ConstantValue(1)))
						.when(LootItemRandomChanceCondition.randomChance(0.35f))
						.add(LootItem.lootTableItem(ModItems.COAGULADOR_BOVINO)));
			}

			if (GRAMA.equals(id)) {
				tableBuilder.withPool(LootPool.lootPool()
						.setRolls(Holder.direct(new ConstantValue(1)))
						.when(LootItemRandomChanceCondition.randomChance(0.05f))
						.add(LootItem.lootTableItem(ModItems.MANDIOCA)));
			}
		});
	}
}
