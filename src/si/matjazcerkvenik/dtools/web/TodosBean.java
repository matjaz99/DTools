package si.matjazcerkvenik.dtools.web;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;

import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.dtools.xml.Todo;

@ManagedBean
@SessionScoped
public class TodosBean {
	
	private String title;
	private String description;
	private Date deadline;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	public List<Todo> getTodoList() {
		return DAO.getInstance().loadTodos().getTodoList();
	}
	
	public void addTodoAction() {
		
		Todo t = new Todo();
		t.setTitle(title);
		t.setDescription(description);
		t.setCreated(new Date());
		if (deadline == null) {
			long ms = System.currentTimeMillis() + 3600 * 1000;
			deadline = new Date(ms);
		}
		t.setDeadline(deadline);
		
		DAO.getInstance().addTodo(t);
		
		title = null;
		description = null;
		deadline = null;
		
	}
	
	public void deleteTodo(Todo t) {
		DAO.getInstance().deleteTodo(t);
	}
	
	/**
	 * Used to display calendar
	 */
	public void click() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.update("form:display");
        requestContext.execute("PF('dlg').show()");
    }
	
	public void toggleCompleted(Todo t) {
		t.setCompleted(!t.isCompleted());
		DAO.getInstance().saveTodos();
	}
	
	public void delay(Todo t, long days) {
		long end = t.getDeadline().getTime();
		end += days * 24 * 3600 * 1000;
		t.setDeadline(new Date(end));
		DAO.getInstance().saveTodos();
	}
	
}
