package dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Size(min=6,max=30,message = "el nombre de usuario debe contener al menos 6 caracteres y un máximo de 30")
    private String username;//Nombre de usuario

    @Size(min=6,max=30,message = "el email debe contener al menos 6 caracteres y un máximo de 30")
    @Email
    private String email;//email del usuario

    @Size(min=6,max=30,message = "La contraseña debe tener al menos 6 carateres y máximo 30")
    private String password;

}
