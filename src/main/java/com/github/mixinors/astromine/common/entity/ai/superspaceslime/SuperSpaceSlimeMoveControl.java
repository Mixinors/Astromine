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

package com.github.mixinors.astromine.common.entity.ai.superspaceslime;

import com.github.mixinors.astromine.common.entity.slime.SuperSpaceSlimeEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class SuperSpaceSlimeMoveControl extends MoveControl {
	private final SuperSpaceSlimeEntity slime;
	
	private float targetYaw;
	
	private int ticksUntilJump;
	
	private boolean jumpOften;
	
	public SuperSpaceSlimeMoveControl(SuperSpaceSlimeEntity slime) {
		super(slime);
		
		this.slime = slime;
		
		this.targetYaw = 180.0F * slime.getYRot() / (float) Math.PI;
	}
	
	@Override
	public void tick() {
		mob.setYRot(rotlerp(mob.getYRot(), targetYaw, 90.0F));
		
		mob.yHeadRot = mob.getYRot();
		mob.yBodyRot = mob.getYRot();
		
		if (operation != MoveControl.Operation.MOVE_TO) {
			mob.setZza(0.0F);
		} else {
			operation = MoveControl.Operation.WAIT;
			
			if (mob.onGround()) {
				mob.setSpeed((float) (speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
				
				if (ticksUntilJump-- <= 0) {
					ticksUntilJump = slime.getTicksUntilNextJump();
					
					if (jumpOften) {
						ticksUntilJump /= 3;
					}
					
					slime.getJumpControl().jump();
					
					slime.playSound(slime.getJumpSound(), slime.getSoundVolume(), slime.getJumpSoundPitch());
				} else {
					slime.xxa = 0.0F;
					slime.zza = 0.0F;
					
					mob.setSpeed(0.0F);
				}
			} else {
				mob.setSpeed((float) (speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
			}
		}
	}
	
	public void look(float targetYaw, boolean jumpOften) {
		this.targetYaw = targetYaw;
		this.jumpOften = jumpOften;
	}
	
	public void move(double speed) {
		this.speedModifier = speed;
		this.operation = MoveControl.Operation.MOVE_TO;
	}
}
