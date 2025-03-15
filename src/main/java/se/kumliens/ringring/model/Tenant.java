package se.kumliens.ringring.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@Container(containerName = "tenants") // Specify the Cosmos DB container name
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates an all-arguments constructor
@Builder // Generates a builder pattern for the class
@Accessors(fluent = true) // Generates record-style getters and setters
public class Tenant {

    @Id
    private String id; // Unique identifier for the tenant

    @PartitionKey
    private String domain; // Partition key for the document

    private String name;
    private String elkId;
    private String elkSecret;
    private SubscriptionPlan subscriptionPlan;
    private List<User> users; // Embedded list of users
    private List<Office> offices; // Embedded list of offices
    private List<VirtualNumber> virtualNumbers; // Embedded list of virtual numbers

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    @CreatedDate
    private OffsetDateTime created;

    @LastModifiedDate
    private OffsetDateTime modified;

    // Static factory method for creation
    public static Tenant create(String domain, String name, String elkId, String elkSecret,
                                SubscriptionPlan subscriptionPlan, List<User> users,
                                List<Office> offices, List<VirtualNumber> virtualNumbers) {
        return Tenant.builder()
                .domain(domain)
                .name(name)
                .elkId(elkId)
                .elkSecret(elkSecret)
                .subscriptionPlan(subscriptionPlan)
                .users(users)
                .offices(offices)
                .virtualNumbers(virtualNumbers)
                .build();
    }
}