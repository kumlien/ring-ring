package se.kumliens.ringring;

import org.springframework.boot.SpringApplication;


public class TestRingRingApplication {

	public static void main(String[] args) {
		SpringApplication.from(RingRingApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
