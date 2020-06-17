package com.github.chainmailstudios.astromine.common.block.entity;

public class EnergyWireConnectorBlockEntity extends WireConnectorBlockEntity {
	@Override
	public float getCableOffset() {
		return 0.5f;
	}

	@Override
	public int getColor() {
		return 0x7e2fd3da;
	}
}
