package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.RoleClient;
import ru.otus.crm.service.DBServiceClient;

@SuppressWarnings({"java:S1989"})
public class CreateClientServlet extends HttpServlet {

    private final DBServiceClient dbServiceClient;

    public CreateClientServlet(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String name = req.getParameter("name");
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String roleParam = req.getParameter("roleClient");

        if (name == null || login == null || password == null || roleParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "All fields are required.");
            return;
        }

        RoleClient role = RoleClient.valueOf(roleParam);
        Client client = new Client(name, login, password, role);
        dbServiceClient.saveClient(client);

        resp.sendRedirect("clients");
    }
}
