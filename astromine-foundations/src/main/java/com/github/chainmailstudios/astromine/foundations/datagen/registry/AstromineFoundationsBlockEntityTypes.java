package com.github.chainmailstudios.astromine.foundations.datagen.registry;

import com.github.chainmailstudios.astromine.foundations.common.block.altar.entity.AltarBlockEntity;
import com.github.chainmailstudios.astromine.foundations.common.block.altar.entity.ItemDisplayerBlockEntity;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class AstromineFoundationsBlockEntityTypes {
	public static final BlockEntityType<ItemDisplayerBlockEntity> ITEM_DISPLAYER = register("item_displayer", ItemDisplayerBlockEntity::new, AstromineFoundationsBlocks.ITEM_DISPLAYER);
	public static final BlockEntityType<AltarBlockEntity> ALTAR = register("altar", AltarBlockEntity::new, AstromineFoundationsBlocks.ALTAR);

	public static void initialize() {

	}

	public static <B extends BlockEntity> BlockEntityType<B> register(String name, Supplier<B> supplier, Block... supportedBlocks) {
		return AstromineBlockEntityTypes.register(name, supplier, supportedBlocks);
	}
}
