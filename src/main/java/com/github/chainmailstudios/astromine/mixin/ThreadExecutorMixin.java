package com.github.chainmailstudios.astromine.mixin;

import net.minecraft.util.thread.ThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

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
