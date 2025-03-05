package com.myProject.OpenBoard.service;

import com.myProject.OpenBoard.entity.Post;
import com.myProject.OpenBoard.entity.Role;
import com.myProject.OpenBoard.entity.User;

import java.util.List;

public interface MainService {

    Role findRoleByName(String roleName);
    void createUser(User user);

    void updateUser(User user);

    boolean updatePassword(User user, String currentPassword, String newPassword);

    User findUserById(int id);

    User findUserByName(String name);

    void createPost(Post post);

    void updatePost(Post post);

    public void deletePostById(int id);

    List<Post> findAllUserPost();

    List<Post> findPublicPost();

    Post findPostById(int id);

}
