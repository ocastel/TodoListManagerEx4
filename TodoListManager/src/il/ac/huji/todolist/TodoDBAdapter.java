package il.ac.huji.todolist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TodoDBAdapter extends SimpleCursorAdapter{

    private Context myContext;
    private int myLayout;
    private Cursor myCursor;
    
	public TodoDBAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to) {
		super(context, layout, c, from, to);
        this.myContext=context;
        this.myLayout=layout;
        this.myCursor=c;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);

		TextView txtTitle = (TextView)view.findViewById(R.id.txtTodoTitle);
		TextView txtDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
		txtTitle.setText(myCursor.getString(1));
		Date date = null;
		try {
			date = new Date(myCursor.getLong(2));
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String tempDate =  sdf.format(date);
			txtDate.setText(sdf.format(date));		
			Date now = new Date();
			if (now.after(date))
			{
				txtTitle.setTextColor(Color.RED);
				txtDate.setTextColor(Color.RED);
			}
			else
			{
				txtTitle.setTextColor(Color.BLACK);
				txtDate.setTextColor(Color.BLACK);
			}
		}
			catch(Exception e) {
			txtDate.setText("No due date");
		}
		return view;
	}

}
