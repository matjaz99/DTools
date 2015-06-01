package si.matjazcerkvenik.dtools.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Todos {
	
	private List<Todo> todoList;

	public List<Todo> getTodoList() {
		return todoList;
	}

	@XmlElement(name="todo")
	public void setTodoList(List<Todo> todoList) {
		this.todoList = todoList;
	}
	
	public void addTodo(Todo t) {
		todoList.add(t);
	}
	
	public void deleteTodo(Todo t) {
		todoList.remove(t);
	}
	
	
}
