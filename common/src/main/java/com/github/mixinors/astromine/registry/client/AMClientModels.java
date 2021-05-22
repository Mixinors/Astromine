/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.techreborn.common.util.ClientUtils;

import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AMClientModels {
	public static final ModelIdentifier CONVEYOR_SUPPORTS = new ModelIdentifier(AMCommon.id("conveyor_supports"), "");
	
	public static final ModelIdentifier ROCKET_INVENTORY = new ModelIdentifier(AMCommon.id("rocket"), "inventory");
	
	public static final Lazy<ModelTransformation> ITEM_HANDHELD_TRANSFORMATION = new Lazy<>(() -> {
		try {
			var resource = ClientUtils.getInstance().getResourceManager().getResource(new Identifier("minecraft:models/item/handheld.json"));
			var stream = resource.getInputStream();
			var model = JsonUnbakedModel.deserialize(new BufferedReader(new InputStreamReader(stream))).getTransformations();
			
			stream.close();
			
			return model;
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	});
	
	public static void init() {
		throw new UnsupportedOperationException("Cannot call this method method; must @Overwrite!");
	}
}
