package ru.kata.spring.boot_security.demo.model;




import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ]{2,}$"
            , message = "длина имени должна быть больше 2 символов, имя может содержать только буквы")
    @Column(name = "name")
    private String name;

    @Min(value = 1, message = "возраст должен положительным")
    @Column(name = "age")
    private int age;

    @NotBlank(message = "поле не должно быть пустым")
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column
    private String password;

    public User(String name,int age, String email, String password) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
