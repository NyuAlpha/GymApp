package dto;


import com.victor.project.gymapp.models.GymSet;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetDto {


    private Long id;

    private Integer setOrder;//Indica el orden que ocupa en la lista de las series, jamás puede ser nulo

    private Float weight;//El peso a levantar, puede ser nulo en caso

    private Integer repetitions;//Repeticiones de la serie, es nulo cuando se desconoce o se llega al fallo

    private Boolean failure;


    public static SetDto getsimpleDto(GymSet set) {
        SetDto setDto = new SetDto();
        setDto.setId(set.getId());
        setDto.setSetOrder(set.getSetOrder());
        setDto.setWeight(set.getWeight());
        setDto.setRepetitions(set.getRepetitions());
        setDto.setFailure(set.getFailure());
        
        return setDto;
    }
}
