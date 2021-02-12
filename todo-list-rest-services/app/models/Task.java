package models;

import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Task extends Model {
  @Id
  @Constraints.Required
  public int id;

  @Constraints.Required
  public String name;

  public boolean complete;

  @Constraints.Required
  public Long updatedAt;
}
