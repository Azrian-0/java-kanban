package managers.impl;

import enums.Status;
import enums.TaskType;
import exceptions.HistoryLoadException;
import exceptions.ManagerSaveException;
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(task.getId()).append(',')
                .append(task.getTaskType()).append(',')
                .append(task.getTaskName()).append(',')
                .append(task.getDescription()).append(',')
                .append(task.getStatus()).append(',');
        if (task.getTaskType() == TaskType.SUBTASK) {
            SubTask subTask = (SubTask) task;
            if (subTask.getEpicId() != 0) {
                stringBuilder.append(subTask.getEpicId()).append(',');
            }
        } else {
            stringBuilder.append("null").append(',');
        }
        stringBuilder
                .append(task.getDuration()).append(',')
                .append(task.getStartTimeToString());
        return stringBuilder.toString();
    }

    private Task fromString(String value) {
        String[] parts = value.split(",");
        final int id = Integer.parseInt(parts[0]);
        final TaskType taskType = TaskType.valueOf(parts[1]);
        final String name = parts[2];
        final String description = parts[3];
        final Status status = Status.valueOf(parts[4]);
        final long duration = Long.parseLong(parts[6]);
        final String startTime = parts[7];
        switch (taskType) {
            case TASK:
                return new Task(id, name, description, status, duration, startTime);
            case EPIC:
                return new Epic(id, name, description, status, duration, startTime);
            case SUBTASK:
                final int epicId = Integer.parseInt(parts[5]);
                return new SubTask(id, name, description, status, epicId, duration, startTime);
        }
        return null;
    }

    private static String historyToString(HistoryManager manager) {
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

    private static List<Integer> historyFromString(String value) {
        try {
            if (value == null) {
                return new ArrayList<>();
            }
            String[] arrayHistory = readFile(value).split("\n");
            String historyLine = arrayHistory[arrayHistory.length - 1];
            if (historyLine.isEmpty()) {
                return new ArrayList<>();
            }
            String[] tasksId = historyLine.split(",");
            List<Integer> historyList = new ArrayList<>();
            for (String id : tasksId) {
                historyList.add(Integer.valueOf(id));
            }
            return historyList;
        } catch (Exception e) {
            throw new HistoryLoadException();
        }
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
            String taskFields = "id,type,name,description,status,epicId,duration,startTime";
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
            throw new ManagerSaveException("Ошибка сохранения файла: " + e.getMessage());
        }
    }

    private void writeToFile(Task task, BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(toString(task));
        bufferedWriter.write("\n");
    }

    public void loadFromFile(String file) {
        String taskString = readFile(file);
        String[] lineTask = taskString.split("\n");
        List<Integer> idTask = new ArrayList<>();
        try {
            idTask = historyFromString(file);
        } catch (HistoryLoadException e) {
            System.out.println("Не удалось загрузить историю задач");
        }
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