package models;

import java.util.*;

public class TaskStore {
  private Map<Integer, Task> tasks = new HashMap<>();

  public Optional<Task> addTask(Task task) {
    int id = task.getId();
    tasks.put(id, task);

    return Optional.ofNullable(task);
  }

  public Optional<Task> updateTask(Task task) {
    int id = task.getId();

    if(tasks.containsKey(id)) {
      tasks.put(id, task);

      return Optional.ofNullable(task);
    }

    return null;
  }

  public Set<Task> getAllTasks() {
    return new HashSet<>(tasks.values());
  }

  public boolean deleteAllTasks() {
    tasks.clear();

    return tasks.size() == 0;
  }
}
