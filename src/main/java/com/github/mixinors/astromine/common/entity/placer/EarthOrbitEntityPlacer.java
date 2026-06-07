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

package com.github.mixinors.astromine.common.entity.placer;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.entity.placer.base.EntityPlacer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

public record EarthOrbitEntityPlacer(
		int y
) implements EntityPlacer {
	public static final EarthOrbitEntityPlacer TO_EARTH = new EarthOrbitEntityPlacer(AMConfig.get().world.layers.earth.spawnY);
	public static final EarthOrbitEntityPlacer TO_EARTH_ORBIT = new EarthOrbitEntityPlacer(AMConfig.get().world.layers.earthOrbit.spawnY);
	
	@Override
	public DimensionTransition placeEntity(Entity entity) {
		return new DimensionTransition((ServerLevel) entity.level(), new Vec3(entity.getX(), y, entity.getZ()), entity.getDeltaMovement(), entity.getYHeadRot(), entity.getXRot(), DimensionTransition.DO_NOTHING);
	}
}
