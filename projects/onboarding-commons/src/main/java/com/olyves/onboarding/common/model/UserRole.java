package com.olyves.onboarding.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olyves.onboarding.common.model.enums.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_role_id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role name;

    @Column(length = 100)
    private String description;

    // Good video to understand https://www.youtube.com/watch?v=f5bdUjEIbrg&t=1s
    // https://www.baeldung.com/jpa-one-to-one
    @JsonIgnore
    @OneToMany(mappedBy = "userRole")
    @ToString.Exclude
    private Set<User> users;


}
