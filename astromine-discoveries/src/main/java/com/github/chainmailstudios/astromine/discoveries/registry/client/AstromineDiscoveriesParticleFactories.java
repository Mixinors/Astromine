/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.discoveries.registry.client;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

import net.minecraft.client.particle.CrackParticle;
import net.minecraft.item.ItemStack;

import com.github.chainmailstudios.astromine.discoveries.client.particle.MarsDustParticle;
import com.github.chainmailstudios.astromine.discoveries.client.particle.RocketFlameParticle;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesItems;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesParticles;
import com.github.chainmailstudios.astromine.registry.client.AstromineParticleFactories;

public class AstromineDiscoveriesParticleFactories extends AstromineParticleFactories {
	public static void initialize() {
		ParticleFactoryRegistry.getInstance().register(AstromineDiscoveriesParticles.SPACE_SLIME, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new CrackParticle(world, x, y, z, new ItemStack(AstromineDiscoveriesItems.SPACE_SLIME_BALL)));

		ParticleFactoryRegistry.getInstance().register(AstromineDiscoveriesParticles.MARS_DUST, MarsDustParticle.Factory::new);

		ParticleFactoryRegistry.getInstance().register(AstromineDiscoveriesParticles.ROCKET_FLAME, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			RocketFlameParticle particle = new RocketFlameParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSprite(provider);
			return particle;
		});
	}
}
