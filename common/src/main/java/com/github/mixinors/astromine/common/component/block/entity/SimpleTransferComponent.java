package com.github.mixinors.astromine.common.component.block.entity;

public class SimpleTransferComponent implements TransferComponent {
	private TransferEntry itemComponentTransferEntry = new TransferEntry();
	private TransferEntry fluidComponentTransferEntry = new TransferEntry();
	private TransferEntry energyComponentTransferEntry = new TransferEntry();
	
	@Override
	public TransferEntry getItemEntry() {
		return itemComponentTransferEntry;
	}
	
	@Override
	public TransferEntry getFluidEntry() {
		return fluidComponentTransferEntry;
	}
	
	@Override
	public TransferEntry getEnergyEntry() {
		return energyComponentTransferEntry;
	}
	
	@Override
	public void addItem() {
		itemComponentTransferEntry = new TransferEntry();
	}
	
	@Override
	public void addFluid() {
		fluidComponentTransferEntry = new TransferEntry();
	}
	
	@Override
	public void addEnergy() {
		energyComponentTransferEntry = new TransferEntry();
	}
	
	@Override
	public boolean hasItem() {
		return itemComponentTransferEntry != null;
	}
	
	@Override
	public boolean hasFluid() {
		return fluidComponentTransferEntry != null;
	}
	
	@Override
	public boolean hasEnergy() {
		return energyComponentTransferEntry != null;
	}
}
