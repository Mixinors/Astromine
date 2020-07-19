package com.github.chainmailstudios.astromine.common.network;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public interface NetworkMember {
	Collection<NetworkMemberType> REQUESTER = Collections.singleton(NetworkMemberType.REQUESTER);
	Collection<NetworkMemberType> PROVIDER = Collections.singleton(NetworkMemberType.PROVIDER);
	Collection<NetworkMemberType> REQUESTER_PROVIDER = Sets.newHashSet(NetworkMemberType.REQUESTER, NetworkMemberType.PROVIDER);
	Collection<NetworkMemberType> BUFFER = Collections.singleton(NetworkMemberType.BUFFER);
	Collection<NetworkMemberType> NODE = Collections.singleton(NetworkMemberType.NODE);
	Collection<NetworkMemberType> NONE = Collections.emptySet();

	@NotNull
	Map<NetworkType, Collection<NetworkMemberType>> getMemberProperties();

	default Map<NetworkType, Collection<NetworkMemberType>> ofTypes(Object... objects) {
		Map<NetworkType, Collection<NetworkMemberType>> map = Maps.newHashMap();
		for (int i = 0; i < objects.length; i += 2) {
			NetworkType type = (NetworkType) objects[i];
			Collection<NetworkMemberType> memberTypes = (Collection<NetworkMemberType>) objects[i + 1];
			map.put(type, memberTypes);
		}
		return map;
	}

	default Collection<NetworkMemberType> getMemberNetworkTypeProperties(NetworkType type) {
		return getMemberProperties().getOrDefault(type, NONE);
	}

	default boolean acceptsType(NetworkType type) {
		return !getMemberNetworkTypeProperties(type).isEmpty();
	}

	default boolean isProvider(NetworkType type) {
		return getMemberNetworkTypeProperties(type).contains(NetworkMemberType.PROVIDER);
	}

	default boolean isRequester(NetworkType type) {
		return getMemberNetworkTypeProperties(type).contains(NetworkMemberType.REQUESTER);
	}

	default boolean isBuffer(NetworkType type) {
		return getMemberNetworkTypeProperties(type).contains(NetworkMemberType.BUFFER);
	}

	default boolean isNode(NetworkType type) {
		return true;
	}
}
