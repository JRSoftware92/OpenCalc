package com.dev.opencalc.expressionparsing;

import java.util.HashMap;

import com.dev.opencalc.exception.CalculationException;
import com.dev.opencalc.exception.FunctionArgException;
import com.dev.opencalc.utils.MathUtils;

/**
 * 
 * A token representing a function built into the system.
 * @author John Riley
 * 
 */
public class PresetFunction extends FunctionToken {
	
	private enum Preset {
		NULL, SQRT("\u221A"), CBRT("CBRT"), FACTORIAL("!"), 
		SIN, COS, TAN, COT, SEC, CSC, ASIN, ACOS, ATAN, ACOT, ASEC, ACSC,
		CUBE_VOLUME("V{SQR}"), SPHERE_VOLUME("V{O}"), CIRCUMFERENCE("C{O}"), 
		CIRCLE_AREA("A{O}"), SQUARE_AREA("A{SQR}"), SQUARE_PERIMETER("P{SQR}"),
		LN, LOG2, LOG10, ABS, LOG, ROOT, NCR, NPR, MIN, MAX, 
		TRI_AREA("A{TRI}"), TRI_PERIMETER("P{TRI}"), CONE_VOLUME;
		
		private String mToken;
		
		private Preset(){
			mToken = name();
		}
		
		private Preset(String funcToken){
			mToken = funcToken;
		}
		
		public String getToken(){ return mToken; }
	}
	
	private final Preset mPresetFunc;
	
	private static final HashMap<String, PresetFunction> mMap;
	
	static{
		mMap = new HashMap<String, PresetFunction>();
		
		Preset[] presets = Preset.values();
		for(int i = 1; i < presets.length ; i++){
			mMap.put(presets[i].getToken(), 
				new PresetFunction(presets[i].getToken(), presets[i], 
						i > Preset.ABS.ordinal() ? 2 : 1));
		}
	}
	
	private PresetFunction(){ 
		this("NULL", Preset.NULL, 0);
	}

	private PresetFunction(String token, Preset preset, int args) {
		super(token, args);
		mPresetFunc = preset;
	}
	
	public Preset getPresetType(){
		return mPresetFunc;
	}
	
	@Override
	public double calculate(double[] params) throws CalculationException {
		if(mPresetFunc != Preset.NULL){
			return calculate(this, params);
		}
		else if(params.length != numArgs){
			throw new FunctionArgException(this);
		}
		else{
			throw new CalculationException("Invalid function: " + mStrToken);
		}
	}
	
	public static boolean isValidToken(String token){
		return mMap.containsKey(token);
	}
	
	public static PresetFunction getToken(String token){
		return mMap.get(token);
	}
	
	public static HashMap<String, PresetFunction> getTokenMap(){
		return mMap;
	}
	
	public static double calculate(PresetFunction func, double[] params) throws CalculationException{
		double result = 0;
		double[] funcParams = new double[func.numArgs];
		
		if(params != null && params.length == func.numArgs){
			for(int i = 0; i < func.numArgs; i++){
				funcParams[i] = params[i];
			}
		}
		
		switch(func.mPresetFunc){
		case SIN: result = MathUtils.sin(funcParams[0]); break;
		case COS: result = MathUtils.cos(funcParams[0]); break;
		case TAN: result = MathUtils.tan(funcParams[0]); break;
		case COT: result = MathUtils.cot(funcParams[0]); break;
		case SEC: result = MathUtils.sec(funcParams[0]); break;
		case CSC: result = MathUtils.csc(funcParams[0]); break;
		case ASIN: result = MathUtils.asin(funcParams[0]); break;
		case ACOS: result = MathUtils.acos(funcParams[0]); break;
		case ATAN: result = MathUtils.atan(funcParams[0]); break;
		case ACOT: result = MathUtils.acot(funcParams[0]); break;
		case ASEC: result = MathUtils.asec(funcParams[0]); break;
		case ACSC: result = MathUtils.acsc(funcParams[0]); break;
		case LN: result = MathUtils.ln(funcParams[0]); break;
		case LOG: result = MathUtils.log(funcParams[1], funcParams[0]); break;
		case LOG2: result = MathUtils.log2(funcParams[0]); break;
		case LOG10: result = MathUtils.log10(funcParams[0]); break;
		case ABS: result = MathUtils.abs(funcParams[0]); break;
		case SQRT: result = MathUtils.sqrt(funcParams[0]); break;
		case CBRT: result = MathUtils.cbrt(funcParams[0]); break;
		case ROOT: result = MathUtils.root(funcParams[0], funcParams[1]); break;
		case FACTORIAL: result = MathUtils.factorial(funcParams[0]); break;
		case MIN: result = MathUtils.min(funcParams[0], funcParams[1]); break;
		case MAX: result = MathUtils.max(funcParams[0], funcParams[1]); break;
		case NCR: result = MathUtils.NCR(funcParams[0], funcParams[1]); break;
		case NPR: result = MathUtils.NPR(funcParams[0], funcParams[1]); break;
		case TRI_AREA: result = MathUtils.triArea(funcParams[0], funcParams[1]); break;
		case SQUARE_AREA: result = MathUtils.squareArea(funcParams[0]); break;
		case CIRCLE_AREA: result = MathUtils.circularArea(funcParams[0]); break;
		case SQUARE_PERIMETER: result = MathUtils.squarePerimeter(funcParams[0]); break;
		case CIRCUMFERENCE: result = MathUtils.circularCircumference(funcParams[0]); break;
		case CUBE_VOLUME: result = MathUtils.boxVolume(funcParams[0]); break;
		case CONE_VOLUME: result = MathUtils.conicVolume(funcParams[0], funcParams[1]); break;
		case SPHERE_VOLUME: result = MathUtils.sphericalVolume(funcParams[0]); break;
		default: 
			throw new CalculationException("Invalid function: " + func.toString());		
		}
		
		return result;
	}
	
	public static PresetFunction getFunction(String s){
		if(mMap.containsKey(s)){
			return mMap.get(s);
		}
		else{
			return new PresetFunction();
		}
	}
}
