package com.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("driver")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Driver extends User {
    @Column(name = "phone")
    private String phone;

    @Column(name = "city")
    private String city;

    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "rating")
    private Double rating = 0D;

    @JsonIgnore
    @Column(name = "star_count")
    private Integer starCount = 0;

    public Driver(String userName, String pass, String firstName, String lastName) {
        super.setUserName(userName);
        super.setUserPassword(pass);
        super.setUserFirstName(firstName);
        super.setUserLastName(lastName);
    }
}
