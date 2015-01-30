package com.dev.opencalc.fragment;

import java.util.Locale;

import com.dev.opencalc.R;
import com.dev.opencalc.interfaces.CalculatorListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 
 * A Keypad fragment containing calculator buttons.
 * @author John Riley
 * 
 */
public class KeypadFragment extends Fragment {
	
	private static String LAYOUT_KEY = "com.calc.keypadfragment.layoutKey";
	
	private CalculatorListener mListener;
	
	public static KeypadFragment newInstance() {
		KeypadFragment fragment = new KeypadFragment();
		return fragment;
	}
	
	public static KeypadFragment newInstance(int id){
		KeypadFragment fragment = new KeypadFragment();
		Bundle args = new Bundle();
		if (id == R.id.keypadAlgebraButton) {
			args.putInt(LAYOUT_KEY, R.layout.fragment_keypad_algebra);
		} else if (id == R.id.keypadGeometryButton) {
			args.putInt(LAYOUT_KEY, R.layout.fragment_keypad_geometry);
		} else if (id == R.id.keypadTrigButton) {
			args.putInt(LAYOUT_KEY, R.layout.fragment_keypad_trig);
		} else {
			args.putInt(LAYOUT_KEY, R.layout.fragment_keypad_standard);
		}
		fragment.setArguments(args);
		return fragment;
	}
	
	public KeypadFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle args = getArguments();
		int layout = R.layout.fragment_keypad_standard;
		if(args != null){
			int temp = args.getInt(LAYOUT_KEY);
			if(isValidLayout(temp)){
				layout = temp;
			}
		}
		View rootView = inflater.inflate(layout, container, false);
		
