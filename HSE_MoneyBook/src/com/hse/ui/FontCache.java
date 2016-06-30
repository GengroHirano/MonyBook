package com.hse.ui;

import android.graphics.Typeface;
import android.util.LruCache;

public class FontCache {
	private static LruCache<String, Typeface> fontCache ;

	public FontCache() {
		fontCache = null;
	}

	public static LruCache<String, Typeface> getInstance() {
		if (fontCache == null) {
			fontCache = new LruCache<String, Typeface>(12) ;
		}
		return fontCache;
	}
}
