package us.dison.gotdam.client;

import com.mojang.blaze3d.systems.RenderSystem;
import me.x150.renderer.event.Events;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import us.dison.gotdam.GotDam;
import us.dison.gotdam.network.BasePacket;
import us.dison.gotdam.network.BasePacketHandler;
import us.dison.gotdam.screen.ControllerGuiDescription;
import us.dison.gotdam.screen.ControllerScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class GotDamClient implements ClientModInitializer {

    public static final ArrayList<Long> PREVIEW_BLOCKS = new ArrayList<>();

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
}
