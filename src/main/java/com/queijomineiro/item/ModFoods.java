package com.queijomineiro.item;

import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

/**
 * Valores de comida. Referência vanilla: pão = 5 / 0.6, bife = 8 / 0.8.
 */
public final class ModFoods {
	private ModFoods() {
	}

	private static final int SEG = 20; // ticks por segundo

	// Mandioca cozida: comida simples e honesta.
	public static final FoodProperties MANDIOCA_COZIDA = new FoodProperties.Builder()
			.nutrition(6).saturationModifier(0.7f).build();

	// Frescal: lanche leve e rápido de comer.
	public static final FoodProperties QUEIJO_MINAS_FRESCAL = new FoodProperties.Builder()
			.nutrition(3).saturationModifier(0.6f).build();
	public static final Consumable QUEIJO_MINAS_FRESCAL_CONSUMO = Consumables.defaultFood()
			.consumeSeconds(1.2f).build();

	// Meia-cura: mais sustância + Regeneração curta.
	public static final FoodProperties QUEIJO_MEIA_CURA = new FoodProperties.Builder()
			.nutrition(5).saturationModifier(0.8f).build();
	public static final Consumable QUEIJO_MEIA_CURA_CONSUMO = Consumables.defaultFood()
			.onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
					new MobEffectInstance(MobEffects.REGENERATION, 5 * SEG, 0)), 1.0f))
			.build();

	// Curado: muita saturação + Absorção.
	public static final FoodProperties QUEIJO_CURADO = new FoodProperties.Builder()
			.nutrition(6).saturationModifier(1.0f).build();
	public static final Consumable QUEIJO_CURADO_CONSUMO = Consumables.defaultFood()
			.onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
					new MobEffectInstance(MobEffects.ABSORPTION, 30 * SEG, 0)), 1.0f))
			.build();

	// Pão de queijo quentinho: comida de conforto de alto nível!
	// Nutrição 6, saturação 1.2 (nível cenoura dourada) e come rápido (1.0s).
	// Efeito temático: Pressa I (Haste) por 20 segundos (disposição para trabalhar/minerar).
	public static final FoodProperties PAO_DE_QUEIJO = new FoodProperties.Builder()
			.nutrition(6).saturationModifier(1.2f).alwaysEdible().build();
	public static final Consumable PAO_DE_QUEIJO_CONSUMO = Consumables.defaultFood()
			.consumeSeconds(1.0f)
			.onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
					new MobEffectInstance(MobEffects.HASTE, 20 * SEG, 0)), 1.0f))
			.build();
}
