package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.model.Contact;
import attus.proc.proc_jur.repository.ContactRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Contact getOrCreateContact(PartyDto dto) {
        return contactRepository.findByEmailOrPhone(
                        dto.getContact().getEmail(),
                        dto.getContact().getPhone()
                )
                .orElseGet(() ->
                        Contact
                                .builder()
                                .email(dto.getContact().getEmail())
                                .phone(dto.getContact().getPhone())
                                .build()
                );
    }
}
