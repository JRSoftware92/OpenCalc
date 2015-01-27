package com.dev.opencalc.utils;
/**
 * 
 * Math Utils class for calculating expressions.
 * @author John Riley
 * 
 */
public final class MathUtils {
	
	public static final char PI_CHAR = '\u03C0';
	public static final double PI = Math.PI;
	public static final double E = Math.E;
	
	//------------------------Algebraic Expressions--------------------------------//
	
	public static double pow(double num, double pow){
		return Math.pow(num, pow);
	}
	
	public static double root(double num, double root){
		return Math.pow(num, 1 / root);
	}
	
	public static double sqrt(double num){
		return Math.sqrt(num);
	}
	
	public static double cbrt(double num){
		return Math.cbrt(num);
	}
	
	public static double abs(double num){
		return Math.abs(num);
	}
	
	public static double log(double base, double num) {
		return (double)(Math.log(num) / Math.log(base));
	}
	
	public static double log2(double x) {
		return log(2, x);
	}
	
	public static double log10(double num){
		return Math.log10(num);
	}
	
	public static double ln(double num){
		return Math.log(num);
	}
	
	public static double min(double d1, double d2){
		return Math.min(d1, d2);
	}
	
	public static double max(double d1, double d2){
		return Math.max(d1, d2);
	}
	
	/**
	 * Computes iteratively to avoid recursive depth problems.
	 * @param n
	 * @return
	 */
	public static double factorial(double n){
		double result = 1;
		while(n > 0){
			result *= n--;
		}
		return result;
	}
	
	public static int AND(int a, int b){
		return a & b;
	}
	
	public static int OR(int a, int b){
		return a | b;
	}
	
	public static int XOR(int a, int b){
		return a ^ b;
	}
	
	/**
	 * Computes using fast algorithm if n <= r
	 * @param n
	 * @param r
	 * @return
	 */
	public static double NCR(double n, double r){
		if(r > n){
			return factorialNCR(n, r);
		}
		else{
			return fastNCR(n, r);
		}
	}
	
	/**
	 * Computes using fast algorithm if n <= r
	 * @param n
	 * @param r
	 * @return
	 */
	public static double NPR(double n, double r){
		if(r > n){
			return factorialNPR(n, r);
		}
		else{
			return fastNPR(n, r);
		}
	}
	
	private static double factorialNCR(double n, double r){
		double nf = factorial(n);
		double rf = factorial(r);
		double nrf = factorial(n - r);
		
		return nf / (rf * nrf);
	}
	
	private static double factorialNPR(double n, double r){
		double nf = factorial(n);
		double nrf = factorial(n - r);
		
		return nf / nrf;
	}
	
	private static double fastNCR(double n, double r){
		double val = 1;
		if(r == 0){
			return 0;
		}
		for (int k = 0; k < r; k++) {
			val = ( val * (n - k)) / (k + 1);
	    }
		return val;
	}
	
	private static double fastNPR(double n, double r){
		double val = 1;
		
		if(r == 0){
			return 0;
		}
		
		for(int k = 0; k < r; k++){
			val *= (n - k);
		}
		
		return val;
	}
	
	//-----------------------Trigonometric Expressions----------------------------//
	
	public static double sin(double degrees){
		return Math.sin(Math.toRadians(degrees));
	}
	
	public static double cos(double degrees){
		return Math.cos(Math.toRadians(degrees));
	}
	
	public static double tan(double degrees){
		return Math.tan(Math.toRadians(degrees));
	}

	public static double cot(double degrees){
		return 1.0 / Math.tan(Math.toRadians(degrees));
	}
	public static double sec(double degrees){
		return 1.0 / Math.cos(Math.toRadians(degrees));
	}
	public static double csc(double degrees){
		return 1.0 / Math.sin(Math.toRadians(degrees));
	}
	
	public static double asin(double degrees){
		return Math.asin(Math.toRadians(degrees));
	}
	
	public static double acos(double degrees){
		return Math.acos(Math.toRadians(degrees));
	}
	
	public static double atan(double degrees){
		return Math.atan(Math.toRadians(degrees));
	}
	
	public static double acot(double degrees){
		return 1.0 / Math.atan(Math.toRadians(degrees));
	}
	
	public static double asec(double degrees){
		return 1.0 / Math.acos(Math.toRadians(degrees));
	}
	
	public static double acsc(double degrees){
		return 1.0 / Math.asin(Math.toRadians(degrees));
	}
	
	//-----------------------Geometry methods----------------------------------------//
	
	public static double circularArea(double radius){
		return PI * pow(radius, 2);
	}
	
	public static double circularCircumference(double radius){
		return PI * 2 * radius;
	}
	
	public static double sphericalVolume(double radius){
		return ((PI * 4) / 3) * pow(radius, 3);
	}
	
	public static double conicVolume(double radius, double height){
		return ((PI * height)/ 3) * pow(radius, 2);
	}
	
	public static double squareArea(double length){
		return pow(length, 2);
	}
	
	public static double squarePerimeter(double length){
		return length * 4;
	}
	
	public static double boxVolume(double length){
		return pow(length, 3);
	}
	
	public static double triArea(double base, double height){
		return .5f * base * height;
	}
}
