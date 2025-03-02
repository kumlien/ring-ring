package se.kumliens.ringring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

@SpringBootApplication
@Theme(themeClass = Material.class)
public class RingRingApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(RingRingApplication.class, args);
	}

}
