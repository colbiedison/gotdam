package us.dison.gotdam.blockentity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;
import us.dison.gotdam.GotDam;
import us.dison.gotdam.inventory.ImplementedInventory;
import us.dison.gotdam.screen.ControllerGuiDescription;

public class ControllerBlockEntity extends BlockEntity implements ImplementedInventory, InventoryProvider, PropertyDelegateHolder, NamedScreenHandlerFactory, SidedInventory {

    public static final long ENERGY_CAPACITY = 500000;
    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(ENERGY_CAPACITY, 2000, 0) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public double scanProgress = 0;
    private boolean scanning = false;
    private boolean enabled = false;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> (int) energyStorage.amount;
                case 1 -> (int) scanProgress;
                case 2 -> scanning ? 1 : 0;
                case 3 -> enabled ? 1 : 0;

                default -> -1;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> setStoredEnergy(value);
                case 1 -> setScanProgress(value);
                case 2 -> setScanning(value != 0);
                case 3 -> setEnabled(value != 0);
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public ControllerBlockEntity(BlockPos pos, BlockState state) {
        super(GotDam.BE_TYPE_CONTROLLER, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state1, ControllerBlockEntity controller) {
        if (controller.enabled && controller.energyStorage.amount >= 100 && world instanceof ServerWorld serverWorld) {
            controller.energyStorage.amount -= 100;
            if (controller.scanProgress < 100) {
                controller.scanProgress++;
            }
            else {
                controller.scanProgress = 0;
            }
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
        Inventories.readNbt(tag, items);
        scanning = tag.getBoolean("scanning");
        enabled = tag.getBoolean("enabled");
    }

    @Override
    protected void writeNbt(NbtCompound tag) {
        tag.putLong("storedEnergy", energyStorage.amount);
        Inventories.writeNbt(tag, items);
        tag.putBoolean("scanning", scanning);
        tag.putBoolean("enabled", enabled);

        super.writeNbt(tag);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }


    private void forceUpdate() {
        if (world != null) {
            BlockState state = world.getBlockState(pos);
            if (!world.isClient)
                world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return this;
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("block.gotdam.controller");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ControllerGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[] {0, 1};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    public void setStoredEnergy(long amount) {
        this.energyStorage.amount = amount;
        markDirty();
    }

    public void setScanProgress(int progress) {
        this.scanProgress = progress;
        markDirty();
    }

    public void setScanning(boolean state) {
        this.scanning = state;
        markDirty();
    }

    public void setEnabled(boolean state) {
        this.enabled = state;
        markDirty();
    }
}
