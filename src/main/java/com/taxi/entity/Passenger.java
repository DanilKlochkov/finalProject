package com.taxi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("passenger")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Passenger extends User {
    private String phone;
    private String city;

    public Passenger(String userName, String pass, String firstName, String lastName) {
        super.setUserName(userName);
        super.setUserPassword(pass);
        super.setUserFirstName(firstName);
        super.setUserLastName(lastName);
    }
}
