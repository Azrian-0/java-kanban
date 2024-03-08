package tasks;

import com.google.gson.annotations.SerializedName;
import enums.Status;
import enums.TaskType;
import java.time.LocalDateTime;
import java.util.Objects;

import static util.GsonMappingConfig.*;

public class Task {
    @SerializedName("taskType")
    protected TaskType taskType;
    @SerializedName("name")
    protected String taskName;
    @SerializedName("description")
    protected String description;
    @SerializedName("id")
    protected int id;
    @SerializedName("status")
    protected Status status;
    @SerializedName("duration")
    protected long duration = 0;

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @SerializedName("startTime")
    protected LocalDateTime startTime;

    public Task(String taskName, String description, Status status, long duration, String startTime) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        if (startTime != null && !(startTime.equals("null"))) {
            createTime(duration, startTime);
        }
    }

    public Task(int id, String name, String description, Status status, long duration, String startTime) {
        this(name, description, status, duration, startTime);
        this.id = id;
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

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    @Override
    public String toString() {
        String toString =
                "Название задачи = " + taskName +
                        " / описание = " + description +
                        " / id = " + id +
                        " / статус = " + status;
        if (duration == 0) {
            return toString + " / продолжительность = " + duration;
        } else return toString + " / продолжительность = " + duration +
                " /  дата = " + startTime.format(DEFAULT_FORMATTER);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && taskType == task.taskType &&
                Objects.equals(taskName, task.taskName) && status == task.status &&
                Objects.equals(description, task.description) &&
                Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskType, taskName, status, description, duration, startTime);
    }

    public String getStartTimeToString() {
        return startTime.format(DEFAULT_FORMATTER);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration);
        }
        return null;
    }

    public String getEndTimeToString() {
        if (startTime != null) {
            return startTime.plusMinutes(duration).format(DEFAULT_FORMATTER);
        }
        return null;
    }

    public void createTime(long duration, String startTime) {
        this.duration = duration;
        this.startTime = LocalDateTime.parse(startTime, DEFAULT_FORMATTER);
    }
}