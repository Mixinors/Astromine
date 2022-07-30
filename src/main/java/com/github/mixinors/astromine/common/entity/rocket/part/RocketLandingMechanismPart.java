package com.github.mixinors.astromine.common.entity.rocket.part;

import com.github.mixinors.astromine.common.entity.rocket.part.base.RocketPart;
import com.github.mixinors.astromine.common.item.rocket.RocketLandingMechanismItem;

public class RocketLandingMechanismPart extends RocketPart<RocketLandingMechanismItem> {
	public enum Type {
		STANDING,
		PERCHING,
		HOVERING
	}
	
	private final Type type;
	
	public RocketLandingMechanismPart(RocketLandingMechanismItem item, Type type) {
		super(item);
		
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
}
