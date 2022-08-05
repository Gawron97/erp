package app.dto;

import lombok.Data;

@Data
public class AddressDto {

    private Integer idAddress;
    private String city;
    private String street;
    private Integer streetNumber;
    private CountryDto countryDto;

}
