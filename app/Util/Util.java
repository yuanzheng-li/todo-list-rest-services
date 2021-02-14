package Util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public class Util {
  public static ObjectNode createResponse(String fieldName, Object response) {
    ObjectNode result = Json.newObject();

    if(response instanceof String) {
      result.put(fieldName, (String) response);
    } else {
      result.putPOJO(fieldName, response);
    }

    return result;
  }
}
