package org.example.service;

import org.example.exception.NotFoundException;
import org.example.model.Post;
import org.example.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PostService {
  private final PostRepository repository;

  public PostService(PostRepository repository) {
    this.repository = repository;
  }

  public Collection<Post> all() {
    return repository.all();
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(repository.getById(id));
  }

  public Post save(Post post) {
    Post result = repository.save(post);
    if (result == null)
      throw new NotFoundException(
              String.format("Не удалось обновить запись с id = %d т.к. она отсутствует", post.getId()));
    return result;
  }

  public void removeById(long id) throws NotFoundException {
    try {
      repository.removeById(id);
    } catch (NotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException("Error while removing post by id", e);
    }
  }
}