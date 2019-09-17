package jigydi.com.jigydi.app.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import jigydi.com.jigydi.R;

/**
 * Created by click2clinic on 08-06-2017.
 */

public class CustomProgressBar extends AlertDialog {
    TextView message;
    Context context;
    String title;

    public CustomProgressBar(@NonNull Context context, String title) {
        super(context, R.style.ProgressDialogTheme);
        this.context =context;
        this.title = title;
        this.setTitle("");
//        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
//        View view = inflater.inflate(R.layout.custom_progress_bar, null);
//        message = (TextView)view.findViewById(R.id.message_text);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progress_bar);
        message = (TextView)findViewById(R.id.message_text);
        message.setText(title);

    }




}
