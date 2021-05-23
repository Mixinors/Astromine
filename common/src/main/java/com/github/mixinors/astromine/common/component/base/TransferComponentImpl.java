package com.github.mixinors.astromine.common.component.base;

import java.util.Objects;

public class TransferComponentImpl implements TransferComponent {
	private TransferEntry itemComponentTransferEntry = TransferEntry.NONE;
	private TransferEntry fluidComponentTransferEntry = TransferEntry.NONE;
	private TransferEntry energyComponentTransferEntry = TransferEntry.NONE;
	
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
		return itemComponentTransferEntry != TransferEntry.NONE;
	}
	
	@Override
	public boolean hasFluid() {
		return fluidComponentTransferEntry != TransferEntry.NONE;
	}
	
	@Override
	public boolean hasEnergy() {
		return energyComponentTransferEntry != TransferEntry.NONE;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TransferComponentImpl that = (TransferComponentImpl) o;
		return Objects.equals(itemComponentTransferEntry, that.itemComponentTransferEntry) && Objects.equals(fluidComponentTransferEntry, that.fluidComponentTransferEntry) && Objects.equals(energyComponentTransferEntry, that.energyComponentTransferEntry);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(itemComponentTransferEntry, fluidComponentTransferEntry, energyComponentTransferEntry);
	}
}
