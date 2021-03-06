package com.sample.task.service;

import com.sample.task.dao.ProjectMgrDao;
import com.sample.task.dao.TaskMgrDao;
import com.sample.task.dao.UserMgrDao;
import com.sample.task.domain.*;
import com.sample.task.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskMgrService {
    @Autowired
    TaskMgrDao taskMgrDao;
    @Autowired
    UserMgrDao userMgrDao;
    @Autowired
    ProjectMgrDao projectMgrDao;

    @Transactional
    public void addTask(Task task) throws  NotFoundException {

        updateParentObjects(task);

        taskMgrDao.addTask(task);
    }

    private void updateParentObjects(Task task) throws NotFoundException {
        if (task.getParentTask() != null){
            Task parentTaskref=taskMgrDao.viewTask(task.getParentTask());
            if (parentTaskref != null){
                ParentTask parentTask =new ParentTask();
                parentTask.setTask(task.getParentTask());
                parentTask.setId(parentTaskref.getId());
                task.setParentTaskEntity(parentTask);
            }
            else {
                throw new NotFoundException("Parent Task Not Found");
            }
        }

        if (task.getUserId() != null){
            User user=userMgrDao.viewUser(task.getUserId());
            if (user != null){
                task.setUser(user);
            }
            else {
                throw new NotFoundException("Parent Task Not Found");
            }
        }

        if (task.getProjectId() != null){
            Project project=projectMgrDao.viewProject(task.getProjectId());
            if (project != null){
                task.setProject(project);
            }
            else {
                throw new NotFoundException("Parent Task Not Found");
            }
        }
    }

    @Transactional
    public void updateTask(Task task) throws NotFoundException{
        Task viewTask =taskMgrDao.viewTask(task.getTask());
        if (viewTask != null){
            task.setId(viewTask.getId());
            updateParentObjects(task);
            taskMgrDao.updateTask(task);
        }
    }

    @Transactional
    public void deleteTask(String task){

        taskMgrDao.deleteTask(task);
    }


    @Transactional
    public Task viewTask(String task){
        return taskMgrDao.viewTask(task);
    }

    @Transactional
    public List<Task> listTask(SearchTask searchTask){
        return taskMgrDao.listTask(searchTask);
    }
}
