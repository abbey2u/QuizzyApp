package com.kaybiel4u;


public class Question{
	
	private int mTextResId;
	private boolean mAnswerTrue;

	public Question(int mTextResId, boolean mAnswerTrue)
	{
		this.mTextResId = mTextResId;
		this.mAnswerTrue = mAnswerTrue;
	}

	public void setMTextResId(int mTextResId)
	{
		this.mTextResId = mTextResId;
	}

	public int getMTextResId()
	{
		return mTextResId;
	}

	public void setMAnswerTrue(boolean mAnswerTrue)
	{
		this.mAnswerTrue = mAnswerTrue;
	}

	public boolean isMAnswerTrue()
	{
		return mAnswerTrue;
	}
	
}

