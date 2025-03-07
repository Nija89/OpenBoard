package com.myProject.OpenBoard.entity;

import com.myProject.OpenBoard.dao.RoleDAO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    @NotNull(message = "is required.")
    @Size(min = 4, message = "Must contain at least 4 character.")
    private String username;

    @NotNull(message = "is required.")
    @Size(min = 5, message = "Be at least 5 characters long.")
   @Column(name = "password")
    private String password;

    @NotNull(message = "is required.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Invalid email format. Example: user@example.com")@Column(name = "email")
    private String email;

    @Column(name = "active")
    private boolean active = true;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "userList")
    private List<Role> roleList;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Post> postList;


    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name="post_like",
            joinColumns = @JoinColumn(name= "user_id"),
            inverseJoinColumns = @JoinColumn(name="post_id")
    )
    private Set<Post> likedPost = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "post_dislike",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> dislikedPost = new HashSet<>();


    public User() {
    }

    public User(String username, String password, String email, String description) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = new ArrayList<>();
    }


    public void addRole(Role newRole){
        if(roleList == null){
            roleList = new ArrayList<>();
        }
        roleList.add(newRole);
        newRole.addUser(this);
    }

    public void removeRole(){
        roleList.remove(0);
    }


    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public void addPost(Post post){
        if(postList == null){
            postList = new ArrayList<>();
        }
        postList.add(post);
        post.setUser(this);
    }

    public Set<Post> getLikedPost() {
        return likedPost;
    }

    public void setLikedPost(Set<Post> likedPost) {
        this.likedPost = likedPost;
    }

    public Set<Post> getDislikedPost() {
        return dislikedPost;
    }

    public void setDislikedPost(Set<Post> dislikedPost) {
        this.dislikedPost = dislikedPost;
    }

    public void removePost(Post post) {
        if (post != null){
            postList.remove(post);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", description='" + description + '\'' +
                '}';
    }
}
