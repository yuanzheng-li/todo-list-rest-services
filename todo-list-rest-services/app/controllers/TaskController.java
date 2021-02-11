package controllers;

import Util.Util;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Task;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.TaskRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TaskController extends Controller {
  private HttpExecutionContext executionContext;
  private TaskRepository taskRepository;

  @Inject
  public TaskController(HttpExecutionContext executionContext, TaskRepository taskRepository) {
    this.executionContext = executionContext;
    this.taskRepository = taskRepository;
  }

  public CompletionStage<Result> list() {
    return taskRepository.list().thenApplyAsync((tasks) -> {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonData = mapper.convertValue(tasks, JsonNode.class);

      return ok(Util.createResponse("tasks", jsonData));
    }, executionContext.current());
  }

  public CompletionStage<Result> create(Http.Request request) {
    JsonNode json = request.body().asJson();

    if(json == null) {
      return supplyAsync(() -> badRequest(Util.createResponse("message", "Expecting JSON Data")), executionContext.current());
    }

    return taskRepository.add(Json.fromJson(json, Task.class)).thenApplyAsync((id) -> {
      JsonNode jsonObject = Json.toJson(id);
      return ok(Util.createResponse("id", jsonObject));
    }, executionContext.current());
  }

  public CompletionStage<Result> update(Http.Request request, String id) {
    JsonNode json = request.body().asJson();

    if(json == null) {
      return supplyAsync(() -> badRequest(Util.createResponse("message", "Expecting JSON Data")), executionContext.current());
    }

    return taskRepository.update(Integer.parseInt(id), Json.fromJson(json, Task.class)).thenApplyAsync((idOptional) -> {
        if(!idOptional.isPresent()) {
          return notFound(Util.createResponse("message", "Task Not Found"));
        }

        return noContent();
    }, executionContext.current());
  }

  public CompletionStage<Result> deleteAll() {
    try {
      return taskRepository.deleteAll().thenApplyAsync((deletedCount) -> ok(Util.createResponse("deletedCount", deletedCount)), executionContext.current());
    } catch (Exception exception) {
      return supplyAsync(() -> internalServerError("message", "Could not delete data"), executionContext.current());
    }
  }
}
