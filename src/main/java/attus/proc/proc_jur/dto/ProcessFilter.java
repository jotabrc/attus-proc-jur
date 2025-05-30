package attus.proc.proc_jur.dto;

import attus.proc.proc_jur.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessFilter implements Serializable {

    private Status status;
    private LocalDate openingDate;
    private String legalEntityId;
}
