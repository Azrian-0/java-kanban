package tasks;

import enums.Status;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String taskName, String description, Status status, ArrayList<SubTask> subTasks) {
        super(taskName, description, status);
        this.subTasks.addAll(subTasks);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        for (SubTask subTask : subTasks) {
            subTask.setEpicId(id);
        }
    }
}