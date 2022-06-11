package us.dison.gotdam.client;

import me.x150.renderer.event.EventListener;
import me.x150.renderer.event.EventType;
import me.x150.renderer.event.Shift;
import me.x150.renderer.event.events.RenderEvent;
import me.x150.renderer.renderer.RendererUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import us.dison.gotdam.mixin.client.GameRendererMixin;
import us.dison.gotdam.scan.Dam;

import java.awt.*;

public class RendererEventHandler {
    private static VertexBuffer PREVIEW_VBO = null;

    @EventListener(shift = Shift.POST, type = EventType.WORLD_RENDER)
    void postWorldRender(RenderEvent renderEvent) {
        if (PREVIEW_VBO == null) return;
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RendererUtils.setupRender();
        renderEvent.getStack().push();
        Camera c = MinecraftClient.getInstance().gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        double fov = ((GameRendererMixin)MinecraftClient.getInstance().gameRenderer).invokeGetFov(c, MinecraftClient.getInstance().getTickDelta(), true);

        renderEvent.getStack().translate(-camPos.x, -camPos.y, -camPos.z);
        PREVIEW_VBO.bind();
        PREVIEW_VBO.draw(renderEvent.getStack().peek().getPositionMatrix(), MinecraftClient.getInstance().gameRenderer.getBasicProjectionMatrix(fov), GameRenderer.getPositionColorShader());
        VertexBuffer.unbind();
        renderEvent.getStack().pop();
        RendererUtils.endRender();
    }

    public static void rebuildPreviews() {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        Color color = Color.BLUE;
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();

        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        for (Dam dam : GotDamClient.getPreviewDams()) {
            for (Long longBlock : dam.getScan().getArea().getInnerBlocks()) {
                BlockPos pos = BlockPos.fromLong(longBlock);
                if (pos.getY() == dam.getScan().getArea().getTopLevel()) {
                    Vec3d start = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                    Vec3d end = start.add(1, 1, 1);
                    float x1 = (float) start.x;
                    float y1 = (float) start.y;
                    float z1 = (float) start.z;
                    float x2 = (float) end.x;
                    float y2 = (float) end.y;
                    float z2 = (float) end.z;

                    // Bottom
                    buffer.vertex(x1, y1, z1).color(r, g, b, a).next();
                    buffer.vertex(x1, y1, z2).color(r, g, b, a).next();

                    buffer.vertex(x1, y1, z2).color(r, g, b, a).next();
                    buffer.vertex(x2, y1, z2).color(r, g, b, a).next();

                    buffer.vertex(x2, y1, z1).color(r, g, b, a).next();
                    buffer.vertex(x1, y1, z1).color(r, g, b, a).next();

                    buffer.vertex(x2, y1, z2).color(r, g, b, a).next();
                    buffer.vertex(x2, y1, z1).color(r, g, b, a).next();

                    // Top
                    buffer.vertex(x1, y2, z1).color(r, g, b, a).next();
                    buffer.vertex(x1, y2, z2).color(r, g, b, a).next();

                    buffer.vertex(x1, y2, z2).color(r, g, b, a).next();
                    buffer.vertex(x2, y2, z2).color(r, g, b, a).next();

                    buffer.vertex(x2, y2, z1).color(r, g, b, a).next();
                    buffer.vertex(x1, y2, z1).color(r, g, b, a).next();

                    buffer.vertex(x2, y2, z2).color(r, g, b, a).next();
                    buffer.vertex(x2, y2, z1).color(r, g, b, a).next();

                    // Vertical
                    buffer.vertex(x1, y1, z1).color(r, g, b, a).next();
                    buffer.vertex(x1, y2, z1).color(r, g, b, a).next();

                    buffer.vertex(x1, y1, z2).color(r, g, b, a).next();
                    buffer.vertex(x1, y2, z2).color(r, g, b, a).next();

                    buffer.vertex(x2, y1, z1).color(r, g, b, a).next();
                    buffer.vertex(x2, y2, z1).color(r, g, b, a).next();

                    buffer.vertex(x2, y1, z2).color(r, g, b, a).next();
                    buffer.vertex(x2, y2, z2).color(r, g, b, a).next();
                }
            }
        }

        PREVIEW_VBO = new VertexBuffer();
        PREVIEW_VBO.bind();
        PREVIEW_VBO.upload(buffer.end());
        VertexBuffer.unbind();
    }
}
