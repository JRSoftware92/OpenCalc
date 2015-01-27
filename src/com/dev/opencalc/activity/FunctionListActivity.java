package com.dev.opencalc.activity;

import java.util.Locale;

import com.dev.opencalc.R;
import com.dev.opencalc.exception.CalculationException;
import com.dev.opencalc.expressionparsing.CustomFunction;
import com.dev.opencalc.expressionparsing.VariableTokenizer;
import com.dev.opencalc.fragment.FunctionListFragment;
import com.dev.opencalc.fragment.SupportFragment;
import com.dev.opencalc.model.CalculatorDatabase;
import com.dev.opencalc.model.FunctionMeta;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * Activity for viewing and interacting with Function List Fragment
 * @author John Riley
 * 
 */
public class FunctionListActivity extends SupportFragmentActivity implements FunctionListFragment.OnListItemClickListener {
		
	private String mCurrentEntry = "";
	
	private Button mAddFunctionButton = null;
	private SupportFragment mFragment;
	private CalculatorDatabase mDb = null;
	
	private VariableTokenizer mTokenizer;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected int getLayoutId() { return R.layout.activity_function_list; }

	@Override
	protected int getMenuId() { return R.menu.function_list; }

	@Override
	protected void initializeDefaultActivity() {
		mDb = new CalculatorDatabase(this);
		mTokenizer = new VariableTokenizer();
		FunctionMeta[] functions = new FunctionMeta[0];
		
		if(mDb != null){
			functions = mDb.getFunctions();
		}
		mFragment = FunctionListFragment.newInstance(functions);
		addFragment(R.id.graphFragmentContainer, mFragment);
		
		mAddFunctionButton = (Button) findViewById(R.id.addFunctionButton);
		mAddFunctionButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				onAddButtonClick();
			}
		});
	}

	@Override
	protected void initializeActivity(Bundle args) {
		initializeDefaultActivity();
		if(args.containsKey(CalculatorActivity.EXPR_KEY)){
			mCurrentEntry = args.getString(CalculatorActivity.EXPR_KEY);
		}
	}
	
	@Override
	public void onListItemClick(int pos, long id) {
		if(mFragment instanceof FunctionListFragment){
			FunctionMeta function = ((FunctionListFragment)mFragment).getItem(pos);
			CustomFunction.registerFunction(new CustomFunction(function.getTokenString(), 
					function.getParameterArray(), function.getExpression()));
		
			Intent i = new Intent(this, CalculatorActivity.class);
			i.putExtra(CalculatorActivity.EXPR_KEY, mCurrentEntry + function.getTokenString() + "(");
			startActivity(i);
		}
	}
	
	/**
	 * Called on add function click. Displays add function dialog.
	 */
	protected void onAddButtonClick(){
		Dialog popup = getAddFunctionDialog(this);
		popup.show();
	}
	
	/**
	 * Called on edit function context menu click: Edits the requested function at the provided
	 * index.
	 * @param pos - Index of the requested function.
	 */
	public void onEditFunction(int pos){
		FunctionMeta[] funcs = mDb.getFunctions();
		if(pos < funcs.length){
			getEditFunctionDialog(funcs[pos], this).show();
		}
	}
	
	/**
	 * TODO CURRENTLY NOT WORKING.
	 * Called on delete function context menu click: Delete the function at the provided index
	 * from the database.
	 * @param pos - Index of the requested function.
	 */
	public void onDeleteFunction(int pos){
		FunctionMeta[] funcs = mDb.getFunctions();
		if(pos < funcs.length){
			Log.d("Function List Activity", "Deleting function at pos " + pos);
			mDb.deleteFunctionRow(funcs[pos].getId());
			((FunctionListFragment)mFragment).setList(mDb.getFunctions());
		}
	}
	
	/**
	 * Attempts to add a new function with the provided input strings, and returns false if it fails.
	 * @param name - Name of the function.
	 * @param token - Token to be shown on the calculator display.
	 * @param expr - Expression to be referenced.
	 * @return True on successfull function addition, false otherwise.
	 */
	public boolean addFunctionIfPossible(String name, String token, String expr){
		boolean flag = false;
		if(!name.equals("") && !token.equals("") && !expr.equals("")){
			try {
				mTokenizer.tokenize(expr);
				FunctionMeta newFunc = new FunctionMeta(
						name, token, expr, mTokenizer.getParameters()
			    );
				mDb.insertFunction(newFunc);
				
				if(!(mFragment instanceof FunctionListFragment)){
					mFragment = FunctionListFragment.newInstance(mDb.getFunctions());
				}
				else{
					((FunctionListFragment)mFragment).setList(mDb.getFunctions());
				}
				replaceFragment(R.id.graphFragmentContainer, mFragment);
				
				flag = true;
			} 
			catch (CalculationException e) {
				Toast.makeText(this, 
					"Please enter a valid expression, with variables if needed.", 
					Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(this, 
				"Please fill in all fields for function creation.", 
				Toast.LENGTH_SHORT).show();
		}
		return flag;
	}
	
	/**
	 * Returns a popup dialog for adding a new function to the database.
	 * @param context - Context of the application.
	 * @return Initialized add function dialog.
	 */
	public Dialog getAddFunctionDialog(Context context){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_function);
        dialog.setTitle("Add Function");

        final EditText mNameText, mTokenText, mExprText;
        
        mNameText = (EditText) dialog.findViewById(R.id.dialogFunctionNameEdit);
        mTokenText = (EditText) dialog.findViewById(R.id.dialogFunctionTokenEdit);
        mExprText = (EditText) dialog.findViewById(R.id.dialogFunctionExpressionEdit);

        Button addButton = (Button) dialog.findViewById(R.id.dialogFunctionAddButton);
        addButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				String name = "", token = "", expr = "";
				
				name = mNameText.getText().toString();
				token = mTokenText.getText().toString().toUpperCase(Locale.US);
				expr = mExprText.getText().toString().toUpperCase(Locale.US);
				
				if(addFunctionIfPossible(name, token, expr)){
					dialog.dismiss();
				}
			}
        });
         
        Button cancelButton = (Button) dialog.findViewById(R.id.dialogFunctionCancelButton);
        cancelButton.setOnClickListener(getCancelListener(dialog));
        
        return dialog;
	}
	
	/**
	 * Returns a popup dialog for editing an existing function.
	 * @param func - Function to be edited.
	 * @param context - Context of the application.
	 * @return Initialized add function dialog.
	 */
	public Dialog getEditFunctionDialog(FunctionMeta func, Context context){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_function);
        dialog.setTitle("Edit Function");

        final EditText mNameText, mTokenText, mExprText;
        
        mNameText = (EditText) dialog.findViewById(R.id.dialogFunctionNameEdit);
        mTokenText = (EditText) dialog.findViewById(R.id.dialogFunctionTokenEdit);
        mExprText = (EditText) dialog.findViewById(R.id.dialogFunctionExpressionEdit);
        
        mNameText.setText(func.getName());
        mTokenText.setText(func.getTokenString());
        mExprText.setText(func.getExpression());
        
        //submit button
        Button addButton = (Button) dialog.findViewById(R.id.dialogFunctionAddButton);
        addButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				String name = mNameText.getText().toString();
				String token = mTokenText.getText().toString().toUpperCase(Locale.US);
				String expr = mExprText.getText().toString().toUpperCase(Locale.US);
				
				if(addFunctionIfPossible(name, token, expr)){
					dialog.dismiss();
				}
			}
        });
         
        Button cancelButton = (Button) dialog.findViewById(R.id.dialogFunctionCancelButton);
        cancelButton.setOnClickListener(getCancelListener(dialog));
        
        return dialog;
	}
	
	/**
	 * Returns a standard onCancel listener for a dialog.
	 * @param dialog - Dialog object to initialize the listener to.
	 * @return An onCancelClick listener targeting the provided dialog.
	 */
	public static Button.OnClickListener getCancelListener(Dialog dialog){
		final Dialog d = dialog;
		return new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				d.dismiss();
			}
		};
	}
}
