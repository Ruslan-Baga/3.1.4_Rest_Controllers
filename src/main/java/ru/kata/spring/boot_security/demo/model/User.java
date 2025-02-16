package ru.kata.spring.boot_security.demo.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ]{2,}$"
            , message = "длина должна быть больше 2 символов, и может содержать только буквы")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ]{2,}$"
            , message = "длина должна быть больше 2 символов, и может содержать только буквы")
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Min(value = 1, message = "возраст должен положительным")
    @Column(name = "age")
    private int age;

    @NotBlank(message = "поле не должно быть пустым")
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank
    @Column
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roleUser = new HashSet<>();


    public User(String firstName, String lastName, int age, String email, String password, Set<Role> roleUser) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.password = password;
        this.roleUser = roleUser;
    }

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName (String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public Set<Role> getRoleUser() {
        return roleUser;
    }

    public void setRoleUser(Set<Role> roleUser) {
        this.roleUser = roleUser;
    }
    public void addRole(Role role){
        this.roleUser.add(role);
    }
    public String getRolesAsString() {
        return roleUser.stream()
                .map(Role::getRole)
                .map(role -> {
                    if (role.contains("ROLE_USER"))
                        return "USER";
                    else if (role.contains("ROLE_ADMIN"))
                        return "ADMIN";
                    else
                        return "No role";
                })
                .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roleUser +
                '}';
    }
}
