/*
 * This file was copied and modified from Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 * Modifications are Copyright (c) 2022, ColbiesTheName.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package us.dison.gotdam.network;

import net.minecraft.network.PacketByteBuf;
import us.dison.gotdam.network.packets.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BasePacketHandler {

    private static final Map<Class<? extends BasePacket>, PacketTypes> REVERSE_LOOKUP = new HashMap<>();

    public enum PacketTypes {
        CONTROLLER_SCAN_TOGGLE(ControllerScanTogglePacket.class, ControllerScanTogglePacket::new),
        CONTROLLER_POWER_TOGGLE(ControllerPowerTogglePacket.class, ControllerPowerTogglePacket::new),
        CONTROLLER_PREVIEW_TOGGLE(ControllerPreviewTogglePacket.class, ControllerPreviewTogglePacket::new),
        DAM_PREVIEW(DamPreviewPacket.class, DamPreviewPacket::new),
        REBUILD_PREVIEW(PreviewRebuildPacket.class, PreviewRebuildPacket::new)
        ;

        private final Function<PacketByteBuf, BasePacket> factory;

        PacketTypes(Class<? extends BasePacket> packetClass, Function<PacketByteBuf, BasePacket> factory) {
            this.factory = factory;

            REVERSE_LOOKUP.put(packetClass, this);
        }

        public static PacketTypes getPacket(int id) {
            return values()[id];
        }

        public int getPacketId() {
            return ordinal();
        }

        static PacketTypes getID(Class<? extends BasePacket> c) {
            return REVERSE_LOOKUP.get(c);
        }

        public BasePacket parsePacket(PacketByteBuf in) throws IllegalArgumentException {
            return this.factory.apply(in);
        }

    }
}
