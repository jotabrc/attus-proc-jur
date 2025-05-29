package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.ContactDto;
import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.model.Contact;

public interface ContactService {

    Contact getOrCreateContact(ContactDto dto);
}
