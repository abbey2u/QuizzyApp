package com.kaybiel4u;

import android.os.Bundle;
import android.app.*;
import android.support.v7.app.AppCompatActivity;
import android.content.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.animation.*;
import android.os.*;



public class CheatActivity extends Activity
{

	//instance variables
	private TextView mAnswerTextView;
	private Button mShowAnswerButton;

	private TextView versionText;
	private static final String EXTRA_ANSWER_IS_TRUE = " com.kaybiel4u.answer_is_true";
	private boolean mAnswerIsTrue;
	private static final String EXTRA_ANSWER_SHOWN ="com.kaybiel4u.answer.shown";
	
	private static final String HAS_CHEATED="has_cheated";
	private boolean hasCheated;
	
	//passing extra info from cheat activity to quiz activity
	public static Intent newIntent(Context packageContext, boolean answerIsTrue)
	{
		Intent intent = new Intent(packageContext, CheatActivity.class);
		intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);

		return intent;
	}
	
	//decoding the result int
	public static boolean wasAnswerShown(Intent result)
	{
		return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

		if(savedInstanceState != null){
			hasCheated=savedInstanceState.getBoolean(HAS_CHEATED, false);
			setAnswerShownResult(hasCheated);
		}
		mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

		mAnswerTextView = findViewById(R.id.answer_text_view);

		versionText = findViewById(R.id.versionText);
		
		mShowAnswerButton = findViewById(R.id.show_answer_button);
		mShowAnswerButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (mAnswerIsTrue)
					{
						mAnswerTextView.setText(R.string.true_button);
					}
					else
					{
						mAnswerTextView.setText(R.string.false_button);
					}
					
					setAnswerShownResult(true);
					//setAnswerShownResult(hasCheated);
				if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
				
				int cx=mShowAnswerButton.getWidth()/2;
				int cy= mShowAnswerButton.getHeight()/2;
				float radius = mShowAnswerButton.getWidth();
				Animator anim =ViewAnimationUtils.createCircularReveal(mShowAnswerButton,cx,cy, radius,0);
				anim.addListener(new AnimatorListenerAdapter(){
					@Override
					public void onAnimationEnd(Animator animator){
						super.onAnimationEnd(animator);
						mShowAnswerButton.setVisibility(View.INVISIBLE);
						
					}
					
				});
				anim.start();
				}else{
					mShowAnswerButton.setVisibility(View.INVISIBLE);
				}
				
				}
			});
			
			versionText.setText(getString(R.string.api_level) +" "+ Build.VERSION.SDK_INT);

    }

	//setting a result
	private void setAnswerShownResult(boolean isAnswerShown)
	{
		Intent intent = new Intent();
		intent.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
		setResult(RESULT_OK, intent);
	}

	//save the instance state of hascheatedto retain it when the device is rotated
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		
		super.onSaveInstanceState(outState);
		
		outState.putBoolean(HAS_CHEATED, hasCheated);
	}
	
	

}
