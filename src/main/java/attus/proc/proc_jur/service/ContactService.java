package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.ContactDto;
import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.model.Contact;

public sealed interface ContactService permits ContactServiceImpl {

    Contact getOrCreateContact(ContactDto dto);
}
