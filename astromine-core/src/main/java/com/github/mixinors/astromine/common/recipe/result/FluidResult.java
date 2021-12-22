package com.github.mixinors.astromine.common.recipe.result;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluidResult {
	public final FluidVariant variant;
	public final long amount;
	
	public FluidResult(FluidVariant variant, long amount) {
		this.variant = variant;
		this.amount = amount;
	}
	
	public boolean equalsAndFitsIn(SingleSlotStorage<FluidVariant> storage) {
		return storage.getCapacity() - storage.getAmount() >= amount && storage.getResource().equals(variant);
	}
	
	public static JsonObject toJson(FluidResult result) {
		var jsonObject = new JsonObject();
		
		jsonObject.addProperty("fluid", Registry.FLUID.getId(result.variant.getFluid()).toString());
		jsonObject.addProperty("amount", result.amount);
		
		return jsonObject;
	}
	
	public static FluidResult fromJson(JsonElement jsonElement) {
		if (!jsonElement.isJsonObject()) {
			var variantId = new Identifier(jsonElement.getAsString());
			var variantFluid = Registry.FLUID.get(variantId);
			
			var variant = FluidVariant.of(variantFluid);
			
			return new FluidResult(variant, 81000);
		} else {
			var jsonObject = jsonElement.getAsJsonObject();
			
			var variantId = new Identifier(jsonObject.get("fluid").getAsString());
			var variantFluid = Registry.FLUID.get(variantId);
			
			var variant = FluidVariant.of(variantFluid);
			
			var variantAmount = jsonObject.get("amount").getAsLong();
			
			return new FluidResult(variant, variantAmount);
		}
	}
	
	public static void toPacket(PacketByteBuf buf, FluidResult result) {
		buf.writeString(Registry.FLUID.getId(result.variant.getFluid()).toString());
		buf.writeLong(result.amount);
	}
	
	public static FluidResult fromPacket(PacketByteBuf buf) {
		var variantId = new Identifier(buf.readString());
		var variantFluid = Registry.FLUID.get(variantId);
		
		var variant = FluidVariant.of(variantFluid);
		
		var variantAmount = buf.readLong();
		
		return new FluidResult(variant, variantAmount);
	}
}
