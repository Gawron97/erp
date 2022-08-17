package app.dto;

import lombok.Data;

@Data
public class OperatorRegisterCredentialsDto {

    private String pesel;
    private String login;
    private String password;
    private Boolean authenticated = false;

    public static OperatorRegisterCredentialsDto of(String pesel, String login, String password){
        OperatorRegisterCredentialsDto dto = new OperatorRegisterCredentialsDto();
        dto.pesel = pesel;
        dto.login = login;
        dto.password = password;

        return dto;
    }

}
