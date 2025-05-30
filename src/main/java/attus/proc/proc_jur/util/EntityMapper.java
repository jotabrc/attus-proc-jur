package attus.proc.proc_jur.util;

import attus.proc.proc_jur.dto.ActionDto;
import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.model.Action;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.model.Process;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class EntityMapper {

    public Process toEntity(final ProcessDto dto) {
        return Process
                .builder()
                .number(dto.getNumber())
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

    public Party toEntity(final PartyDto dto) {
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

    public Action toEntity(final ActionDto dto) {
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
