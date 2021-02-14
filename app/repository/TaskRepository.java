package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Transaction;
import models.Task;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TaskRepository {
  private final EbeanServer ebeanServer;
  private final DatabaseExecutionContext databaseExecutionContext;

  @Inject
  public TaskRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext databaseExecutionContext) {
    this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
    this.databaseExecutionContext = databaseExecutionContext;
  }

  public CompletionStage<List<Task>> list() {
    return supplyAsync(() -> ebeanServer.find(Task.class).findList(), databaseExecutionContext);
  }

  public CompletionStage<Integer> add(Task task) {
    return supplyAsync(() -> {
      ebeanServer.insert(task);
      return task.id;
    }, databaseExecutionContext);
  }

  public CompletionStage<Optional<Integer>> update(int id, Task task) {
    return supplyAsync(() -> {
      Transaction transaction = ebeanServer.beginTransaction();
      Optional<Integer> idOptional = Optional.empty();
      try {
        Task oldTask = ebeanServer.find(Task.class).setId(id).findOne();

        if(oldTask != null) {
          oldTask.complete = task.complete;
          oldTask.updatedAt = task.updatedAt;

          oldTask.update();
          transaction.commit();

          idOptional = Optional.of(id);
        }
      } finally {
        transaction.end();
      }

      return idOptional;
    }, databaseExecutionContext);
  }

  public CompletionStage<Integer> deleteAll() throws Exception {
    return this.list().thenApplyAsync((tasks) -> {
      int deletedCount = 0;
      if(!tasks.isEmpty()) {
        Transaction transaction = ebeanServer.beginTransaction();

        try {
          deletedCount = ebeanServer.deleteAll(tasks);

          transaction.commit();
        } catch (Exception exception) {
          throw exception;
        } finally {
          transaction.end();
        }
      }

      return deletedCount;
    }, databaseExecutionContext);
  }
}
