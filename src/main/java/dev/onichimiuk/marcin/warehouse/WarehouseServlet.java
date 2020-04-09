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
import java.util.stream.Collectors;

@WebServlet(name = "Warehouse", urlPatterns = {"/api/warehouses/*"})
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

    public WarehouseServlet(WarehouseService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // Jako zmienne x, y - przyjąłem współrzędne geograficzne miast Polski ze stopniami i minutami bez znaczników
    // np. Bydgoszcz  18°00'E  53°07'N  ma współrzędne x = 1800, y = 5307.
    // Współrzędne zamawiającego i wybór paramterów obsłużyłem tymczasowo w GUI pod localhostem:8777
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var requestParameterMap = req.getParameterMap();
        logger.info("WarehouseServlet GET request with parameters: " + requestParameterMap);

        var productsMap = requestParameterMap.entrySet()
                .stream()
                .filter(f -> !f.getKey().equals("x") & !f.getKey().equals("y") & Integer.parseInt(f.getValue()[0])!=0)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> Integer.parseInt(e.getValue()[0])));

        var x = Integer.valueOf(req.getParameter("x"));
        var y = Integer.valueOf(req.getParameter("y"));

        try {
            resp.setContentType("application/json;charset=UTF-8");
            var response = service.findNearestConfiguration(x, y, productsMap);
            mapper.writeValue(resp.getOutputStream(), response);
            logger.info("WarehouseServlet GET response:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
        } catch (Exception e) {
            resp.setContentType("text/html;charset=UTF-8");
            mapper.writeValue(resp.getOutputStream(), e.getMessage());
        }

    }
}