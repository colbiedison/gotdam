package us.dison.gotdam.client;

import me.x150.renderer.event.EventListener;
import me.x150.renderer.event.EventType;
import me.x150.renderer.event.Shift;
import me.x150.renderer.event.events.RenderEvent;
import me.x150.renderer.renderer.MSAAFramebuffer;
import me.x150.renderer.renderer.Renderer2d;
import me.x150.renderer.renderer.Renderer3d;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import us.dison.gotdam.scan.Dam;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RendererEventHandler {
    @EventListener(shift = Shift.POST, type = EventType.WORLD_RENDER)
    void postWorldRender(RenderEvent renderEvent) {
//        MSAAFramebuffer.use(MSAAFramebuffer.MAX_SAMPLES, () -> {
            ArrayList<Long> blocks = new ArrayList<>();
            for (Dam dam : GotDamClient.PREVIEW_DAMS) {
                for (Long longBlock : dam.getScan().getArea().getInnerBlocks()) {
                    BlockPos pos = BlockPos.fromLong(longBlock);
                    if (pos.getY() == dam.getScan().getArea().getTopLevel()) {
                        Renderer3d.renderOutline(
                                renderEvent.getStack(),
                                new Vec3d(pos.getX(), pos.getY(), pos.getZ()),
                                new Vec3d(1, 1, 1),
                                new Color(0, 0, 255, 127)
                        );
                    }
                }
            }
//        });
    }
}
