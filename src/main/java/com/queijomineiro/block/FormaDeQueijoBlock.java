package com.queijomineiro.block;

import com.queijomineiro.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

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
	public static final IntegerProperty CURA = IntegerProperty.create("cura", 0, 3);
	public static final int VAZIA = 0;
	public static final int FRESCO = 1;
	public static final int MEIA_CURA = 2;
	public static final int CURADO = 3;

	/** 1 em N random ticks avança um estágio (~3–4 min por estágio com randomTickSpeed padrão). */
	private static final int CHANCE_DE_CURA = 3;

	private static final VoxelShape FORMA_VAZIA = Block.box(2, 0, 2, 14, 5, 14);
	private static final VoxelShape FORMA_CHEIA = Block.box(2, 0, 2, 14, 7, 14);

	public FormaDeQueijoBlock(Properties settings) {
		super(settings);
		registerDefaultState(getStateDefinition().any().setValue(CURA, VAZIA));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(CURA);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return state.getValue(CURA) == VAZIA ? FORMA_VAZIA : FORMA_CHEIA;
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos,
										 Player player, InteractionHand hand, BlockHitResult hit) {
		if (state.getValue(CURA) == VAZIA) {
			int novoEstagio = -1;
			if (stack.is(ModItems.MASSA_DE_QUEIJO) || stack.is(ModItems.QUEIJO_MINAS_FRESCAL)) {
				novoEstagio = FRESCO;
			} else if (stack.is(ModItems.QUEIJO_MEIA_CURA)) {
				novoEstagio = MEIA_CURA;
			} else if (stack.is(ModItems.QUEIJO_CURADO)) {
				novoEstagio = CURADO;
			}

			if (novoEstagio != -1) {
				if (!world.isClientSide()) {
					world.setBlock(pos, state.setValue(CURA, novoEstagio), Block.UPDATE_ALL);
					stack.consume(1, player);
					world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0f, 1.1f);
					world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.TRY_WITH_EMPTY_HAND;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		int cura = state.getValue(CURA);
		if (cura == VAZIA) {
			return InteractionResult.PASS;
		}
		if (!world.isClientSide()) {
			ItemStack queijo = new ItemStack(queijoDoEstagio(cura));
			if (!player.addItem(queijo)) {
				Block.popResource(world, pos, queijo);
			}
			world.setBlock(pos, state.setValue(CURA, VAZIA), Block.UPDATE_ALL);
			world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8f, 0.9f);
			world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected boolean isRandomlyTicking(BlockState state) {
		int cura = state.getValue(CURA);
		return cura == FRESCO || cura == MEIA_CURA;
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		int cura = state.getValue(CURA);
		if (cura < FRESCO || cura >= CURADO) {
			return;
		}
		// Queijo molhado não cura.
		if (world.isRainingAt(pos.above())) {
			return;
		}
		if (random.nextInt(CHANCE_DE_CURA) == 0) {
			world.setBlock(pos, state.setValue(CURA, cura + 1), Block.UPDATE_ALL);
			world.sendParticles(ParticleTypes.HAPPY_VILLAGER,
					pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
					6, 0.25, 0.15, 0.25, 0.0);
		}
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
		return state.getValue(CURA) * 5; // 0, 5, 10, 15
	}

	public static Item queijoDoEstagio(int cura) {
		return switch (cura) {
			case FRESCO -> ModItems.QUEIJO_MINAS_FRESCAL;
			case MEIA_CURA -> ModItems.QUEIJO_MEIA_CURA;
			default -> ModItems.QUEIJO_CURADO;
		};
	}
}
