package com.queijomineiro.block;

import com.queijomineiro.item.ModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;

/**
 * Mandioca: planta-se a própria raiz em terra arada (como cenoura/batata).
 * Idades 0–7; quando madura dropa várias mandiocas.
 */
public class MandiocaCropBlock extends CropBlock {
	public MandiocaCropBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return ModItems.MANDIOCA;
	}
}
