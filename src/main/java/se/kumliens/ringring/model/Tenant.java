package se.kumliens.ringring.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Container(containerName = "tenants") // Specify the Cosmos DB container name
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates an all-arguments constructor
@Slf4j
public class Tenant implements Persistable<String> {

    @Id
    private String id; // Unique identifier for the tenant

    @PartitionKey
    private String domain; // Partition key for the document

    private String name;
    private String elkId;
    private String elkSecret;
    private SubscriptionPlan subscriptionPlan;
    private List<User> users = new ArrayList<>(); // Embedded list of users
    private List<Office> offices = new ArrayList<>(); // Embedded list of offices
    private List<VirtualNumber> virtualNumbers = new ArrayList<>(); // Embedded list of virtual numbers

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime modified;

    public void addUser(User user) {
        if(users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    @Override
    public boolean isNew() {
        return created == null;
    }

    public void addOffice(Office office) {
        offices.stream().filter(o -> o.name.equalsIgnoreCase(office.name))
                .findFirst()
                .ifPresentOrElse(
                        o -> log.info("Won't add office with name {} since it allready exist", office.name),
                        () -> offices.add(office));
    }
}