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

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public abstract class BasePacket {
    public static final Identifier CHANNEL = new Identifier("gda:m");

    private PacketByteBuf p;

    public final int getPacketID() {
        return BasePacketHandler.PacketTypes.getID(this.getClass()).ordinal();
    }

    public void handleOnClient(PlayerEntity player) {
        throw new UnsupportedOperationException("The client can't create this packet. ("+this.getPacketID()+")");
    }

    public void handleOnServer(ServerPlayerEntity player) {
        throw new UnsupportedOperationException("The server can't create this packet. ("+this.getPacketID()+")");
    }

    protected void configureWrite(PacketByteBuf data) {
        data.capacity(data.readableBytes());
        this.p = data;
    }

    public PacketByteBuf getPayload() {
        return this.p;
    }

    public Packet<?> toPacket(NetworkSide direction) {
        if (direction == NetworkSide.SERVERBOUND)
            return ClientPlayNetworking.createC2SPacket(CHANNEL, this.p);
        else
            return ServerPlayNetworking.createS2CPacket(CHANNEL, this.p);
    }
}
