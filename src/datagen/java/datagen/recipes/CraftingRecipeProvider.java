package datagen.recipes;

import datagen.tags.ItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.system.CallbackI;
import us.dison.gotdam.GotDam;

import java.util.function.Consumer;

public class CraftingRecipeProvider extends FabricRecipeProvider {

    public CraftingRecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(GotDam.ITEM_CONTROLLER)
                .criterion("has_aluminum_plates", RecipeProvider.conditionsFromTag(ItemTagProvider.ALUMINUM_PLATES))
                .input('r', ItemTagProvider.REDSTONE_DUSTS)
                .input('a', ItemTagProvider.ALUMINUM_PLATES)
                .input('s', ItemTagProvider.STEEL_PLATES)
                .input('c', Registry.ITEM.get(new Identifier("techreborn:advanced_circuit")))
                .input('m', Registry.ITEM.get(new Identifier("techreborn:advanced_machine_frame")))
                .input('g', Registry.ITEM.get(new Identifier("techreborn:insulated_gold_cable")))
                .pattern("rar")
                .pattern("sms")
                .pattern("gcg")
            .offerTo(exporter);
    }
}
