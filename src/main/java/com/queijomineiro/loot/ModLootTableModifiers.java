package com.queijomineiro.loot;

import java.util.Set;

import com.queijomineiro.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

/**
 * Modificadores de tabelas de loot nativas:
 * <ul>
 *   <li>Vacas e Mooshrooms: 35% de chance de dropar Coagulador Bovino.</li>
 *   <li>Grama baixa e Samambaia: 5% de chance de dropar Mandioca.</li>
 *   <li>Baús de Vilas (Casas, Açougueiro e Pastor): Mandioca, Polvilho, Coalho, Queijos e Pão de Queijo.</li>
 *   <li>Baús de Dungeons, Minas Abandonadas, Fortalezas e Postos Pillager: Queijo Curado, Meia-Cura, Pão de Queijo e Mandioca.</li>
 * </ul>
 */
public final class ModLootTableModifiers {
	private ModLootTableModifiers() {
	}

	private static final Identifier VACA = Identifier.ofVanilla("entities/cow");
	private static final Identifier MOOSHROOM = Identifier.ofVanilla("entities/mooshroom");
	private static final Identifier GRAMA = Identifier.ofVanilla("blocks/short_grass");
	private static final Identifier SAMAMBAIA = Identifier.ofVanilla("blocks/fern");

	private static final Set<Identifier> BAUS_CASAS_VILA = Set.of(
			Identifier.ofVanilla("chests/village/village_plains_house"),
			Identifier.ofVanilla("chests/village/village_savanna_house"),
			Identifier.ofVanilla("chests/village/village_taiga_house"),
			Identifier.ofVanilla("chests/village/village_snowy_house"),
			Identifier.ofVanilla("chests/village/village_desert_house")
	);

	private static final Set<Identifier> BAUS_OFICIOS_VILA = Set.of(
			Identifier.ofVanilla("chests/village/village_butcher"),
			Identifier.ofVanilla("chests/village/village_shepherd")
	);

	private static final Set<Identifier> BAUS_DUNGEONS_E_ESTRUTURAS = Set.of(
			Identifier.ofVanilla("chests/simple_dungeon"),
			Identifier.ofVanilla("chests/abandoned_mineshaft"),
			Identifier.ofVanilla("chests/stronghold_corridor"),
			Identifier.ofVanilla("chests/stronghold_crossing"),
			Identifier.ofVanilla("chests/pillager_outpost"),
			Identifier.ofVanilla("chests/woodland_mansion")
	);

	public static void initialize() {
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (!source.isBuiltin()) {
				return;
			}
			Identifier id = key.getValue();

			if (VACA.equals(id) || MOOSHROOM.equals(id)) {
				adicionarItemSimples(tableBuilder, ModItems.COAGULADOR_BOVINO, 0.35f);
			}

			if (GRAMA.equals(id) || SAMAMBAIA.equals(id)) {
				adicionarItemSimples(tableBuilder, ModItems.MANDIOCA, 0.05f);
			}

			if (BAUS_CASAS_VILA.contains(id)) {
				adicionarItemComQuantidade(tableBuilder, ModItems.MANDIOCA, 0.45f, 1, 4);
				adicionarItemComQuantidade(tableBuilder, ModItems.POLVILHO, 0.30f, 1, 3);
				adicionarItemComQuantidade(tableBuilder, ModItems.PAO_DE_QUEIJO, 0.25f, 1, 3);
			}

			if (BAUS_OFICIOS_VILA.contains(id)) {
				adicionarItemComQuantidade(tableBuilder, ModItems.COALHO, 0.35f, 1, 2);
				adicionarItemComQuantidade(tableBuilder, ModItems.QUEIJO_MINAS_FRESCAL, 0.40f, 1, 2);
				adicionarItemSimples(tableBuilder, ModItems.QUEIJO_MEIA_CURA, 0.20f);
			}

			if (BAUS_DUNGEONS_E_ESTRUTURAS.contains(id)) {
				adicionarItemComQuantidade(tableBuilder, ModItems.QUEIJO_CURADO, 0.30f, 1, 2);
				adicionarItemComQuantidade(tableBuilder, ModItems.QUEIJO_MEIA_CURA, 0.35f, 1, 2);
				adicionarItemComQuantidade(tableBuilder, ModItems.PAO_DE_QUEIJO, 0.25f, 1, 4);
				adicionarItemComQuantidade(tableBuilder, ModItems.MANDIOCA, 0.30f, 2, 4);
			}
		});
	}

	private static void adicionarItemSimples(LootTable.Builder tableBuilder, Item item, float chance) {
		tableBuilder.pool(LootPool.builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceLootCondition.builder(chance))
				.with(ItemEntry.builder(item)));
	}

	private static void adicionarItemComQuantidade(LootTable.Builder tableBuilder, Item item, float chance, float min, float max) {
		tableBuilder.pool(LootPool.builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(RandomChanceLootCondition.builder(chance))
				.with(ItemEntry.builder(item)
						.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max)))));
	}
}
