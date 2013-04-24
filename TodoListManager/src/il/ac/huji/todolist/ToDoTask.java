package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToDoTask implements ITodoItem{
	public String _task;
	public String _date;
	public Date _dueDate;

	public ToDoTask(String task, Date date) {
		this._task = task;
		this._dueDate = date;
	}

	@Override
	public String getTitle() {
		return _task;
	}

	@Override
	public Date getDueDate() {
		return _dueDate;
	}
}
