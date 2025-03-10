package com.myProject.OpenBoard.dao;

import com.myProject.OpenBoard.entity.Post;
import com.myProject.OpenBoard.entity.User;

import java.util.List;

public interface PostDAO {
    void createPost(Post post);

    void updatePost(Post post);

    List<Post> findPublicPost();

    Post findPostById(int id);

    void deleteById(int id);

    void deletePublicPostById(int id);

    void likePublicPost(Post post);

    void disLikePublicPost(Post post);

    List<Post> topTenPost();

}
