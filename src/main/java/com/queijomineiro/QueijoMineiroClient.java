package com.queijomineiro;

import com.queijomineiro.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;

public class QueijoMineiroClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// A plantação usa texturas com transparência.
		BlockRenderLayerMap.putBlock(ModBlocks.PLANTACAO_DE_MANDIOCA, BlockRenderLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(ModBlocks.FORMA_DE_QUEIJO, BlockRenderLayer.CUTOUT);
	}
}
