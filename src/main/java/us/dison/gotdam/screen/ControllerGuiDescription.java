package us.dison.gotdam.screen;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import us.dison.gotdam.GotDam;
import us.dison.gotdam.blockentity.ControllerBlockEntity;
import us.dison.gotdam.client.GotDamClient;
import us.dison.gotdam.network.BasePacket;
import us.dison.gotdam.network.packets.ControllerPowerTogglePacket;
import us.dison.gotdam.network.packets.ControllerPreviewTogglePacket;
import us.dison.gotdam.network.packets.ControllerScanTogglePacket;

public class ControllerGuiDescription extends SyncedGuiDescription {

    private static final int INVENTORY_SIZE = 2;

    public ControllerGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(GotDam.SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context, 7));

//        WGridPanel root = new WGridPanel();
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(150, 200);
        root.setInsets(Insets.NONE);

        // Block inventory
        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
        root.add(itemSlot, 10, 20);

        // Scan control panel
        WPlainPanel scanPanel = new WPlainPanel();
        {
            // Title
            WText title = new WText(Text.literal("Scanner"));
            scanPanel.add(title, 0, 0);

            // Scan progress bar
            WBar scanProgressBar = WBar.withConstantMaximum(
                    new Identifier("gotdam:textures/gui/controller/scan_progress_bg.png"),
                    new Identifier("gotdam:textures/gui/controller/scan_progress_fg.png"),
                    1, 100,
                    WBar.Direction.RIGHT
            );
            scanPanel.add(scanProgressBar, 2, 10);
            scanProgressBar.setSize(64, 8);

            // Scan start button
            WButton startScanButton = new WButton();
            startScanButton.setOnClick(() ->
                    ClientPlayNetworking.send(BasePacket.CHANNEL, new ControllerScanTogglePacket(GotDamClient.getOpenControllerPos(), true).getPayload())
            );
            startScanButton.setLabel(Text.literal("Scan"));
            scanPanel.add(startScanButton, 0, 23);
            startScanButton.setSize(32, 8);

            // Scan stop button
            WButton stopScanButton = new WButton();
            stopScanButton.setOnClick(() ->
                    ClientPlayNetworking.send(BasePacket.CHANNEL, new ControllerScanTogglePacket(GotDamClient.getOpenControllerPos(), false).getPayload())
            );
            stopScanButton.setLabel(Text.literal("Stop").formatted(Formatting.RED));
            scanPanel.add(stopScanButton, 36, 23);
            stopScanButton.setSize(32, 8);

            title.setSize(scanPanel.getWidth()+15, 16);
            title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        }
        root.add(scanPanel, 70, 10);

        // Info panel
        WPlainPanel infoPanel = new WPlainPanel();
        infoPanel.setSize(128, 50);
        {
            // Title
            WText title = new WText(Text.literal("Info"));
            infoPanel.add(title, 0, 0);
            title.setSize(infoPanel.getWidth()/2, 8);
            title.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // Status
            SyncedWText status = new SyncedWText(Text.literal("Status: "), 4);
            status.forScanResult();
            infoPanel.add(status, 0, 10);
            status.setSize(infoPanel.getWidth(), 16);

            // Size
            SyncedWText size = new SyncedWText(Text.literal("Size: "), 6);
            infoPanel.add(size, 0, 20);
            size.setSize(infoPanel.getWidth(), 16);

            // Top level
            SyncedWText topLevel = new SyncedWText(Text.literal("Max y: "), 5);
            infoPanel.add(topLevel, 0, 30);
            topLevel.setSize(infoPanel.getWidth(), 16);
        }
        root.add(infoPanel, 10, 50);

        // Power button
        SyncedWToggleButton powerToggleButton = new SyncedWToggleButton(3);
        powerToggleButton.setOnToggle(state -> {
            ClientPlayNetworking.send(BasePacket.CHANNEL, new ControllerPowerTogglePacket(GotDamClient.getOpenControllerPos(), state).getPayload());
        });
        root.add(powerToggleButton, 143 ,3);
//        powerToggleButton.addTooltip(new TooltipBuilder().add(new TranslatableText("gui.gotdam.button.power")));

        // Energy storage bar
        WBar energyStorageBar = WBar.withConstantMaximum(
                new Identifier("gotdam:textures/gui/controller/energy_bar_bg.png"),
                new Identifier("gotdam:textures/gui/controller/energy_bar_fg.png"),
                0, (int) ControllerBlockEntity.ENERGY_CAPACITY,
                WBar.Direction.UP
        );
        root.add(energyStorageBar, 155, 20);
        energyStorageBar.setSize(8,64);

        // Preview toggle button
        WToggleButton previewToggleButton = new WToggleButton();
        previewToggleButton.setToggle(GotDamClient.getPreviewDams().stream().anyMatch(dam ->
                dam.getScan().getArea().getControllerPos().asLong() == GotDamClient.getOpenControllerPos().asLong())
        );
        previewToggleButton.setOnToggle(state -> {
            ClientPlayNetworking.send(BasePacket.CHANNEL, new ControllerPreviewTogglePacket(GotDamClient.getOpenControllerPos(), state).getPayload());
        });
        root.add(previewToggleButton, 143, 85);

        // Player inventory
        WPlayerInvPanel playerInvPanel = this.createPlayerInventoryPanel();
        playerInvPanel.setInsets(new Insets(0, 7));
        root.add(playerInvPanel, 0, 100);

        root.validate(this);
    }
}
