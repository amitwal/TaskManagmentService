package application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;

import application.model.Task;

public class TaskContollerTest {
	@Test
	void shouldAllTask() {
        TaskService taskService = mock(TaskService.class);
        ArrayList<Task> tasks = new ArrayList<>();
        Task t = new Task();
        t.setTaskName("task1");
        t.setCompleted(false);
        t.setDescription("task1 desc");
        t.setDueDate(new Date());
        tasks.add(t);
        System.out.println("in test");
        when(taskService.getAllTasks()).thenReturn(tasks);
        int size = taskService.getAllTasks().size();
        System.out.println("number of tasks "+size);
        assertEquals(1,size);
	}

}
