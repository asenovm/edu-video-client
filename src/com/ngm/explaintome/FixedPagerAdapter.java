package com.ngm.explaintome;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngm.explaintome.data.QuestionType;

public class FixedPagerAdapter extends PagerAdapter {

	private Context context;

	public FixedPagerAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		switch (position) {

		default:
			return LayoutInflater.from(context).inflate(R.layout.multi_answer_question, null);
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return QuestionType.MULTIPLE_CHOICE.name();
		case 1:
			return QuestionType.OPEN_ENDED.name();
		case 2:
			return QuestionType.COMMENT.name();
		}
		return null;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return false;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeViewAt(position);
	}

}
