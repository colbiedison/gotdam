package us.dison.gotdam.screen;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import us.dison.gotdam.GotDam;
import us.dison.gotdam.blockentity.ControllerBlockEntity;

public class ControllerGuiDescription extends SyncedGuiDescription {

    private static final int INVENTORY_SIZE = 2;

    public ControllerGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(GotDam.SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context, 3));

//        WGridPanel root = new WGridPanel();
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(150, 200);
        root.setInsets(Insets.NONE);

        // Block inventory
        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
        root.add(itemSlot, 10, 20);

        // Scan button
        WToggleButton toggleScanButton = new WToggleButton();
        // attempt 1
        toggleScanButton.setOnToggle(state -> getPropertyDelegate().set(2, state ? 1 : 0));
//        // attempt 2
//        toggleScanButton.setOnToggle(state ->
//            context.run((world1, pos) -> {
//                if (world1.getBlockEntity(pos) instanceof ControllerBlockEntity controller) {
//                    controller.setScanning(state);
//                }
//        }));

        root.add(toggleScanButton, 50 ,50);

        // Energy storage bar
        WBar energyStorageBar = WBar.withConstantMaximum(
                new Identifier("gotdam:textures/gui/controller/scan_progress_bg.png"),
                new Identifier("gotdam:textures/gui/controller/scan_progress_fg.png"),
                0, (int) ControllerBlockEntity.ENERGY_CAPACITY,
                WBar.Direction.UP
        );
        root.add(energyStorageBar, 155, 20);
        energyStorageBar.setSize(8,64);

        // Scan progress bar
        WBar scanProgressBar = WBar.withConstantMaximum(
                new Identifier("gotdam:textures/gui/controller/scan_progress_bg.png"),
                new Identifier("gotdam:textures/gui/controller/scan_progress_fg.png"),
                1, 100,
                WBar.Direction.UP
        );
        root.add(scanProgressBar, 140, 20);
        scanProgressBar.setSize(8,64);

        // Player inventory
        WPlayerInvPanel playerInvPanel = this.createPlayerInventoryPanel();
        playerInvPanel.setInsets(new Insets(0, 7));
        root.add(playerInvPanel, 0, 100);

        root.validate(this);
    }
}
