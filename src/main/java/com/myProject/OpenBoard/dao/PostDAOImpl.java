package com.myProject.OpenBoard.dao;

import com.myProject.OpenBoard.entity.Post;
import com.myProject.OpenBoard.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;

@Repository
public class PostDAOImpl implements PostDAO{
    private EntityManager entityManager;
    public PostDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    @Override
    public void createPost(Post post) {
        entityManager.persist(post);
    }

    @Override
    public void updatePost(Post post) {
        entityManager.clear();
        entityManager.merge(post);
    }
    @Override
    public List<Post> findPublicPost() {
        TypedQuery<Post> typedQuery = entityManager.createQuery(
          "FROM Post WHERE showPost=:x", Post.class
        );
        typedQuery.setParameter("x", true);
        return typedQuery.getResultList();
    }

    @Override
    public Post findPostById(int id) {
        return entityManager.find(Post.class, id);
    }

    @Override
    public void deleteById(int id) {
        Query query = entityManager.createQuery("DELETE FROM Post WHERE id=:x");
        query.setParameter("x", id);
        query.executeUpdate();
    }


}
