package us.dison.gotdam.data;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import us.dison.gotdam.scan.Dam;
import us.dison.gotdam.scan.DamArea;
import us.dison.gotdam.scan.DamScanResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DamManager extends PersistentState {
    public static final HashMap<Identifier, DamManager> MANAGERS = new HashMap<>();
    public static final String KEY = "gotdam_dams";

    private static final Codec<List<Dam>> CODEC = Codec.list(Dam.CODEC);

    private final ServerWorld world = null;
    private final List<Dam> dams = new ArrayList<>();

    public DamManager() {
    }

    public static DamManager ofWorld(ServerWorld world) {
        Identifier worldID = world.getRegistryKey().getValue();
        if (MANAGERS.containsKey(worldID))
            return MANAGERS.get(worldID);
        else {
            DamManager manager = world.getPersistentStateManager().getOrCreate(DamManager::fromTag, DamManager::new, KEY);
            MANAGERS.put(worldID, manager);
            return manager;
        }
    }

    public static DamManager fromTag(NbtCompound tag) {
        DamManager manager = new DamManager();
        List<Dam> dams = CODEC.parse(NbtOps.INSTANCE, tag.getList("dams", NbtElement.COMPOUND_TYPE))
                .result()
                .orElse(Collections.emptyList());
        manager.dams.addAll(dams);

        return manager;
    }


    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        CODEC.encodeStart(NbtOps.INSTANCE, dams)
                .result()
                .ifPresent(damsTag -> tag.put("dams", damsTag));
        return tag;
    }

    public Dam get(int id) {
        for (Dam dam : dams) {
            if (dam.getID() == id)
                return dam;
        }
        return null;
    }
    public Dam get(BlockPos pos) {
        for (Dam dam : dams) {
            if (dam.getScan().getArea().getControllerPos().asLong() == pos.asLong())
                return dam;
        }
        return null;
    }

    public Dam getOrCreate(Identifier worldID, BlockPos pos) {
        Dam existingDam = get(pos);
        if (existingDam != null)
            return existingDam;
        else {
            Dam newDam = new Dam(nextID(), DamScanResult.notRunYet(new DamArea(worldID, pos)));
            set(newDam);
            return newDam;
        }
    }

    public int nextID() {
        return dams.size();
    }

    public void add(Dam dam) {
        dams.add(dam);
        markDirty();
    }

    public void remove(int id) {
        dams.remove(get(id));
        markDirty();
    }
    public void remove(Dam dam) {
        remove(dam.getID());
    }

    public void set(Dam dam) {
        dams.removeIf(dam1 -> dam1.getID() == dam.getID());
        dams.add(dam);
        markDirty();
    }
}
