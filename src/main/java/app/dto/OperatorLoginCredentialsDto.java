package app.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OperatorLoginCredentialsDto {

    private String login;
    private String password;
    private Boolean authenticated = false;


}
