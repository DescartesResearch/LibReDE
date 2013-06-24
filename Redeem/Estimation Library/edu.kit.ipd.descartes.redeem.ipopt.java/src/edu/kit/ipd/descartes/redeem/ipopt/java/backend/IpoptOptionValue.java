package edu.kit.ipd.descartes.redeem.ipopt.java.backend;

import java.lang.ref.WeakReference;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public enum IpoptOptionValue {

	YES("yes"),
	NO("no"),
	ADAPTIVE("adaptive"),
	SECOND_ORDER("second-order");

	private String keyword;
	/*
	 * Ensure that this reference does not block the GC from freeing the native
	 * memory.
	 */
	private WeakReference<Pointer> nativeString;

	private IpoptOptionValue(String keyword) {
		this.keyword = keyword;
	}

	public Pointer toNativeString() {
		if (nativeString == null || nativeString.get() == null) {
			Pointer p = new Memory(keyword.length() + 1);
			p.setString(0, keyword);
			nativeString = new WeakReference<Pointer>(p);
		}
		return nativeString.get();
	}
}
