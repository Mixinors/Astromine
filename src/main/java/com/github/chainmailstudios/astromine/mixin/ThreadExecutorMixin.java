package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.util.thread.ThreadExecutor;

@Mixin(ThreadExecutor.class)
public class ThreadExecutorMixin {
	/**
	 * @author HalfOf2
	 * @reason Testing
	 */
	@Overwrite
	public void waitForTasks() {
	}
}
