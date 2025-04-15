package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import ru.otus.crm.model.RoleClient;
import ru.otus.service.TemplateProcessor;

@SuppressWarnings({"java:S1989"})
public class CreateClientFormServlet extends HttpServlet {

    private static final String CLIENT_FROM_PAGE_TEMPLATE = "createClientForm.html";
    private static final String TEMPLATE_ROLES = "roles";

    private final TemplateProcessor templateProcessor;

    public CreateClientFormServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put(TEMPLATE_ROLES, RoleClient.values());
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENT_FROM_PAGE_TEMPLATE, model));
    }
}
