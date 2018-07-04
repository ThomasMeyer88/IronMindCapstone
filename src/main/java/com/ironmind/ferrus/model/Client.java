package com.ironmind.ferrus.model;


import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="users")
public class Client {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 100)
    private Long height;

    @Column(length = 100)
    private Long weight;

    @Column(length = 100)
    private String sex;

    @Column(length = 100)
    private String imgurl;

    @Column(length = 1000)
    private String bio;

    @Column()
    private String coach_id;

    @OneToMany (mappedBy = "client")
    private List<Program> programs;

    public Client(Long id, String name, String username, String email, String password, Long height, Long weight, String sex, String imgurl, String bio, String coach_id){
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.imgurl = imgurl;
        this.bio = bio;
        this.coach_id = coach_id;
    }



    public Client(Long id, String name, String username, String email, String password){
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Client(String name, String username, String email, String password){
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Client(Client copy){
        id = copy.id;
        email = copy.email;
        username = copy.username;
        password = copy.password;
    }

    public Client(String username, String password){
        this.username = username;
        this.password = password;
    }

    public Client(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(String coach_id) {
        this.coach_id = coach_id;
    }
}
