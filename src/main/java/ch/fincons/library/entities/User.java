package ch.fincons.library.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @ApiModelProperty(notes = "Il Codice Interno Univoco dell'Utente")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, max = 50, message = "{Size.User.email.Validation}")
    @NotNull(message = "{NotNull.User.email.Validation}")
    private String email;
    private String username;
    private String roles;
    private String password;
}
