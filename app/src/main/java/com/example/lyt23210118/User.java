package com.example.lyt23210118;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

public class User {
    public String name;
    public String gender;
    public CopyOnWriteArraySet<String> hobbies;

    public User(String name, String gender, CopyOnWriteArraySet<String> hobbies) {
        this.name = name;
        this.gender = gender;
        this.hobbies = hobbies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(gender, user.gender) &&
                Objects.equals(hobbies, user.hobbies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gender, hobbies);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", hobbies=" + hobbies +
                '}';
    }
}
