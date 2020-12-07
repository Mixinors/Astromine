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

package com.github.chainmailstudios.astromine.discoveries.common.entity.ai.superspaceslime;

import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.attribute.EntityAttributes;

import com.github.chainmailstudios.astromine.discoveries.common.entity.SuperSpaceSlimeEntity;

public class SuperSpaceSlimeMoveControl extends MoveControl {

	private final SuperSpaceSlimeEntity slime;
	private float targetYaw;
	private int ticksUntilJump;
	private boolean jumpOften;

	public SuperSpaceSlimeMoveControl(SuperSpaceSlimeEntity slime) {
		super(slime);
		this.slime = slime;
		this.targetYaw = 180.0F * slime.yaw / 3.1415927F;
	}

	@Override
	public void tick() {
		this.entity.yaw = this.changeAngle(this.entity.yaw, this.targetYaw, 90.0F);
		this.entity.headYaw = this.entity.yaw;
		this.entity.bodyYaw = this.entity.yaw;

		if (this.state != MoveControl.State.MOVE_TO) {
			this.entity.setForwardSpeed(0.0F);
		} else {
			this.state = MoveControl.State.WAIT;

			if (this.entity.isOnGround()) {
				this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
				if (this.ticksUntilJump-- <= 0) {
					this.ticksUntilJump = this.slime.getTicksUntilNextJump();

					if (this.jumpOften) {
						this.ticksUntilJump /= 3;
					}

					this.slime.getJumpControl().setActive();
					this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getJumpSoundPitch());
				} else {
					this.slime.sidewaysSpeed = 0.0F;
					this.slime.forwardSpeed = 0.0F;
					this.entity.setMovementSpeed(0.0F);
				}
			} else {
				this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
			}
		}
	}

	public void look(float targetYaw, boolean jumpOften) {
		this.targetYaw = targetYaw;
		this.jumpOften = jumpOften;
	}

	public void move(double speed) {
		this.speed = speed;
		this.state = MoveControl.State.MOVE_TO;
	}
}
