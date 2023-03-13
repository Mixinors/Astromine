package com.google.common.collect;

import java.util.ArrayList;
import java.util.List;

public class RemutableList<E> extends ImmutableList<E> {
	private final List<E> wrapped = new ArrayList<>();
	
	@Override
	public int size() {
		return wrapped.size();
	}
	
	@Override
	public E get(int index) {
		return wrapped.get(index);
	}
	
	public List<E> getWrapped() {
		return wrapped;
	}
	
	@Override
	boolean isPartialView() {
		return false;
	}
}
