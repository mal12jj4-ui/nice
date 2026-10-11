package com.example.myclient.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

/** Draws boxes through walls. Call begin() once, box() as often as needed, then end(). */
public final class Render3D {
    private Render3D() {}

    public static void begin() {
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.lineWidth(2.0f);
    }

    public static void end() {
        RenderSystem.lineWidth(1.0f);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
    }

    public static void box(Matrix4f m, Vec3d cam, Box b, int argb, boolean fill) {
        float x1 = (float) (b.minX - cam.x), y1 = (float) (b.minY - cam.y), z1 = (float) (b.minZ - cam.z);
        float x2 = (float) (b.maxX - cam.x), y2 = (float) (b.maxY - cam.y), z2 = (float) (b.maxZ - cam.z);

        if (fill) {
            int fc = (argb & 0x00FFFFFF) | (0x2E << 24);
            BufferBuilder q = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            quad(q, m, fc, x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2); // bottom
            quad(q, m, fc, x1, y2, z1, x2, y2, z1, x2, y2, z2, x1, y2, z2); // top
            quad(q, m, fc, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1); // north
            quad(q, m, fc, x1, y1, z2, x1, y2, z2, x2, y2, z2, x2, y1, z2); // south
            quad(q, m, fc, x1, y1, z1, x1, y2, z1, x1, y2, z2, x1, y1, z2); // west
            quad(q, m, fc, x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2); // east
            BufferRenderer.drawWithGlobalProgram(q.end());
        }

        BufferBuilder l = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        // bottom ring
        line(l, m, argb, x1, y1, z1, x2, y1, z1);
        line(l, m, argb, x2, y1, z1, x2, y1, z2);
        line(l, m, argb, x2, y1, z2, x1, y1, z2);
        line(l, m, argb, x1, y1, z2, x1, y1, z1);
        // top ring
        line(l, m, argb, x1, y2, z1, x2, y2, z1);
        line(l, m, argb, x2, y2, z1, x2, y2, z2);
        line(l, m, argb, x2, y2, z2, x1, y2, z2);
        line(l, m, argb, x1, y2, z2, x1, y2, z1);
        // verticals
        line(l, m, argb, x1, y1, z1, x1, y2, z1);
        line(l, m, argb, x2, y1, z1, x2, y2, z1);
        line(l, m, argb, x2, y1, z2, x2, y2, z2);
        line(l, m, argb, x1, y1, z2, x1, y2, z2);
        BufferRenderer.drawWithGlobalProgram(l.end());
    }

    private static void line(BufferBuilder b, Matrix4f m, int c,
                             float ax, float ay, float az, float bx, float by, float bz) {
        b.vertex(m, ax, ay, az).color(c);
        b.vertex(m, bx, by, bz).color(c);
    }

    private static void quad(BufferBuilder b, Matrix4f m, int c,
                             float ax, float ay, float az, float bx, float by, float bz,
                             float cx, float cy, float cz, float dx, float dy, float dz) {
        b.vertex(m, ax, ay, az).color(c);
        b.vertex(m, bx, by, bz).color(c);
        b.vertex(m, cx, cy, cz).color(c);
        b.vertex(m, dx, dy, dz).color(c);
    }
}
