package datagen;

import datagen.recipes.CraftingRecipeProvider;
import datagen.tags.ItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import datagen.models.ModelProvider;

public class GotDamDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(ModelProvider::new);
        fabricDataGenerator.addProvider(CraftingRecipeProvider::new);
        fabricDataGenerator.addProvider(ItemTagProvider::new);
    }
}
