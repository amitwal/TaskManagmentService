package application.service;

import java.util.ArrayList;

import application.model.Task;
import application.model.TaskId;

public interface TaskService {
	public ArrayList<Task> getAllTasks();
	public boolean addTask(Task task);
	public boolean markAsCompleteTask(TaskId taskid);
	public boolean deleteTask(TaskId taskid);

}
