package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.content.Intent;

public class TodoListManagerActivity extends Activity {

    private TodoDAL todoDal;
    private TodoDBAdapter todoDBAdapter;
    
    private TodoListAdapter adapter;
    private ArrayList<ToDoTask> todoList;
    private ListView todoListView;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);

        todoDal = new TodoDAL(this); // DB maintainance

        todoListView = (ListView)findViewById(R.id.lstTodoItems);        
        registerForContextMenu(todoListView);

        String[] from = {"title","due"}; // Adapter params
		int[] to = {R.id.txtTodoTitle,R.id.txtTodoDueDate};
        todoDBAdapter = new TodoDBAdapter(this, R.layout.row, todoDal.displayCursor,from,to);
        todoListView.setAdapter(todoDBAdapter);
    }
    
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo info) {
		super.onCreateContextMenu(menu, v, info);
		getMenuInflater().inflate(R.menu.ctxmenu, menu);

		String title = todoDal.displayCursor.getString(1);

		menu.setHeaderTitle(title);

		if (!title.startsWith("Call "))
			{
		 	menu.removeItem(R.id.menuItemCall);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.menuItemDelete:
			todoDal.delete(new ToDoTask(todoDal.displayCursor.getString(1),new Date()));
			break;
		case R.id.menuItemCall:
			Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ todoDal.displayCursor.getString(1).substring(5))); 
			startActivity(dial); 
		}
		return true;
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.main, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	    	case R.id.menuItemAdd:
    		Intent intent = new Intent(this, AddNewTodoItemActivity.class); 
    		startActivityForResult(intent, 42); 
    		break;
    	}
	    return true;
    }
    	
    protected void onActivityResult(int reqCode, int resCode, Intent data) { 
    	if (resCode == RESULT_CANCELED)
    	{
    		return;
    	}
    	switch (reqCode) { 
    	case 42:
    		Date date = null;
    		if (data.getExtras().containsKey("due") )
    		{
        		date = (Date) data.getSerializableExtra("due");    			
    		}
    		String task = data.getStringExtra("title");
    		ToDoTask todoTask = new ToDoTask(task,date);
     		todoDal.insert(todoTask);
     	break;
    	}
    }

}
	