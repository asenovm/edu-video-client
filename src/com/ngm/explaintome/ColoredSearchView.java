package com.ngm.explaintome;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.SearchView;

public class ColoredSearchView extends SearchView {

	public ColoredSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);

		final EditText textField = (EditText) findViewById(getResources()
				.getIdentifier("android:id/search_src_text", null, null));
		textField.setTextColor(Color.BLACK);
	}

	public ColoredSearchView(Context context) {
		this(context, null);
	}

}
