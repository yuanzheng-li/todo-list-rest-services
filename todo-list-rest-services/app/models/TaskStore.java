package models;

import java.util.*;

public class TaskStore {
  private Map<Integer, Task> tasks = new HashMap<>();

  public Optional<Integer> addTask(Task task) {
    int id = task.getId();
    tasks.put(id, task);

    return Optional.ofNullable(id);
  }

  public Optional<Task> updateTask(String id, Task task) {
    int idInt = Integer.parseInt(id);

    if(tasks.containsKey(idInt)) {
      tasks.put(idInt, task);

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
