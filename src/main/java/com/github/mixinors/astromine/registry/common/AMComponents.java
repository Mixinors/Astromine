/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.component.entity.OxygenComponent;
import com.github.mixinors.astromine.common.component.world.HoloBridgesComponent;
import com.github.mixinors.astromine.common.component.world.NetworksComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class AMComponents {
	private static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, AMCommon.MOD_ID);
	
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<NetworksComponent>> NETWORKS = REGISTRY.register("networks", () -> AttachmentType.builder(holder -> new NetworksComponent((Level) holder)).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<HoloBridgesComponent>> HOLO_BRIDGES = REGISTRY.register("holo_bridges", () -> AttachmentType.builder(holder -> new HoloBridgesComponent((Level) holder)).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<OxygenComponent>> OXYGEN_COMPONENT = REGISTRY.register("oxygen", () -> AttachmentType.builder(holder -> new OxygenComponent((Entity) holder)).serialize(new IAttachmentSerializer<CompoundTag, OxygenComponent>() {
		@Override
		public OxygenComponent read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
			var component = new OxygenComponent((Entity) holder);

			component.readFromNbt(tag);

			return component;
		}

		@Override
		public CompoundTag write(OxygenComponent attachment, HolderLookup.Provider provider) {
			var tag = new CompoundTag();

			attachment.writeToNbt(tag);

			return tag;
		}
	}).build());
	
	public static void init() {
		REGISTRY.register(AMCommon.modEventBus());
	}
}
