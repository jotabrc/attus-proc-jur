package attus.proc.proc_jur.util;

import attus.proc.proc_jur.dto.ActionDto;
import attus.proc.proc_jur.dto.ContactDto;
import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.model.Action;
import attus.proc.proc_jur.model.Contact;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.model.Process;

import java.util.List;

public class Transform {

    public static ProcessDto toDto(final Process process) {
        return new ProcessDto(
                process.getNumber(),
                process.getOpeningDate().toLocalDateTime(),
                process.getStatus(),
                process.getDescription(),
                mapParties(process.getParties()),
                mapActions(process.getActions())
        );
    }

    private static List<PartyDto> mapParties(final List<Party> parties) {
        return parties
                .stream()
                .map(Transform::toDto)
                .toList();
    }

    private static List<ActionDto> mapActions(final List<Action> actions) {
        return actions
                .stream()
                .map(Transform::toDto)
                .toList();
    }

    private static PartyDto toDto(final Party party) {
        return new PartyDto(
                party.getFullName(),
                party.getLegalEntityId(),
                party.getType(),
                toDto(party.getContact())
        );
    }

    private static ContactDto toDto(final Contact contact) {
        return new ContactDto(
                contact.getEmail(),
                contact.getPhone()
        );
    }

    private static ActionDto toDto(final Action action) {
        return new ActionDto(
                null,
                action.getType(),
                action.getDescription()
        );
    }
}
