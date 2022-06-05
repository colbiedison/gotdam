/*
 * This file contains modified code from LibGui.
 *
 * LibGui is licensed under the MIT License.
 * LibGui Copyright (c) 2018-2021 The Cotton Project
 *
 */

package us.dison.gotdam.screen;

import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SyncedWToggleButton extends WToggleButton {
    private int field;
    public SyncedWToggleButton(int field) {
        super();
        this.field = field;
    }

    public SyncedWToggleButton(Text label, int field) {
        super(label);
        this.field = field;
    }

    public SyncedWToggleButton(Identifier onImage, Identifier offImage, int field) {
        super(onImage, offImage);
        this.field = field;
    }

    public SyncedWToggleButton(Identifier onImage, Identifier offImage, Text label, int field) {
        super(onImage, offImage, label);
        this.field = field;
    }

    public SyncedWToggleButton(Texture onImage, Texture offImage, int field) {
        super(onImage, offImage);
        this.field = field;
    }

    public SyncedWToggleButton(Texture onImage, Texture offImage, Text label, int field) {
        super(onImage, offImage, label);
        this.field = field;
    }

//    @Override
//    public void tick() {
//        super.tick();
//        setToggle(host.getPropertyDelegate().get(2) != 0);
//    }
    @Override
    @Environment(EnvType.CLIENT)
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        try {
            ScreenDrawing.texturedRect(matrices, x, y, 18, 18, host.getPropertyDelegate().get(field) != 0 ? this.onImage : this.offImage, -1);
        } catch (NullPointerException e) {
            super.paint(matrices, x, y, mouseX, mouseY);
            return;
        }

        if (this.isFocused()) {
            ScreenDrawing.texturedRect(matrices, x, y, 18, 18, this.focusImage, -1);
        }

        if (this.label != null) {
            ScreenDrawing.drawString(matrices, this.label.asOrderedText(), x + 22, y + 6, LibGui.isDarkMode() ? this.darkmodeColor : this.color);
        }
    }

    @Override
    public void tick() {
        super.tick();
        try {
            if (getToggle() == (host.getPropertyDelegate().get(field) == 0)) {
                setToggle(!getToggle());
            }
        } catch (NullPointerException ignored) {}
    }
}
