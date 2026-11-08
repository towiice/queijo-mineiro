package com.queijomineiro.block;

import com.mojang.serialization.MapCodec;
import com.queijomineiro.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.block.ShapeContext;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * Forma de queijo.
 * <ul>
 *   <li>cura = 0: vazia — clique com Massa de Queijo para enformar.</li>
 *   <li>cura = 1: queijo fresco (frescal)</li>
 *   <li>cura = 2: meia-cura</li>
 *   <li>cura = 3: curado</li>
 * </ul>
 * O queijo cura com o tempo (random ticks), mas não cura se estiver tomando chuva.
 * Clique com a mão (ou qualquer item que não seja massa) para desenformar.
 */
public class FormaDeQueijoBlock extends Block {
	public static final MapCodec<FormaDeQueijoBlock> CODEC = createCodec(FormaDeQueijoBlock::new);

	public static final IntProperty CURA = IntProperty.of("cura", 0, 3);
	public static final int VAZIA = 0;
	public static final int FRESCO = 1;
	public static final int MEIA_CURA = 2;
	public static final int CURADO = 3;

	/** 1 em N random ticks avança um estágio (~3–4 min por estágio com randomTickSpeed padrão). */
	private static final int CHANCE_DE_CURA = 3;

	private static final VoxelShape FORMA_VAZIA = Block.createCuboidShape(2, 0, 2, 14, 5, 14);
	private static final VoxelShape FORMA_CHEIA = Block.createCuboidShape(2, 0, 2, 14, 7, 14);

	public FormaDeQueijoBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(CURA, VAZIA));
	}

	@Override
	protected MapCodec<? extends Block> getCodec() {
		return CODEC;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(CURA);
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(CURA) == VAZIA ? FORMA_VAZIA : FORMA_CHEIA;
	}

	@Override
	protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos,
										 PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (state.get(CURA) == VAZIA) {
			int novoEstagio = -1;
			if (stack.isOf(ModItems.MASSA_DE_QUEIJO) || stack.isOf(ModItems.QUEIJO_MINAS_FRESCAL)) {
				novoEstagio = FRESCO;
			} else if (stack.isOf(ModItems.QUEIJO_MEIA_CURA)) {
				novoEstagio = MEIA_CURA;
			} else if (stack.isOf(ModItems.QUEIJO_CURADO)) {
				novoEstagio = CURADO;
			}

			if (novoEstagio != -1) {
				if (!world.isClient()) {
					world.setBlockState(pos, state.with(CURA, novoEstagio), Block.NOTIFY_ALL);
					stack.decrementUnlessCreative(1, player);
					world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0f, 1.1f);
					world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
				}
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		int cura = state.get(CURA);
		if (cura == VAZIA) {
			return ActionResult.PASS;
		}
		if (!world.isClient()) {
			player.getInventory().offerOrDrop(new ItemStack(queijoDoEstagio(cura)));
			world.setBlockState(pos, state.with(CURA, VAZIA), Block.NOTIFY_ALL);
			world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8f, 0.9f);
			world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	protected boolean hasRandomTicks(BlockState state) {
		int cura = state.get(CURA);
		return cura == FRESCO || cura == MEIA_CURA;
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int cura = state.get(CURA);
		if (cura < FRESCO || cura >= CURADO) {
			return;
		}
		// Queijo molhado não cura.
		if (world.hasRain(pos.up())) {
			return;
		}
		if (random.nextInt(CHANCE_DE_CURA) == 0) {
			world.setBlockState(pos, state.with(CURA, cura + 1), Block.NOTIFY_ALL);
			world.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
					pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
					6, 0.25, 0.15, 0.25, 0.0);
		}
	}

	@Override
	protected boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	// Usado pelo Minecraft 1.21.6, 1.21.7 e 1.21.8 (3 parâmetros)
	protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(CURA) * 5; // 0, 5, 10, 15
	}

	// Usado pelo Minecraft 1.21.9, 1.21.10 e 1.21.11 (4 parâmetros)
	protected int getComparatorOutput(BlockState state, World world, BlockPos pos, net.minecraft.util.math.Direction direction) {
		return state.get(CURA) * 5; // 0, 5, 10, 15
	}

	public static Item queijoDoEstagio(int cura) {
		return switch (cura) {
			case FRESCO -> ModItems.QUEIJO_MINAS_FRESCAL;
			case MEIA_CURA -> ModItems.QUEIJO_MEIA_CURA;
			default -> ModItems.QUEIJO_CURADO;
		};
	}
}
