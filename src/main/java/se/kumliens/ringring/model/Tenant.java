package se.kumliens.ringring.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Container(containerName = "tenants") // Specify the Cosmos DB container name
public record Tenant(
        @Id String id, // Unique identifier for the tenant
        @PartitionKey String domain, // Partition key for the document
        String name,
        String subscriptionPlan,
        LocalDateTime createdAt,
        List<User> users, // Embedded list of users
        List<Office> offices, // Embedded list of offices
        List<VirtualNumber> virtualNumbers // Embedded list of virtual numbers
) {}
