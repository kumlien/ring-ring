package se.kumliens.ringring;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Disabled("måste lägga in env-vars")
class RingRingApplicationTests {

	@Test
	void contextLoads() {
	}

}