		if(layout == R.layout.fragment_keypad_standard){
			initializeDefaultButtons(rootView);
		}
		else{
			initializeFunctionButtons(rootView, layout);
		}
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			mListener = (CalculatorListener) activity;
		} 
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement CalculatorListener");
		}
		
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	public void onKeypadEntry(String entry){
		if(mListener != null){
			mListener.onKeyPadEntry(entry);
		}
	}
	
	public void onKeypadBackspace(){
		if(mListener != null){
			mListener.onKeyPadBackspaceClick();
		}
	}
	
	public void onKeypadClear(){
		if(mListener != null){
			mListener.onKeyPadClearClick();
		}
	}
	
	public void onKeypadEnter(){
		if(mListener != null){
			mListener.onKeyPadEnterClick();
		}
	}
	
	public void setFragmentListener(CalculatorListener listener){
		mListener = listener;
	}
	
	private static boolean isValidLayout(int id){
		return id == R.layout.fragment_keypad_standard ||
			   id == R.layout.fragment_keypad_algebra ||
			   id == R.layout.fragment_keypad_trig ||
			   id == R.layout.fragment_keypad_geometry;
	}

	/**
	 * Initializes buttons composing keypad fragment.
	 * @param rootView
	 */
	private void initializeDefaultButtons(View rootView){
		Button[] simpleEntryButtons = new Button[]{
				(Button) rootView.findViewById(R.id.calculatorButtonOne),
				(Button) rootView.findViewById(R.id.calculatorButtonTwo),
				(Button) rootView.findViewById(R.id.calculatorButtonThree),
				(Button) rootView.findViewById(R.id.calculatorButtonFour),
				(Button) rootView.findViewById(R.id.calculatorButtonFive),
				(Button) rootView.findViewById(R.id.calculatorButtonSix),
				(Button) rootView.findViewById(R.id.calculatorButtonSeven),
				(Button) rootView.findViewById(R.id.calculatorButtonEight),
				(Button) rootView.findViewById(R.id.calculatorButtonNine),
				(Button) rootView.findViewById(R.id.calculatorButtonZero),
				(Button) rootView.findViewById(R.id.calculatorButtonLeftParen),
				(Button) rootView.findViewById(R.id.calculatorButtonRightParen),
				(Button) rootView.findViewById(R.id.calculatorEButton),
				(Button) rootView.findViewById(R.id.calculatorPlusButton),
				(Button) rootView.findViewById(R.id.calculatorMinusButton),
				(Button) rootView.findViewById(R.id.calculatorMultButton),
				(Button) rootView.findViewById(R.id.calculatorDivideButton),
				(Button) rootView.findViewById(R.id.calculatorPowButton),
				(Button) rootView.findViewById(R.id.calculatorPiButton),
				(Button) rootView.findViewById(R.id.calculatorDecimalButton),
				(Button) rootView.findViewById(R.id.calculatorCommaButton),
				(Button) rootView.findViewById(R.id.keypadMemoryButton),
				(Button) rootView.findViewById(R.id.keypadMemoryPlusButton),
				(Button) rootView.findViewById(R.id.keypadModuloButton),
				
				(Button) rootView.findViewById(R.id.keypadCbrtButton),
				(Button) rootView.findViewById(R.id.keypadSqrtButton),
				(Button) rootView.findViewById(R.id.keypadLnButton),
				(Button) rootView.findViewById(R.id.keypadLogButton),
				(Button) rootView.findViewById(R.id.keypadFactButton)
		};
		
		for(int i = 0; i < simpleEntryButtons.length - 5; i++){
			simpleEntryButtons[i].setOnClickListener(DEFAULT_LISTENER);
		}
		
		for(int i = simpleEntryButtons.length - 5; i < simpleEntryButtons.length; i++){
			simpleEntryButtons[i].setOnClickListener(FUNCTION_LISTENER);
		}
	}
	
	private void initializeTrigButtons(View rootView){
		Button[] trigEntryButtons = new Button[]{
				(Button) rootView.findViewById(R.id.keypadSinButton),
				(Button) rootView.findViewById(R.id.keypadCosButton),
				(Button) rootView.findViewById(R.id.keypadTanButton),
				(Button) rootView.findViewById(R.id.keypadCotButton),
				(Button) rootView.findViewById(R.id.keypadSecButton),
				(Button) rootView.findViewById(R.id.keypadCscButton),
				(Button) rootView.findViewById(R.id.keypadAsinButton),
				(Button) rootView.findViewById(R.id.keypadAcosButton),
				(Button) rootView.findViewById(R.id.keypadAtanButton),
				(Button) rootView.findViewById(R.id.keypadAsecButton),
				(Button) rootView.findViewById(R.id.keypadAcotButton),
				(Button) rootView.findViewById(R.id.keypadAcscButton)
		};
		
		for(int i = 0; i < trigEntryButtons.length; i++){
			trigEntryButtons[i].setOnClickListener(FUNCTION_LISTENER);
		}
	}
	
	private void initializeGeometryButtons(View rootView){
		//TODO Initialize geometry function buttons
		Button[] geomEntryButtons = new Button[]{
				(Button) rootView.findViewById(R.id.keypadAreaCircle),
				(Button) rootView.findViewById(R.id.keypadAreaSquare),
				(Button) rootView.findViewById(R.id.keypadAreaTriangle),
				(Button) rootView.findViewById(R.id.keypadPerimeterSquare),
				(Button) rootView.findViewById(R.id.keypadCircumferenceCircle),
				(Button) rootView.findViewById(R.id.keypadVolumeCircle),
				(Button) rootView.findViewById(R.id.keypadVolumeBox)
		};
		
		for(int i = 0; i < geomEntryButtons.length; i++){
			geomEntryButtons[i].setOnClickListener(FUNCTION_LISTENER);
		}
	}
	
	private void initializeAlgebraButtons(View rootView){
		Button[] algEntryButtons = new Button[]{
				(Button) rootView.findViewById(R.id.keypadMinButton),
				(Button) rootView.findViewById(R.id.keypadMaxButton),
				(Button) rootView.findViewById(R.id.keypadRootButton),
				(Button) rootView.findViewById(R.id.keypadLog2Button),
				(Button) rootView.findViewById(R.id.keypadLog10Button),
				(Button) rootView.findViewById(R.id.keypadAbsButton),
				(Button) rootView.findViewById(R.id.keypadNcrButton),
				(Button) rootView.findViewById(R.id.keypadNprButton)
		};
		
		for(int i = 0; i < algEntryButtons.length; i++){
			algEntryButtons[i].setOnClickListener(FUNCTION_LISTENER);
		}
	}
	
	private void initializeFunctionButtons(View rootView, int layout){
		if (layout == R.layout.fragment_keypad_algebra) {
			initializeAlgebraButtons(rootView);
		} else if (layout == R.layout.fragment_keypad_trig) {
			initializeTrigButtons(rootView);
		} else if (layout == R.layout.fragment_keypad_geometry) {
			initializeGeometryButtons(rootView);
		}
	}
	
	private final Button.OnClickListener DEFAULT_LISTENER = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			String temp = ((Button)v).getText().toString();
			mListener.onKeyPadEntry(temp);
		}
	};
	
	private final Button.OnClickListener FUNCTION_LISTENER = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			String temp = ((Button)v).getText().toString().toUpperCase(Locale.US) + "(";
			mListener.onKeyPadEntry(temp);
		}
	};
}
