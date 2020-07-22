package com.github.chainmailstudios.astromine.common.item;

import com.github.chainmailstudios.astromine.common.block.TieredHorizontalFacingMachineBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.List;

public class AstromineBlockItem extends BlockItem {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.##");

	public AstromineBlockItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		if (getBlock() instanceof TieredHorizontalFacingMachineBlock) {
			tooltip.add(new TranslatableText("text.astromine.tooltip.speed", DECIMAL_FORMAT.format(((TieredHorizontalFacingMachineBlock) getBlock()).getMachineSpeed())).formatted(Formatting.GRAY));
			tooltip.add(new LiteralText(" "));
		}
		super.appendTooltip(stack, world, tooltip, context);
	}
}
