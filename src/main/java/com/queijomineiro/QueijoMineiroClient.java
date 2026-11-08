package com.queijomineiro;

import com.queijomineiro.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class QueijoMineiroClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// A plantação usa texturas com transparência.
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PLANTACAO_DE_MANDIOCA, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FORMA_DE_QUEIJO, RenderLayer.getCutout());
	}
}
