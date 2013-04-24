package il.ac.huji.todolist;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class TodoListAdapter extends ArrayAdapter<ToDoTask> {

	public TodoListAdapter(TodoListManagerActivity activity, ArrayList<ToDoTask> todoList) {
			super(activity, android.R.layout.simple_list_item_1, todoList);
		}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);
		TextView txtTitle = (TextView)view.findViewById(R.id.txtTodoTitle);
		TextView txtDate = (TextView)view.findViewById(R.id.txtTodoDueDate);

		ToDoTask task = getItem(position);
		txtTitle.setText(task._task);
		if (task._date == "No due date")
		{
			txtDate.setText("No due date");
		}
		else 
		{
			txtDate.setText(task._date);		
	
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
			Date task_date = null;
			try {
				task_date = sdf.parse(task._date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Date now = new Date();
			if (now.after(task_date))
			{
				txtTitle.setTextColor(Color.RED);
				txtDate.setTextColor(Color.RED);
			}
		}
		return view;
	}
}
