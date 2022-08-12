package application.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.Task;
import application.model.TaskId;
import application.service.TaskService;

@CrossOrigin
@RestController
@RequestMapping("/tasks")
public class TaskController {
		
	@Autowired
    private TaskService taskService;
	
		
		
	/* This GET method will return all the tasks from db.
	 * 
	 * @returns array of Tasks
	 */
	@CrossOrigin
 	@GetMapping
    public ArrayList<Task> getAllTasks() {        
        return taskService.getAllTasks();
    }
	/*
	 * This PUT method save the new documents in the DB
	 * 
	 * @return status as boolean
	 */
	@CrossOrigin
 	@PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Boolean saveTask(@RequestBody Task task) {
		return taskService.addTask(task);
	}

	/*
	 * This method delete a document from the DB
	 * 
	 * @return status as boolean
	 */
	@CrossOrigin
 	@DeleteMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Boolean deleteTask(@RequestBody TaskId taskid) {
		
		return taskService.deleteTask(taskid);
		
	}
	/*
	 * This method update the existing document and set the completed flag to true.
	 * 
	 * @return status as boolean
	 */

	@CrossOrigin
 	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Boolean markCompleteTask(@RequestBody TaskId taskid) {
		
		return taskService.markAsCompleteTask(taskid);
		
	}
		
}