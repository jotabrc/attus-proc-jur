package attus.proc.proc_jur.dto;

import attus.proc.proc_jur.enums.PartyType;
import attus.proc.proc_jur.validation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PartyDto {

    @ValidName
    private final String fullName;

    @ValidLegalId
    private final String legalEntityId;

    @ValidPartyType
    private final PartyType type;

    @ValidEmail
    private final String email;

    @ValidPhone
    private final String phone;
}
