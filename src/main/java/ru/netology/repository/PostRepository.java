package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import javax.servlet.ServletResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// Stub
public class PostRepository {
  private ConcurrentHashMap <Long, Post> postMap = new ConcurrentHashMap();
  private AtomicLong id = new AtomicLong();
  public List<Post> all() {
    if (postMap.isEmpty()){
      return Collections.emptyList();
    }
    return postMap.values().stream().collect(Collectors.toList());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(postMap.get(id)).map(Optional::of).orElseThrow(NotFoundException::new);
  }

  public Post save(Post post) {
    if (!postMap.containsKey(post.getId()) && post.getId() != 0){
      throw new NotFoundException();
    }
    if (post.getId() == 0){
      post.setId(id.incrementAndGet());
    }
    postMap.put(post.getId(), post);
    return post;
  }

  public void removeById(long id) {
    if (!postMap.containsKey(id))
      throw new NotFoundException();
    postMap.remove(id);
  }
}
