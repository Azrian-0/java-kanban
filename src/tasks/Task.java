package tasks;

import enums.Status;

public class Task {
    protected String taskName;
    protected String description;
    protected int id;
    protected Status status;

    public Task(String taskName, String description, Status status) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "Название задачи = " + taskName +
                        " / описание = " + description +
                        " / id = " + id +
                        " / статус = " + status;
    }
}