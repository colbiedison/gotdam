package us.dison.gotdam.scan;

import com.mojang.serialization.Codec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractArea implements ICodecProvider {

    protected final Identifier world;
    protected final BlockPos controllerPos;
    protected final List<Long> innerBlocks;

    public AbstractArea(Identifier world, BlockPos controllerPos) {
        this(world, controllerPos, new ArrayList<>());
    }

    public AbstractArea(Identifier world, BlockPos controllerPos, List<Long> blocks) {
        this.world = world;
        this.controllerPos = controllerPos;
        this.innerBlocks = blocks;
    }

    public static ServerWorld getWorld(MinecraftServer server, AbstractArea area) {
        return server.getWorld(RegistryKey.of(Registry.WORLD_KEY, area.getWorldID()));
    }

    public Identifier getWorldID() {
        return world;
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    public List<Long> getInnerBlocks() {
        return innerBlocks;
    }

    @Override
    public abstract Codec<?> getCodec();

    public enum AreaType implements StringIdentifiable {
        DAM("dam");

        private final String name;

        AreaType(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return name;
        }

        public static AreaType fromName(String name) {
            for (AreaType value : values()) {
                if (value.name.equals(name))
                    return value;
            }
            return null;
        }
    }
}
