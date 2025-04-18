package ru.otus.server;

import com.google.gson.Gson;
import java.util.Arrays;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.service.ClientAuthService;
import ru.otus.service.TemplateProcessor;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.LoginServlet;

public class UsersWebServerWithFilterBasedSecurity extends UsersWebServerSimple {
    private final ClientAuthService clientAuthService;

    public UsersWebServerWithFilterBasedSecurity(
            int port,
            ClientAuthService authService,
            DBServiceClient userDao,
            Gson gson,
            TemplateProcessor templateProcessor) {
        super(port, userDao, gson, templateProcessor);
        this.clientAuthService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(
                new ServletHolder(new LoginServlet(templateProcessor, clientAuthService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
