package tasks;

import enums.Status;
import enums.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<SubTask> subTasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String taskName, String description, long duration, String time) {
        super(taskName, description, Status.NEW, duration, time);
    }

    public Epic(String taskName, String description, Status status, long duration, String time) {
        super(taskName, description, status, duration, time);
    }

    public Epic(int id, String name, String description, Status status, long duration, String time) {
        super(id, name, description, status, duration, time);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTask(SubTask subTask) {
        if (Objects.isNull(subTasks)){
            subTasks = new ArrayList<>();
        }
        subTasks.add(subTask);
    }

    public void clearSubTasks() {
        subTasks.clear();
    }

    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = Objects.isNull(subTasks) ? new ArrayList<SubTask>() : subTasks;
    }
}