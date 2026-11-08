package com.queijomineiro;

import net.fabricmc.api.ClientModInitializer;

public class QueijoMineiroClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// No Minecraft 26.x, o motor gráfico detecta automaticamente a transparência (cutout) das texturas PNG.
	}
}
