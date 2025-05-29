package attus.proc.proc_jur.dto;

import attus.proc.proc_jur.enums.PartyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PartyDto {

    private final String fullName;
    private final String legalEntityId;
    private final PartyType type;
    private final ContactDto contact;
}
