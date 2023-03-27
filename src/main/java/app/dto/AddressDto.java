package app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class AddressDto {

    private Integer idAddress;
    private String city;
    private String street;
    private Integer streetNumber;
    private CountryDto countryDto;

    public static AddressDto of(String city, String street, Integer streetNumber, CountryDto countryDto){
        AddressDto addressDto = new AddressDto();
        addressDto.setCity(city);
        addressDto.setStreet(street);
        addressDto.setStreetNumber(streetNumber);
        addressDto.setCountryDto(countryDto);
        return addressDto;
    }

}
