package com.special.AndroidSmartUpdates.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import com.special.AndroidSmartUpdates.R;

import static android.content.DialogInterface.*;

/**
 * User: special
 * Date: 13-9-16
 * Time: 下午2:30
 * Mail: specialcyci@gmail.com
 */
public class DialogHelper {

    private final Context context;
    private AlertDialog.Builder builder;

    public DialogHelper(Context context){
        this.context = context;
        buildUpdateDialogBuilder();
    }

    public void buildUpdateDialogBuilder(){
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_title)
               .setCancelable(true);
        builder.setNegativeButton(R.string.dialog_negative_button,negativeOnClick);
    }

    public void setMessage(String message){
        builder.setMessage(message);
    }

    public void setUpdateButtonClickEvent(OnClickListener event){
        builder.setPositiveButton(R.string.dialog_positive_button,event);
    }

    public void showDialog(){
        builder.create().show();
    }

    private OnClickListener negativeOnClick = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };
}
