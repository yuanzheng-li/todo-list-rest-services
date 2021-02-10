package models;

public class Task {
  private int id;
  private String name;
  private boolean complete;

  public Task(int id, String name, boolean complete) {
    this.id = id;
    this.name = name;
    this.complete = complete;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isComplete() {
    return complete;
  }

  public void setComplete(boolean complete) {
    this.complete = complete;
  }
}
