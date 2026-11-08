package com.queijomineiro.item;

import com.queijomineiro.QueijoMineiro;
import com.queijomineiro.block.ModBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

public final class ModItems {
	private ModItems() {
	}

	// --- Mandioca ---
	/** A raiz: planta em terra arada e vira a plantação. */
	public static final Item MANDIOCA = register("mandioca",
			s -> new BlockItem(ModBlocks.PLANTACAO_DE_MANDIOCA, s), new Item.Settings());
	public static final Item MANDIOCA_RALADA = register("mandioca_ralada", Item::new, new Item.Settings());
	public static final Item MANDIOCA_COZIDA = register("mandioca_cozida", Item::new,
			new Item.Settings().food(ModFoods.MANDIOCA_COZIDA));
	public static final Item POLVILHO = register("polvilho", Item::new, new Item.Settings());

	// --- Queijo ---
	public static final Item COALHO = register("coalho", Item::new, new Item.Settings());
	public static final Item MASSA_DE_QUEIJO = register("massa_de_queijo", Item::new,
			new Item.Settings().maxCount(16));
	public static final Item QUEIJO_MINAS_FRESCAL = register("queijo_minas_frescal", Item::new,
			new Item.Settings().food(ModFoods.QUEIJO_MINAS_FRESCAL, ModFoods.QUEIJO_MINAS_FRESCAL_CONSUMO));
	public static final Item QUEIJO_MEIA_CURA = register("queijo_meia_cura", Item::new,
			new Item.Settings().food(ModFoods.QUEIJO_MEIA_CURA, ModFoods.QUEIJO_MEIA_CURA_CONSUMO));
	public static final Item QUEIJO_CURADO = register("queijo_curado", Item::new,
			new Item.Settings().food(ModFoods.QUEIJO_CURADO, ModFoods.QUEIJO_CURADO_CONSUMO));
	public static final Item FORMA_DE_QUEIJO = register("forma_de_queijo",
			s -> new BlockItem(ModBlocks.FORMA_DE_QUEIJO, s), new Item.Settings().useBlockPrefixedTranslationKey());

	// --- Pão de queijo ---
	public static final Item MASSA_DE_PAO_DE_QUEIJO = register("massa_de_pao_de_queijo", Item::new, new Item.Settings());
	public static final Item PAO_DE_QUEIJO = register("pao_de_queijo", Item::new,
			new Item.Settings().food(ModFoods.PAO_DE_QUEIJO, ModFoods.PAO_DE_QUEIJO_CONSUMO));

	private static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, QueijoMineiro.id(name));
		Item item = factory.apply(settings.registryKey(key));
		return Registry.register(Registries.ITEM, key, item);
	}

	public static void initialize() {
		// Força o carregamento da classe (registros estáticos).
	}
}
