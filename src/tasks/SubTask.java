package tasks;

import enums.Status;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String taskName, String description, Status status) {
        super(taskName, description, status);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}