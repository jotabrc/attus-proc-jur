package attus.proc.proc_jur.util;

import attus.proc.proc_jur.dto.ActionDto;
import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.model.Action;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.model.Process;

import java.util.List;

public class DtoMapper {

    private DtoMapper() {}

    public static ProcessDto toDto(final Process process) {
        return new ProcessDto(
                process.getOpeningDate().toLocalDateTime(),
                process.getStatus(),
                process.getDescription(),
                mapParties(process.getParties()),
                mapActions(process.getActions()),
                process.getNumber()
        );
    }

    private static List<PartyDto> mapParties(final List<Party> parties) {
        return parties
                .stream()
                .map(DtoMapper::toDto)
                .toList();
    }

    private static List<ActionDto> mapActions(final List<Action> actions) {
        return actions
                .stream()
                .map(DtoMapper::toDto)
                .toList();
    }

    private static PartyDto toDto(final Party party) {
        return new PartyDto(
                party.getFullName(),
                party.getLegalEntityId(),
                party.getType(),
                party.getEmail(),
                party.getPhone()
        );
    }

    private static ActionDto toDto(final Action action) {
        return new ActionDto(
                action.getType(),
                action.getDescription()
        );
    }
}
