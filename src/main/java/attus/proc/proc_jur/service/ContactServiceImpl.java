package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.ContactDto;
import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.model.Contact;
import attus.proc.proc_jur.repository.ContactRepository;
import attus.proc.proc_jur.util.EntityMapper;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    private final EntityMapper entityMapper;
    private final ContactRepository contactRepository;

    public ContactServiceImpl(EntityMapper entityMapper, ContactRepository contactRepository) {
        this.entityMapper = entityMapper;
        this.contactRepository = contactRepository;
    }

    @Override
    public Contact getOrCreateContact(final ContactDto dto) {
        return contactRepository.findByEmailOrPhone(
                        dto.getEmail(),
                        dto.getPhone()
                )
                .orElseGet(() ->
                        entityMapper.toEntity(dto)
                );
    }
}
