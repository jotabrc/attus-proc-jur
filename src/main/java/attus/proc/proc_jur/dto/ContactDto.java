package attus.proc.proc_jur.dto;

import attus.proc.proc_jur.validation.ValidEmail;
import attus.proc.proc_jur.validation.ValidPhone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ContactDto {

    @ValidEmail
    private final String email;

    @ValidPhone
    private final String phone;
}
