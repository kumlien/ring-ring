package se.kumliens.ringring;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@Theme(themeClass = Material.class)
@Theme("ringring")
@PWA(name = "Ring-Ring Application", shortName = "RingRing")
public class RingRingApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(RingRingApplication.class, args);
	}

}
