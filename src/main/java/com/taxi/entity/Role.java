package com.taxi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "role", schema = "public")
public class Role {
    @Id
    private String roleName;
    private String roleDescription;
}
