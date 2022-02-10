package ch.fincons.library.entities;

import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    private Long id;
    @Column(unique = true)
    private String email;
    private String name;
    private String roles;
    private String password;
}
