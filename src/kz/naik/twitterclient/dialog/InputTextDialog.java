package kz.naik.twitterclient.dialog;

import kz.naik.twitterclient.R;
import kz.naik.twitterclient.common.IInputTextHandler;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;

public class InputTextDialog {
	
	private Context context;
	private IInputTextHandler handler;
	
	public InputTextDialog(Context context,IInputTextHandler handler){
		this.context = context;
		this.handler = handler;
	}
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	public void callDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getResources().getString(R.string.new_tweet));
		final EditText input = new EditText(context);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		input.setLines(5);
		input.setGravity(Gravity.TOP);
		input.setSingleLine(false);
		builder.setView(input);
		
		builder.setPositiveButton(context.getResources().getString(R.string.send),
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						handler.inputTextHandler(input.getText().toString());
					}
				});
		
		builder.setNegativeButton(context.getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//NOOP
					}
				});
		
		builder.create().show();
		
	}

}
