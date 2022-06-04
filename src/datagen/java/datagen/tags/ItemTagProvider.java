package datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public static final TagKey<Item> REDSTONE_DUSTS =   TagKey.of(Registry.ITEM_KEY, new Identifier("c:redstone_dusts"));
    public static final TagKey<Item> ALUMINUM_PLATES =  TagKey.of(Registry.ITEM_KEY, new Identifier("c:aluminum_plates"));
    public static final TagKey<Item> STEEL_PLATES =     TagKey.of(Registry.ITEM_KEY, new Identifier("c:steel_plates"));

    public ItemTagProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateTags() {
        getOrCreateTagBuilder(REDSTONE_DUSTS).add(Items.REDSTONE);
    }
}
