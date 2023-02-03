package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
  private ConcurrentHashMap <Long, Post> postMap = new ConcurrentHashMap();
  private AtomicLong id = new AtomicLong();
  public List<Post> all() {
    if (postMap.isEmpty()){
      return Collections.emptyList();
    }
    List <Post> postList = new ArrayList<>();
    for (long key: postMap.keySet()) {
      postList.add(postMap.get(key));
    }
    return postList;
  }

  public Optional<Post> getById(long id) {
    if (!postMap.containsKey(id)){
      throw new NotFoundException();
    }
    return Optional.of(postMap.get(id));
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
