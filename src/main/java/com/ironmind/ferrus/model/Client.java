package com.ironmind.ferrus.model;


import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="users")
public class Client {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long coachId;

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
    private Long phonenumber;

    @Column(length = 100)
    private String imgurl;

    @Column(length = 1000)
    private String bio;

    @Column(length= 100)
    private Long activeprogram;


    @Column(nullable = false)
    private String role;



    @OneToMany (mappedBy = "client")
    private List<Program> programs;

    @OneToMany (mappedBy = "client")
    private List<CompletedSet> completedSets;





    public Client(Long id, String name, String username, String email, String password){
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Client(String name, String username, String email, String password, Long phonenumber){
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
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




    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    public List<CompletedSet> getCompletedSets() {
        return completedSets;
    }


    public void setCompletedSets(List<CompletedSet> completedSets) {
        this.completedSets = completedSets;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public Long getActiveprogram() {
        return activeprogram;
    }

    public void setActiveprogram(Long activeprogram) {
        this.activeprogram = activeprogram;
    }

    public Long getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(Long phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
