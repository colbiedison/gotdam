package us.dison.gotdam.client;

import me.x150.renderer.event.Events;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.util.math.BlockPos;
import us.dison.gotdam.GotDam;
import us.dison.gotdam.network.BasePacket;
import us.dison.gotdam.network.BasePacketHandler;
import us.dison.gotdam.scan.Dam;
import us.dison.gotdam.screen.ControllerGuiDescription;
import us.dison.gotdam.screen.ControllerScreen;

import java.util.ArrayList;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class GotDamClient implements ClientModInitializer {

    private static final ArrayList<Dam> PREVIEW_DAMS = new ArrayList<>();

    private static BlockPos openControllerPos = null;

    @Override
    public void onInitializeClient() {
        //noinspection RedundantTypeArguments
        ScreenRegistry.<ControllerGuiDescription, ControllerScreen>register(GotDam.SCREEN_HANDLER_TYPE, (gui, inventory, title) -> new ControllerScreen(gui, inventory.player, title));
        registerNetworking();
        Events.registerEventHandlerClass(new RendererEventHandler());
    }

    private void registerNetworking() {
        for (BasePacketHandler.PacketTypes type : BasePacketHandler.PacketTypes.values()) {
            ClientPlayNetworking.registerGlobalReceiver(BasePacket.CHANNEL, (client, handler, payload, responseSender) -> {
                final int packetType = payload.readInt();
                final BasePacket packet = BasePacketHandler.PacketTypes.getPacket(packetType).parsePacket(payload);
                client.execute(() -> {
                    packet.handleOnClient(client.player);
                });
            });
        }
    }

    public static void onOpenController(BlockPos pos) {
        openControllerPos = pos;
    }

    public static BlockPos getOpenControllerPos() {
        return openControllerPos;
    }

    public static ArrayList<Dam> getPreviewDams() {
        return new ArrayList<>(PREVIEW_DAMS);
    }

    public static void addPreviewDam(Dam d) {
        PREVIEW_DAMS.add(d);
        RendererEventHandler.rebuildPreviews();
    }

    public static void removePreviewDamIf(Predicate<? super Dam> filter) {
        PREVIEW_DAMS.removeIf(filter);
        RendererEventHandler.rebuildPreviews();
    }
}
