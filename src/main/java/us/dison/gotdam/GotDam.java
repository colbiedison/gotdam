package us.dison.gotdam;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.system.CallbackI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.reborn.energy.api.EnergyStorage;
import us.dison.gotdam.block.ControllerBlock;
import us.dison.gotdam.blockentity.ControllerBlockEntity;

import javax.annotation.Nullable;

public class GotDam implements ModInitializer {
	public static final String MODID = "gotdam";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final Identifier ID_CONTROLLER = new Identifier(MODID, "controller");
	public static final Block BLOCK_CONTROLLER = new ControllerBlock(FabricBlockSettings.of(Material.METAL));
	public static final Item ITEM_CONTROLLER = new BlockItem(BLOCK_CONTROLLER, new FabricItemSettings().group(ItemGroup.MISC));
	public static final BlockEntityType<ControllerBlockEntity> BE_TYPE_CONTROLLER = FabricBlockEntityTypeBuilder.create(ControllerBlockEntity::new, BLOCK_CONTROLLER).build(null);

	@Override
	public void onInitialize() {
		LOGGER.info("Got dam?");

		registerBlocks();
		registerItems();
		registerBlockEntities();
		registerEnergyStorage();
	}

	private void registerBlocks() {
		LOGGER.info("Register BLOCK");
		Registry.register(Registry.BLOCK, ID_CONTROLLER, BLOCK_CONTROLLER);
	}

	private void registerItems() {
		LOGGER.info("Register ITEM");
		Registry.register(Registry.ITEM, ID_CONTROLLER, ITEM_CONTROLLER);
	}

	private void registerBlockEntities() {
		Registry.register(Registry.BLOCK_ENTITY_TYPE, ID_CONTROLLER, BE_TYPE_CONTROLLER);
	}

	private void registerEnergyStorage() {
		EnergyStorage.SIDED.registerForBlockEntity((controllerEntity, direction) -> controllerEntity.energyStorage, BE_TYPE_CONTROLLER);
	}
}
