package se.kumliens.ringring.api;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import se.kumliens.ringring.model.Tenant;
import se.kumliens.ringring.service.TenantService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant) {
        var createdTenant = tenantService.upsert(tenant);
        // Build the full URI for the Location header
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest() // Get the current request URI (e.g., /api/tenants)
                .path("/{id}") // Append the tenant ID to the URI
                .buildAndExpand(createdTenant.id()) // Replace {id} with the actual tenant ID
                .toUri(); // Convert to URI

        return ResponseEntity.created(location).body(createdTenant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tenant> getTenantById(@PathVariable String id) {
        var tenant = tenantService.getTenantById(id);
        return tenant.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/domain/{domain}")
    public ResponseEntity<Tenant> getTenantByDomain(@PathVariable String domain) {
        var tenant = tenantService.getTenantByDomain(domain);
        return tenant.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable String id) {
        tenantService.deleteTenant(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Tenant>> findAll() {
        var tenants = tenantService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }
}
