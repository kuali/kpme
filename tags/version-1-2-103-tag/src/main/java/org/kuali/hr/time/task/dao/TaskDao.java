package org.kuali.hr.time.task.dao;


import org.kuali.hr.time.task.Task;

import java.sql.Date;
import java.util.List;

public interface TaskDao {
    public Task getTask(Long task, Date asOfDate);

    public void saveOrUpdate(Task task);
    public void saveOrUpdate(List<Task> tasks);
    public Task getMaxTask();

    List<Task> getTasks(Long task, String description, Long workArea, String workAreaDesc, Date fromEffdt, Date toEffdt);
    public int getTaskCount(Long task);
}