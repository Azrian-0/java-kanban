package tasks;

import com.google.gson.annotations.SerializedName;
import enums.Status;
import enums.TaskType;

public class SubTask extends Task {
    @SerializedName("epicId")
    private int epicId;

    public SubTask(String taskName, String description, Status status, Epic epic, long duration, String time) {
        super(taskName, description, status, duration, time);
        epicId = epic.getId();
    }

    public SubTask(int id, String name, String description, Status status, int epicId, long duration, String time) {
        super(id, name, description, status, duration, time);
        this.epicId = epicId;
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