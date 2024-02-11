package managers.impl;

import enums.Status;
import enums.TaskType;
import managers.interfaces.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String file;

    public FileBackedTasksManager(String file) {
        this.file = file;
    }

    public String toString(Task task) {
        String epicIdString = "";
        if (task.getTaskType() == TaskType.SUBTASK) {
            SubTask subTask = (SubTask) task;
            epicIdString = String.valueOf(subTask.getEpicId());
        }
        String epicIdComma = epicIdString.isEmpty() ? "" : ",";
        String finalComma = epicIdString.isEmpty() ? "" : ",";
        return String.format("%d,%s,%s,%s,%s%s%s",
                task.getId(), task.getTaskType(), task.getTaskName(), task.getDescription(), task.getStatus(), epicIdComma, epicIdString, finalComma);
    }

    public Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        TaskType taskType;
        try {
            taskType = TaskType.valueOf(parts[1]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверное значение типа задачи: " + parts[1]);
        }
        String name = parts[2];
        String description = parts[3];
        Status status;
        try {
            status = Status.valueOf(parts[4]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверное значение статуса задачи: " + parts[4] + id);
        }
        int epicId = 0;
        if (parts.length == 6 && parts[5] != null && !parts[5].isEmpty()) {
            epicId = Integer.parseInt(parts[5]);
        }
        if (taskType == TaskType.SUBTASK && epicId == 0) {
            throw new IllegalArgumentException("Отсутствует значение для epicId у SubTask");
        }
        Task task = null;
        switch (taskType) {
            case TASK:
                task = new Task(name, description, status);
                break;
            case EPIC:
                task = new Epic(name, description, status);
                break;
            case SUBTASK:
                if (epicId == 0) {
                    throw new IllegalArgumentException("Значение epicId не указано для SubTask: " + value);
                }
                Epic phantomEpic = getEpicById(epicId, false);
                if (phantomEpic == null) {
                    throw new IllegalArgumentException("Epic с id=" + epicId + " не найден для: " + value);
                }
                task = new SubTask(name, description, status, phantomEpic);
                break;
        }
        task.setId(id);
        return task;
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> tasks = manager.getHistory();
        StringBuilder taskLine = new StringBuilder();
        taskLine.append("\n");
        for (Task task : tasks) {
            taskLine.append(task.getId());
            taskLine.append(",");
        }
        taskLine.deleteCharAt(taskLine.length() - 1);
        return taskLine.toString();
    }

    public static List<Integer> historyFromString(String value) {
        if (value == null) {
            return new ArrayList<>();
        }
        String[] arrayHistory = readFile(value).split("\n");
        String historyLine = arrayHistory[arrayHistory.length - 1];
        String[] tasksId = historyLine.split(",");
        List<Integer> historyList = new ArrayList<>();
        for (String id : tasksId) {
            historyList.add(Integer.valueOf(id));
        }
        return historyList;
    }

    private static String readFile(String path) {
        try {
            return Files.readString(Path.of(path));

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла", e);
        }
    }

    public void save() {
        try {
            Writer fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String taskFields = "id,type,name,description,status,epicId";
            bufferedWriter.write(taskFields);
            bufferedWriter.write("\n");
            for (Task task : tasks.values()) {
                writeToFile(task, bufferedWriter);
            }
            for (Epic epic : epics.values()) {
                writeToFile(epic, bufferedWriter);
            }
            for (SubTask subTask : subTasks.values()) {
                writeToFile(subTask, bufferedWriter);
            }
            bufferedWriter.write(historyToString(historyManager));
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения файла", e);
        }
    }

    private void writeToFile(Task task, BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(toString(task));
        bufferedWriter.write("\n");
    }

    public void loadFromFile(String file) {
        String taskString = readFile(file);
        String[] lineTask = taskString.split("\n");
        List<Integer> idTask = historyFromString(file);
        for (int i = 1; i < lineTask.length; i++) {
            if (lineTask[i].isEmpty()) {
                break;
            }
            Task task = this.fromString(lineTask[i]);
            if (task != null) {
                switch (task.getTaskType()) {
                    case TASK:
                        super.createTask(task);
                        break;
                    case EPIC:
                        super.createEpic((Epic) task);
                        break;
                    case SUBTASK:
                        super.createSubTask((SubTask) task);
                        break;
                }
                for (Integer id : idTask) {
                    if (task.getId() == id) {
                        this.historyManager.add(task);
                    }
                }
            }
        }
        this.save();
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public ArrayList<SubTask> getEpicSubTasks(Epic epic) {
        super.getEpicSubTasks(epic);
        save();
        return epic.getSubTasks();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public Task getTaskById(Integer taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public SubTask getSubTaskById(Integer taskId) {
        SubTask subTask = super.getSubTaskById(taskId);
        save();
        return subTask;
    }

    @Override
    public Epic getEpicById(Integer taskId, boolean addToHistory) {
        Epic epic = super.getEpicById(taskId, addToHistory);
        save();
        return epic;
    }
}