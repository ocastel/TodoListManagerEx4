package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class TodoDAL {
	
	
	public SQLiteDatabase db;
	public Cursor displayCursor;
	
	
	public TodoDAL(Context context) {
		
		TodoDatabaseHelper helper = new TodoDatabaseHelper(context);	
		db = helper.getWritableDatabase();
		displayCursor = db.query("todo", new String[] {"_id","title","due"},null,null,null,null,null);
		displayCursor.moveToFirst();

		Parse.initialize(context, context.getResources().getString(R.string.parseApplication),context.getResources().getString(R.string.clientKey));
		ParseUser.enableAutomaticUser();	
	}
	
	 
	public boolean insert(ITodoItem todoItem){
		String title = todoItem.getTitle();
		Date dueDate = todoItem.getDueDate();
		ParseObject parseObj = new ParseObject("todo"); 		
		parseObj.put("title", title);
		if (dueDate == null)
		{
			parseObj.put("due",JSONObject.NULL);
		}
		else
		{
			parseObj.put("due", dueDate.getTime());
		}
		parseObj.saveInBackground();
						
		ContentValues todoTitleDate = new ContentValues(); 
		todoTitleDate.put("title", title); 
		if (dueDate == null)
		{
			todoTitleDate.putNull("due");
		}
		else
		{
			todoTitleDate.put("due", dueDate.getTime()); 
		}
		long retVal = db.insert("todo", null, todoTitleDate);
		if (retVal == -1) {
			return false;
		}
		displayCursor.requery();
		return true;
	}
	 
	public boolean update(ITodoItem todoItem){
		if (todoItem == null) return false;
		final ITodoItem finalTodoItem = todoItem;
		ParseQuery query = new ParseQuery("todo"); 
		query.whereEqualTo("title", todoItem.getTitle());
		query.findInBackground(new FindCallback() {
		@ Override
			public void done(List<ParseObject> matches, com.parse.ParseException e) { 
				if (e == null){
					if (!matches.isEmpty()){ //if there are any matches to the query
						matches.get(0).put("dueDate", finalTodoItem.getDueDate().getTime());
						matches.get(0).saveInBackground();
						//Assuming the list will include only 1 entry
					}
				}
				else { e.printStackTrace(); }
			}
		});
		ContentValues contentValues = new ContentValues();
		if(!(todoItem.getDueDate().equals(null))){ // Null date
			contentValues.put("due", todoItem.getDueDate().getTime());
		}
		else { contentValues.putNull("due"); }
		// DB update
		long retVal = db.update("todo",contentValues , "title=?", new String[]{todoItem.getTitle()});
		if (retVal != 1) {
			return false;
		}
		displayCursor.requery();
		return true;		
	}
	 
	public boolean delete(ITodoItem todoItem){
		// Parse delete
		ParseQuery query = new ParseQuery("todo"); 
		query.whereEqualTo("title", todoItem.getTitle());
		query.findInBackground(new FindCallback()
			{
			@Override
			public void done(List<ParseObject> matches, com.parse.ParseException  e) { 
				if (e == null){
					if (!matches.isEmpty()){
						matches.get(0).deleteInBackground();
					}
				}
				else {
					e.printStackTrace();
				}
			}
		});
		// DB delete
		long retVal = db.delete("todo", "title = ?", new String[] {todoItem.getTitle() });
		if (retVal != 1) {
			return false;
		}
		displayCursor.requery();
		return true;		
	}
	
	public List<ITodoItem> all() {
		List<ITodoItem> todoList = new ArrayList<ITodoItem>();
		displayCursor = db.query("todo", new String[] { "title", "due" },
		null, null, null, null, null);
		if (displayCursor.moveToFirst()) {
			do {
			String activity = displayCursor.getString(0);
			long due = displayCursor.getLong(1);
			Date dueDate = new Date(due);
			todoList.add(new ToDoTask(activity, dueDate));
			} while (displayCursor.moveToNext());
		}		
		return todoList;
	}
}