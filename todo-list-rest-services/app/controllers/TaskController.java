package controllers;

import Util.Util;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Task;
import models.TaskStore;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TaskController extends Controller {
  private HttpExecutionContext executionContext;
  private TaskStore taskStore;

  @Inject
  public TaskController(HttpExecutionContext executionContext, TaskStore taskStore) {
    this.executionContext = executionContext;
    this.taskStore = taskStore;
  }

  public CompletionStage<Result> list() {
    return supplyAsync(() -> {
      Set<Task> result = taskStore.getAllTasks();
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonData = mapper.convertValue(result, JsonNode.class);

      return ok(Util.createResponse("tasks", jsonData));
    }, executionContext.current());
  }

  public CompletionStage<Result> create(Http.Request request) {
    JsonNode json = request.body().asJson();

    return supplyAsync(() -> {
      if(json == null) {
        return badRequest(Util.createResponse("message", "Expecting JSON Data"));
      }

      Optional<Integer> taskIdOptional = taskStore.addTask(Json.fromJson(json, Task.class));

      return taskIdOptional.map((task) -> {
        JsonNode jsonObject = Json.toJson(task);
        return ok(Util.createResponse("id", jsonObject));
      }).orElse(internalServerError(Util.createResponse("message", "Could not create data")));
    }, executionContext.current());
  }

  public CompletionStage<Result> update(Http.Request request, String id) {
    JsonNode json = request.body().asJson();

    return supplyAsync(() -> {
      if(json == null) {
        return badRequest(Util.createResponse("message", "Expecting JSON Data"));
      }

      Optional<Task> taskOptional = taskStore.updateTask(id, Json.fromJson(json, Task.class));

      return taskOptional.map((task) -> {
        if(task == null) {
          return notFound(Util.createResponse("message", "Task Not Found"));
        }

        return noContent();
      }).orElse(internalServerError(Util.createResponse("message", "Could not create data")));
    }, executionContext.current());
  }

  public CompletionStage<Result> deleteAll() {
    return supplyAsync(() -> {
      boolean deleted = taskStore.deleteAllTasks();

      if(!deleted) {
        return internalServerError("message", "Could not delete data");
      }

      return noContent();
    }, executionContext.current());
  }
}
