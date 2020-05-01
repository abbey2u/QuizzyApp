package com.kaybiel4u;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import java.util.*;
import android.support.v7.app.*;
import android.content.*;


public class MainActivity extends Activity
{
	//instance variables
	private Button mTrueButton;
	private Button mFalseButton;
	private Button nextButton;
	private Button prevButton;
	private Button mCheatButton;
	private TextView questionTextview;
	private static final String KEY_INDEX = "index";
	private static final String KEY_ANSWERED="answer";
	private ArrayList<Integer> answeredQuestion = new ArrayList<Integer>();
	private static final int REQUEST_CODE_CHEAT = 0;

	private boolean[] mCheatedArray;
	private static final String CHEATED_ARRAY="cheated_array ";
	

	private Question[] mQuestionBank = new Question[]{
		new Question(R.string.question_australia, true),
		new Question(R.string.question_oceans, true),
		new Question(R.string.question_mideast, false),
		new Question(R.string.question_africa, false),
		new Question(R.string.question_americas, true),
		new Question(R.string.question_asia, true)
	};

	private int mCurrentIndex = 0;
	private int numberOfCorrectAnswer = 0;
	private int numberOfIncorrectAnswer = 0;
	private boolean mIsCheater;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		mCheatedArray = new boolean[mQuestionBank.length];
		for(int  i=0; i < mQuestionBank.length; i++){
			mCheatedArray[i]=false;
		}

		if (savedInstanceState != null)
		{
			mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
			answeredQuestion = savedInstanceState.getIntegerArrayList(KEY_ANSWERED);
			mCheatedArray = savedInstanceState.getBooleanArray(CHEATED_ARRAY);
			mIsCheater = mCheatedArray[mCurrentIndex];
		}

		//textview
		questionTextview = findViewById(R.id.question_text_view);
		questionTextview.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

					updateQuestion();
				}
			});
			
		//true button
		mTrueButton = findViewById(R.id.true_button);
		mTrueButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mTrueButton.setClickable(false);
					mFalseButton.setClickable(false);
					checkAnswer(true);
					answeredQuestion.add(mCurrentIndex);
				}
			});
		//false button
		mFalseButton = findViewById(R.id.false_button);
		mFalseButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mTrueButton.setClickable(false);
					mFalseButton.setClickable(false);
					checkAnswer(false);

					answeredQuestion.add(mCurrentIndex);

				}
			});
		//next button	
		nextButton = findViewById(R.id.nextButtonId);
		nextButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

					mIsCheater = mCheatedArray[mCurrentIndex];

					updateQuestion();
				}
			});

		//previous button
		prevButton = findViewById(R.id.previousButtonId);
		prevButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (mCurrentIndex > 0)
					{
						mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
					}
					else
					{
						mCurrentIndex = mQuestionBank.length - 1;
					}
					updateQuestion();
				}
			});

		//cheat button
		mCheatButton = findViewById(R.id.cheat_button);
		mCheatButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					boolean answerIsTrue = mQuestionBank[mCurrentIndex].isMAnswerTrue();
					Intent intent = CheatActivity. newIntent(MainActivity.this, answerIsTrue);
					startActivityForResult(intent, REQUEST_CODE_CHEAT);
				}
			});

		updateQuestion();
    }
	//update questions
	private void updateQuestion()
	{

		int question = mQuestionBank[mCurrentIndex].getMTextResId();
		questionTextview.setText(question);

		if (answeredQuestion.contains(mCurrentIndex))
		{
			mTrueButton.setClickable(false);
			mFalseButton.setClickable(false);
		}
		else
		{
			mTrueButton.setClickable(true);
			mFalseButton.setClickable(true);
		}

	}
	
	// checking if the answer is correct or not
	private void checkAnswer(boolean userPressedTrue)
	{

		boolean answerIsTrue = mQuestionBank[mCurrentIndex].isMAnswerTrue();

		int messageResId = 0;

		if (mIsCheater)
		{
			messageResId = R.string.judgment_toast;
		}
		else
		{
			if (userPressedTrue == answerIsTrue)
			{
				numberOfCorrectAnswer += 1;
				messageResId = R.string.correct_toast;

			}
			else
			{
				numberOfIncorrectAnswer += 1;
				messageResId = R.string.incorrect_toast;
			}
		}

		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

		//calculate the percentage of the correct answer
		if ((numberOfCorrectAnswer + numberOfIncorrectAnswer) == mQuestionBank.length)
		{
			double percentageOfCorrectAnswers = ((double)(numberOfCorrectAnswer) / (double)mQuestionBank.length) * 100;
			Toast.makeText(MainActivity.this, getString(R.string.number_of_correct_answer) + Integer.toString(numberOfCorrectAnswer) + "\n" 
						   + getString(R.string.percentage_of_correct_answer, percentageOfCorrectAnswers), Toast.LENGTH_LONG).show();
		}
	}
	//saving the instance of the activity so that when the activity is destroyed and recreated it continues
	@Override
	protected void onSaveInstanceState(Bundle saveInstateState)
	{
		super.onSaveInstanceState(saveInstateState);

		saveInstateState.putInt(KEY_INDEX, mCurrentIndex);
		saveInstateState.putIntegerArrayList(KEY_ANSWERED, answeredQuestion);
		saveInstateState.putBooleanArray(CHEATED_ARRAY, mCheatedArray);
	}
	
	//implementing the onActivityResult method to accept return result of cheat activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != Activity.RESULT_OK)
		{
			return;
		}
		if (requestCode == REQUEST_CODE_CHEAT)
		{
			if (data == null)
			{
				return;
			}

			mIsCheater = CheatActivity.wasAnswerShown(data);
			
			mCheatedArray[mCurrentIndex] = mIsCheater;
		}
	}

}
