package com.dev.opencalc.expressionparsing;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Token for matching a string parameter with a value (used for functions.)
 * @author John Riley
 *
 */
public class ParameterToken extends ValueToken implements Parcelable{

	public ParameterToken(String token, double value) {
		super(token, value);
	}
	
	public ParameterToken(ParameterToken token, double newValue){
		this(token.mStrToken, newValue);
	}
	
	public ParameterToken(Parcel in){
		this(in.readString(), in.readDouble());
	}
	
	public String getStringParameter(){ return this.mStrToken; }

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mStrToken);
		dest.writeDouble(mValue);
	}
	
	public static final Parcelable.Creator<ParameterToken> CREATOR = new Parcelable.Creator<ParameterToken>() {
        public ParameterToken createFromParcel(Parcel in) {
            return new ParameterToken(in);
        }

        public ParameterToken[] newArray(int size) {
         //throw new UnsupportedOperationException();
            return new ParameterToken[size];
        }
    };
}
