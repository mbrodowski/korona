package dev.onichimiuk.marcin.warehouse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name="Warehouse", urlPatterns = {"/api/warehouses/*"})
public class WarehouseServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(HttpServlet.class);

    private WarehouseService service;
    private ObjectMapper mapper;

    /**
     * Servlet container needs it.
     */
    @SuppressWarnings("unused")
    public WarehouseServlet() {
        this(new WarehouseService(), new ObjectMapper());
    }

    WarehouseServlet(WarehouseService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // Jako zmienne x, y - przyjąłem zaokrąglone współrzędne geograficzne miast Polski
    // np. Bydgoszcz  18°00'E  53°07'N  ma współrzędne x = 18, y = 53.
    // Współrzędne zamawiającego i wybór paramterów obsłużyłem tymczasowo w GUI pod localhostem:8777
    // należy zmienić URL w hibernate.cfg.xml na swój adres do pliku db.mv.db <property name="connection.url">jdbc:h2:file:C:\marcin.onichimiuk\MavenDEV\korona\db</property>
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("WarehouseServlet GET request with parameters: " + req.getParameterMap());
        resp.setContentType("application/json;charset=UTF-8");

        List<Boolean> userChoices = new ArrayList<>(); // narazie zamokowana baza z 3 produktami
        userChoices.add(Boolean.valueOf(req.getParameter("rice")));
        userChoices.add(Boolean.valueOf(req.getParameter("pasta")));
        userChoices.add(Boolean.valueOf(req.getParameter("water")));
//        userChoices.add(Boolean.valueOf(req.getParameter("kolejnyProdukt")));

        var x = Integer.valueOf(req.getParameter("x"));
        var y = Integer.valueOf(req.getParameter("y"));
        var b = userChoices.toArray(Boolean[]::new);

        var response = service.findNearestConfiguration(x, y, b);
        mapper.writeValue(resp.getOutputStream(), response);
        logger.info("WarehouseServlet GET response:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }
}