package us.dison.gotdam;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.reborn.energy.api.EnergyStorage;
import us.dison.gotdam.block.ControllerBlock;
import us.dison.gotdam.blockentity.ControllerBlockEntity;
import us.dison.gotdam.data.DamManager;
import us.dison.gotdam.network.BasePacket;
import us.dison.gotdam.network.BasePacketHandler;
import us.dison.gotdam.screen.ControllerGuiDescription;

public class GotDam implements ModInitializer {
	public static final String MODID = "gotdam";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final Identifier ID_CONTROLLER = new Identifier(MODID, "controller");
	public static final Block BLOCK_CONTROLLER = new ControllerBlock(FabricBlockSettings.of(Material.METAL));
	public static final Item ITEM_CONTROLLER = new BlockItem(BLOCK_CONTROLLER, new FabricItemSettings().group(ItemGroup.MISC));
	public static final BlockEntityType<ControllerBlockEntity> BE_TYPE_CONTROLLER = FabricBlockEntityTypeBuilder.create(ControllerBlockEntity::new, BLOCK_CONTROLLER).build(null);

	public static ScreenHandlerType<ControllerGuiDescription> SCREEN_HANDLER_TYPE = null;
	@Override
	public void onInitialize() {
		LOGGER.info("Got dam?");

		registerBlocks();
		registerItems();
		registerBlockEntities();
		registerEnergyStorage();
		registerNetworking();

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			for (ServerWorld world : server.getWorlds()) {
				DamManager.MANAGERS.put(world.getRegistryKey().getValue(), world.getPersistentStateManager().getOrCreate(DamManager::fromTag, DamManager::new, DamManager.KEY));
			}
		});

		SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(ID_CONTROLLER, (syncId, inventory) -> new ControllerGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY));

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

	private void registerNetworking() {
		for (BasePacketHandler.PacketTypes type : BasePacketHandler.PacketTypes.values()) {
			ServerPlayNetworking.registerGlobalReceiver(BasePacket.CHANNEL, (server, player, handler, payload, responseSender) -> {
				final int packetType = payload.readInt();
				final BasePacket packet = BasePacketHandler.PacketTypes.getPacket(packetType).parsePacket(payload);
				server.execute(() -> {
					packet.handleOnServer(player);
				});
			});
		}
	}
}
