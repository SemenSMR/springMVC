package org.example.controller;

import com.google.gson.Gson;
import org.example.exception.NotFoundException;
import org.example.model.Post;
import org.example.service.PostService;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

@Controller
public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private final PostService service;
  private final Gson gson = new Gson();

  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
   sendResponse(response,service.all());
  }

  public void getById(long id, HttpServletResponse response) throws IOException {
    try {
      final var data = service.getById(id)
              .orElseThrow(() -> new NotFoundException(String.format("Запись с id=%d отсутствует", id)));
      sendResponse(response, data);
    } catch (NotFoundException e) {
      sendResponse(response, e.getMessage());
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var gson = new Gson();
    final var post = gson.fromJson(body, Post.class);
    final var data = service.save(post);
    response.getWriter().print(gson.toJson(data));
  }

  public void removeById(long id, HttpServletResponse response) throws IOException {
    try {
      service.removeById(id);
      response.getWriter().print("{\"message\": \"Post deleted\"}");
    } catch (NotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
    }
  }
  private <T> void sendResponse(HttpServletResponse response, T data) throws IOException {
    response.setContentType(APPLICATION_JSON);
    response.getWriter().print(gson.toJson(data));
  }
}
