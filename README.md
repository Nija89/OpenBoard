## Project Description:

Open Board is a dynamic website that allows multiple users to sign up, log in, and create posts with a title and description. Users have the option to set the visibility of their posts as either public or private. Public posts can be viewed by other users, while private posts are visible only to the author.

Public posts allow users to interact with them by liking or disliking. Additionally, the website features a page that showcases the top 10 posts based on user likes and dislikes, ensuring a dynamic and engaging experience.


### Feature User Roles:

1. Admin:
- Can manage users (enable, disable, change roles). 
- Can delete any public  post.
- Can monitor site activity.

2. Moderator:
- Can manage public posts (edit, delete inappropriate content).
- Cannot delete users but can report issues to Admin.

3. Member (Default Role):
- Can create, edit, and delete their own posts.
- Can like/dislike public posts.
- Can view the Top 10 Posts page.


### Post Visibility:

- Public Posts: Visible to all users. Other users can like or dislike them.
- Private Posts: Visible only to the author. Not accessible by other users.


### Top 10 Posts Page:

This page showcases the top 10 posts based on the number of likes and dislikes they receive. It provides a ranking of posts and offers users a way to engage with popular content.


### Schema Location:

The SQL schema files are located in the SQL_Schema folder at the root of the project directory.


## Technologies Used:
- Java
- Spring Boot
- Thymeleaf (for rendering views)
- Bootstrap (for styling)
- Spring Security (for user authentication and authorization)
