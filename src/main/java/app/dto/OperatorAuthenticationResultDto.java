package app.dto;

import lombok.Data;

@Data
public class OperatorAuthenticationResultDto {

    private Integer idOperator;
    private String name;
    private String surname;
    private boolean authenticated;

}
