package us.dison.gotdam.blockentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;
import us.dison.gotdam.GotDam;

public class ControllerBlockEntity extends BlockEntity {

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(500000, 2000, 0) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };

    public ControllerBlockEntity(BlockPos pos, BlockState state) {
        super(GotDam.BE_TYPE_CONTROLLER, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state1, ControllerBlockEntity controller) {
        if (world instanceof ServerWorld serverWorld && controller.energyStorage.amount >= 100) {
            controller.energyStorage.amount -= 100;
            controller.markDirty();
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        energyStorage.amount = tag.getLong("storedEnergy");
    }

    @Override
    protected void writeNbt(NbtCompound tag) {
        tag.putLong("storedEnergy", energyStorage.amount);

        super.writeNbt(tag);
    }


    private void forceUpdate() {
        if (world != null) {
            BlockState state = world.getBlockState(pos);
            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        }
    }
}
