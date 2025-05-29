package attus.proc.proc_jur.repository;

import attus.proc.proc_jur.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Optional<Contact> findByEmailOrPhone(String email, String phone);
}
