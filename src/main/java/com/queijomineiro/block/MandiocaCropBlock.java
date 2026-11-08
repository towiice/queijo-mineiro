package com.queijomineiro.block;

import com.mojang.serialization.MapCodec;
import com.queijomineiro.item.ModItems;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;

/**
 * Mandioca: planta-se a própria raiz em terra arada (como cenoura/batata).
 * Idades 0–7; quando madura dropa várias mandiocas.
 */
public class MandiocaCropBlock extends CropBlock {
	public static final MapCodec<MandiocaCropBlock> CODEC = createCodec(MandiocaCropBlock::new);

	public MandiocaCropBlock(Settings settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends CropBlock> getCodec() {
		return CODEC;
	}

	@Override
	protected ItemConvertible getSeedsItem() {
		return ModItems.MANDIOCA;
	}
}
