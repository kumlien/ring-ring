package se.kumliens.ringring.repo;

//import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;
import se.kumliens.ringring.model.Tenant;

import java.util.Optional;

//@Repository
public interface TenantRepo /*extends CosmosRepository<Tenant, String> */{

    Optional<Tenant> findByDomain(String domain);
}
