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

    void deletePostById(int id);

    void deletePublicPostById(int id);

    List<Post> findAllUserPost();

    List<Post> findPublicPost();

    void likePublicPost(int id);

    void disLikePublicPost(int id);

    List<Post> topTenPost();

    Post findPostById(int id);

    List<User> findAllMemberUser();

    List<User> findAllModeratorUser();

    List<User> findDisabledUser();

    void promoteUserById(int id);

    void demoteUserById(int id);

    void disableUserById(int id);

    void enableUserById(int id);

    void deleteUserById(int id);

}
