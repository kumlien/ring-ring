package se.kumliens.ringring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.kumliens.ringring.model.Tenant;
import se.kumliens.ringring.repo.TenantRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantService {
    private final TenantRepo tenantRepository;

    public Tenant upsert(Tenant tenant) {
        if(tenant.getId() != null) {
            log.info("Updating tenant with id {}: {}", tenant.getId(), tenant);
        } else {
            tenant.setId(UUID.randomUUID().toString());
            log.info("Creating new tenant: {}", tenant);
        }
        return tenantRepository.save(tenant);
    }

    public Optional<Tenant> getTenantById(String id) {
        return tenantRepository.findById(id);
    }

    public Optional<Tenant> getTenantByDomain(String domain) {
        return tenantRepository.findByDomain(domain);
    }

    public void deleteTenant(String id) {
        tenantRepository.deleteById(id);
    }

    public List<Tenant> getAllTenants() {
        var iter = tenantRepository.findAll();
        return StreamSupport.stream(iter.spliterator(), false)
                .toList();
    }
}
