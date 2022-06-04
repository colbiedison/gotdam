package datagen.models;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.TexturedModel;
import net.minecraft.util.Identifier;
import us.dison.gotdam.GotDam;

import static us.dison.gotdam.GotDam.*;

public class ModelProvider extends FabricModelProvider {

    public ModelProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        GotDam.LOGGER.info("Generate Block Models");
        generator.registerSouthDefaultHorizontalFacing(TexturedModel.ORIENTABLE_WITH_BOTTOM, BLOCK_CONTROLLER);
        generator.registerParentedItemModel(ITEM_CONTROLLER, new Identifier(MODID, "block/"+ID_CONTROLLER.getPath()));
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        GotDam.LOGGER.info("Generate Item Models");
//        generator.register(ITEM_CONTROLLER, Models.ORIENTABLE_WITH_BOTTOM);
    }
}
