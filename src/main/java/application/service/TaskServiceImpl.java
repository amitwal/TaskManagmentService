package application.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.ibm.cloud.cloudant.v1.Cloudant;
import com.ibm.cloud.cloudant.v1.model.AllDocsResult;
import com.ibm.cloud.cloudant.v1.model.DeleteDocumentOptions;
import com.ibm.cloud.cloudant.v1.model.DocsResultRow;
import com.ibm.cloud.cloudant.v1.model.Document;
import com.ibm.cloud.cloudant.v1.model.DocumentResult;
import com.ibm.cloud.cloudant.v1.model.GetDocumentOptions;
import com.ibm.cloud.cloudant.v1.model.PostAllDocsOptions;
import com.ibm.cloud.cloudant.v1.model.PostDocumentOptions;
import com.ibm.cloud.cloudant.v1.model.PutDocumentOptions;

import application.model.Task;
import application.model.TaskId;

@Service
public class TaskServiceImpl implements TaskService {

	static Cloudant client = Cloudant.newInstance();
	String dbName = "tasks";
	
	/* This method return all the tasks from db.
	 * 
	 * @returns array of Tasks
	 */
	@Override
	public ArrayList<Task> getAllTasks() {
		ArrayList<Task> list = new ArrayList<>();
        List<DocsResultRow>  allDocs = getAllDocs();
        for ( int i=0; i < allDocs.size() ; i++ ) {
        	DocsResultRow doc = allDocs.get(i);
        	Task task = new Task();
        	task.setId(doc.getDoc().getId());
        	String taskName = doc.getDoc().get("taskName").toString();
        	task.setTaskName(taskName);
        	String desc = doc.getDoc().get("description").toString();
        	task.setDescription(desc);
        	if (doc.getDoc().get("dueDate")==null) {
        		continue;
        	}
        	String due = doc.getDoc().get("dueDate").toString();
        	task.setRev(doc.getDoc().getRev());
        	Date date;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(due);
				task.setDueDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
				date = new Date();
			}
            String x = doc.getDoc().get("completed").toString();
            if (x.equalsIgnoreCase("true")) {
            	task.setCompleted(true);
            } else {
            	task.setCompleted(false);
            }
            list.add(task);
        }
		return list;
	}

	@Override
	public boolean addTask(Task task) {
		if (StringUtils.isEmpty(task.getTaskName()) || StringUtils.isEmpty(task.getDueDate().toString())) {
			return false;
		}
		Document productsDocument = new Document();
		productsDocument.put("taskName", task.getTaskName());
		productsDocument.put("description", task.getDescription());
		productsDocument.put("dueDate", task.getDueDate());
		productsDocument.put("completed", "false");
		
		PostDocumentOptions documentOptions =
		    new PostDocumentOptions.Builder()
		        .db(dbName)
		        .document(productsDocument)
		        .build();

		DocumentResult response =
		    client.postDocument(documentOptions).execute()
		        .getResult();
		
		return response.isOk();
	}

	@Override
	public boolean markAsCompleteTask(TaskId taskid) {
		GetDocumentOptions findDocumentOptions =
			    new GetDocumentOptions.Builder()
			        .db(dbName)
			        .docId(taskid.getId())
			        .build();

			Document doc =
			    client.getDocument(findDocumentOptions).execute()
			        .getResult();
			doc.put("completed", true);
			
			PutDocumentOptions documentOptions =
				    new PutDocumentOptions.Builder()
				        .db(dbName)
				        .docId(doc.getId())
				        .document(doc)
				        .build();

				DocumentResult response =
				    client.putDocument(documentOptions).execute()
				        .getResult();
		return response.isOk();
	}

	@Override
	public boolean deleteTask(TaskId taskid) {
		DeleteDocumentOptions documentOptions =
			    new DeleteDocumentOptions.Builder()
			        .db(dbName)
			        .rev(taskid.getRev())
			        .docId(taskid.getId())
			        .build();

			DocumentResult response =
			    client.deleteDocument(documentOptions).execute()
			        .getResult();
		
		
		return response.isOk();
	}

	public List<DocsResultRow> getAllDocs() {
 		PostAllDocsOptions all = new PostAllDocsOptions.Builder(dbName).includeDocs(true).build();
 		AllDocsResult allDocs = client.postAllDocs(all).execute().getResult();
 		return allDocs.getRows();
 	}
}
