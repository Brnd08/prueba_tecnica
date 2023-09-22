package prueba.tecnica.prueba_tecnica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.faces.webapp.FacesServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import java.util.List;


@SpringBootApplication
public class PruebaTecnicaApplication {

    public static void main(final String[] args) {
        SpringApplication.run(PruebaTecnicaApplication.class, args);
    }


    @Bean
    ServletRegistrationBean<Servlet> jsfServletRegistration (ServletContext servletContext) {
        //spring boot only works if this is set
        servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());

        //registration
        ServletRegistrationBean<Servlet> srb = new ServletRegistrationBean<>();
        srb.setServlet(new FacesServlet());
        List<String> urlMappings = new java.util.ArrayList<>();
        urlMappings.add("*.xhtml");
        srb.setUrlMappings(urlMappings);
        srb.setLoadOnStartup(1);
        return srb;
    }

}
