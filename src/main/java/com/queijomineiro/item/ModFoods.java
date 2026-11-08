package com.queijomineiro.item;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;

import java.util.List;

/**
 * Valores de comida. Referência vanilla: pão = 5 / 0.6, bife = 8 / 0.8.
 */
public final class ModFoods {
	private ModFoods() {
	}

	private static final int SEG = 20; // ticks por segundo

	// Mandioca cozida: comida simples e honesta.
	public static final FoodComponent MANDIOCA_COZIDA = new FoodComponent.Builder()
			.nutrition(6).saturationModifier(0.7f).build();

	// Frescal: lanche leve e rápido de comer.
	public static final FoodComponent QUEIJO_MINAS_FRESCAL = new FoodComponent.Builder()
			.nutrition(3).saturationModifier(0.6f).build();
	public static final ConsumableComponent QUEIJO_MINAS_FRESCAL_CONSUMO = ConsumableComponents.food()
			.consumeSeconds(1.2f).build();

	// Meia-cura: mais sustância + Regeneração curta.
	public static final FoodComponent QUEIJO_MEIA_CURA = new FoodComponent.Builder()
			.nutrition(5).saturationModifier(0.8f).build();
	public static final ConsumableComponent QUEIJO_MEIA_CURA_CONSUMO = ConsumableComponents.food()
			.consumeEffect(new ApplyEffectsConsumeEffect(List.of(
					new StatusEffectInstance(StatusEffects.REGENERATION, 5 * SEG, 0)), 1.0f))
			.build();

	// Curado: muita saturação + Absorção.
	public static final FoodComponent QUEIJO_CURADO = new FoodComponent.Builder()
			.nutrition(6).saturationModifier(1.0f).build();
	public static final ConsumableComponent QUEIJO_CURADO_CONSUMO = ConsumableComponents.food()
			.consumeEffect(new ApplyEffectsConsumeEffect(List.of(
					new StatusEffectInstance(StatusEffects.ABSORPTION, 30 * SEG, 0)), 1.0f))
			.build();

	// Pão de queijo quentinho: comida de conforto de alto nível!
	// Nutrição 6, saturação 1.2 (nível cenoura dourada) e come rápido (1.0s).
	// Efeito temático: Pressa I (Haste) por 20 segundos (disposição para trabalhar/minerar).
	public static final FoodComponent PAO_DE_QUEIJO = new FoodComponent.Builder()
			.nutrition(6).saturationModifier(1.2f).alwaysEdible().build();
	public static final ConsumableComponent PAO_DE_QUEIJO_CONSUMO = ConsumableComponents.food()
			.consumeSeconds(1.0f)
			.consumeEffect(new ApplyEffectsConsumeEffect(List.of(
					new StatusEffectInstance(StatusEffects.HASTE, 20 * SEG, 0)), 1.0f))
			.build();
}
