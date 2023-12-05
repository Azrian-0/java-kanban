package tasks;

import enums.Status;

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
}