package us.dison.gotdam.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class ControllerScreen extends CottonInventoryScreen<ControllerGuiDescription> {
    public ControllerScreen(ControllerGuiDescription gui, PlayerEntity player, Text title) {
        super(gui, player, title);
    }
}
