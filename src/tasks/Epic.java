package tasks;

import enums.Status;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String taskName, String description, Status status) {
        super(taskName, description, status);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(SubTask subTask) {
        subTasks.add(subTask);
    }

    public void clearSubTasks() {
        subTasks.clear();
    }
}