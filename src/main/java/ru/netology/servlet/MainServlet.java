package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
    private PostController controller;

    @Override
    public void init() {
        controller = context.getBean("postController", PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing

            if (method.equals("GET") && path.equals("/api/posts")) {
                controller.all(resp);
                return;
            }

            if (method.equals("GET") && path.matches("/api/posts/\\d+")) {
                // easy way
                getPost(path, resp);
                return;
            }

            if (method.equals("POST") && path.equals("/api/posts")) {
                controller.save(req.getReader(), resp);
                return;
            }

            if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
                // easy way
                deletePost(path, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private long parseId(String path) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
        return id;
    }

    private void deletePost(String path, HttpServletResponse resp) {
        try{
            final var id = parseId(path);
            controller.removeById(id, resp);
        } catch (NotFoundException e){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    private void getPost(String path, HttpServletResponse resp) throws IOException {
        try{
            final var id = parseId(path);
            controller.getById(id, resp);
            return;
        } catch (NotFoundException e){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

