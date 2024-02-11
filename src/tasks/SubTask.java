package tasks;

import enums.Status;
import enums.TaskType;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String taskName, String description, Status status, Epic epic) {
        super(taskName, description, status);
        epicId = epic.getId();
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }
}