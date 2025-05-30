package attus.proc.proc_jur.util;

import attus.proc.proc_jur.dto.ActionDto;
import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.dto.ProcessAbs;
import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.model.Action;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.model.Process;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Validated
@Component
public class EntityMapper {

    public Process toEntity(@NotNull final ProcessAbs dto) {
        String getNumber = null;
        if (dto instanceof ProcessDto p) getNumber = p.getNumber();
        return Process
                .builder()
                .number(Optional
                        .ofNullable(getNumber)
                        .orElse("")
                )
                .openingDate(Optional
                        .ofNullable(dto.getOpeningDate())
                        .orElse(LocalDateTime.now())
                        .atZone(ZoneId.of("America/Sao_Paulo")))
                .status(dto.getStatus())
                .description(dto.getDescription())
                .parties(
                        Optional.ofNullable(dto.getParties())
                                .orElse(List.of())
                                .stream()
                                .map(this::toEntity)
                                .toList()
                )
                .actions(
                        Optional.ofNullable(dto.getActions())
                                .orElse(List.of())
                                .stream()
                                .map(this::toEntity)
                                .toList()
                )
                .build();
    }

    public Party toEntity(@NotNull final PartyDto dto) {
        return Party
                .builder()
                .fullName(Optional
                        .ofNullable(dto.getFullName())
                        .orElse(""))
                .legalEntityId(Optional
                        .ofNullable(dto.getLegalEntityId())
                        .orElse(""))
                .type(Optional
                        .of(dto.getType())
                        .orElse(null))
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
    }

    public Action toEntity(@NotNull final ActionDto dto) {
        return Action
                .builder()
                .type(dto.getType())
                .registrationDate(
                        ZonedDateTime
                                .now()
                                .withZoneSameLocal(ZoneId.of("America/Sao_Paulo")))
                .description(dto.getDescription())
                .build();
    }
}
