package app.dto;

import lombok.Data;

@Data
public class QuantityTypeDto {

    private Integer idQuantityType;
    private String name;

    public static QuantityTypeDto of(Integer id, String name){
        QuantityTypeDto dto = new QuantityTypeDto();
        dto.idQuantityType = id;
        dto.name = name;

        return dto;
    }

    @Override
    public String toString(){
        return name;
    }

}
