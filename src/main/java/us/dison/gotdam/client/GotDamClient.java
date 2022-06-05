package us.dison.gotdam.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import us.dison.gotdam.GotDam;
import us.dison.gotdam.network.BasePacket;
import us.dison.gotdam.network.BasePacketHandler;
import us.dison.gotdam.screen.ControllerGuiDescription;
import us.dison.gotdam.screen.ControllerScreen;

@Environment(EnvType.CLIENT)
public class GotDamClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.<ControllerGuiDescription, ControllerScreen>register(GotDam.SCREEN_HANDLER_TYPE, (gui, inventory, title) -> new ControllerScreen(gui, inventory.player, title));
        registerNetworking();
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
}
