package us.dison.gotdam.client;

import me.x150.renderer.event.EventListener;
import me.x150.renderer.event.EventType;
import me.x150.renderer.event.Shift;
import me.x150.renderer.event.events.RenderEvent;
import me.x150.renderer.renderer.MSAAFramebuffer;
import me.x150.renderer.renderer.Renderer3d;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class RendererEventHandler {
    @EventListener(shift = Shift.POST, type = EventType.WORLD_RENDER)
    void postWorldRender(RenderEvent renderEvent) {
        MSAAFramebuffer.use(MSAAFramebuffer.MAX_SAMPLES, () -> {
            Renderer3d.renderOutline(renderEvent.getStack(), new Vec3d(0,0,0), new Vec3d(1, 1, 1), Color.BLUE);
        });
    }
}
