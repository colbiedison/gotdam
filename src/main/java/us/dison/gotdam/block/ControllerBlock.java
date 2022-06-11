package us.dison.gotdam.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.NetworkSide;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import us.dison.gotdam.GotDam;
import us.dison.gotdam.blockentity.ControllerBlockEntity;
import us.dison.gotdam.client.GotDamClient;
import us.dison.gotdam.data.DamManager;
import us.dison.gotdam.network.packets.DamPreviewPacket;
import us.dison.gotdam.scan.DamArea;
import us.dison.gotdam.scan.DamScanResult;

public class ControllerBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public ControllerBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.SOUTH));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient)
            GotDamClient.onOpenController(pos);
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ControllerBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing());
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if (world.getBlockEntity(pos) instanceof ControllerBlockEntity controller) {
            if (world instanceof ServerWorld serverWorld) {
                controller.setScanResult(DamScanResult.notRunYet(new DamArea(serverWorld.getRegistryKey().getValue(), controller.getPos())));
                for (ServerWorld sw : serverWorld.getServer().getWorlds()) {
                    for (ServerPlayerEntity p : sw.getPlayers()) {
                        p.networkHandler.sendPacket(new DamPreviewPacket(controller.getDam()).toPacket(NetworkSide.CLIENTBOUND));
                    }
                }

                DamManager.ofWorld(serverWorld).remove(controller.getID());
            } else if (world instanceof ClientWorld clientWorld) {

            }
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, GotDam.BE_TYPE_CONTROLLER, ControllerBlockEntity::tick);
    }
}
