package fantastic.cms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/register").setViewName("register");
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/main").setViewName("main");
		registry.addViewController("/login").setViewName("login");
	}

}