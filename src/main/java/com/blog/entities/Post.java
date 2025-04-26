package com.blog.entities;

import jakarta.persistence.*;
import java.util.*;
import lombok.Data;
import com.blog.entities.User;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length=5000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable=false,referencedColumnName = "id")
    private User user;
    
    private String img;

    private Date date;

    private int likeCount;
   
    private int viewCount;

    private List<String> tags;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getContent() {
    return content;
}

public void setContent(String content) {
    this.content = content;
}

public User getUser() {
    return user;
}

public void setUser(User user) {
    this.user = user;
}

public String getImg() {
    return img;
}

public void setImg(String img) {
    this.img = img;
}

public Date getDate() {
    return date;
}

public void setDate(Date date) {
    this.date = date;
}

public int getLikeCount() {
    return likeCount;
}

public void setLikeCount(int likeCount) {
    this.likeCount = likeCount;
}

public int getViewCount() {
    return viewCount;
}

public void setViewCount(int viewCount) {
    this.viewCount = viewCount;
}

}