package util;

import tasks.Task;

import java.time.LocalDateTime;
import java.util.Comparator;

public class StartTimeComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        LocalDateTime startTime1 = o1.getStartTime();
        LocalDateTime startTime2 = o2.getStartTime();

        if (startTime1 == null && startTime2 == null) {
            return 0; // если оба null, считаем их равными
        } else if (startTime1 == null) {
            return -1; // null меньше любого не-null значения
        } else if (startTime2 == null) {
            return 1; // любое не-null значение больше null
        }
        return startTime1.compareTo(startTime2);
    }
}
