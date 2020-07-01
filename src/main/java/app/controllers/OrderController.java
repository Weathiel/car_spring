package app.controllers;

import app.entity.Orders;
import app.services.OrdersService;
import app.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;

@RestController
public class OrderController {
    private OrdersService ordersService;
    private UserServiceImpl userService;

    @Autowired
    public OrderController(OrdersService ordersService, UserServiceImpl userService) {
        this.ordersService = ordersService;
        this.userService = userService;
    }

    @PostMapping(value = "/order/create", produces = "application/json")
    public ResponseEntity<?> createOrder(@RequestBody Orders orders, HttpServletRequest httpServletRequest) {
        return ordersService.createOrder(orders, httpServletRequest);
    }

    @GetMapping(value = "/order/getAll", produces = "application/json")
    public ResponseEntity<?> getAllOrders() {
        return ordersService.getAllOrders();
    }

    @DeleteMapping(value = "/order/delete/{id}", produces = "application/json")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        return ordersService.deleteOrder(id);
    }

    @GetMapping(value = "/order/getOrders", produces = "application/json")
    public ResponseEntity<?> getOrdersForUserId(HttpServletRequest httpServletRequest) {
        Long userId = userService.getUserByToken(httpServletRequest).getId();
        return ordersService.getOrdersForUser(userId);
    }

    @GetMapping(value = "/order/archivize/{orderId}", produces = "application/json")
    public ResponseEntity<?> archivizeOrder(HttpServletRequest httpServletRequest, @PathVariable Long orderId) {
        return ordersService.archivizeOrder(orderId);
    }

    @GetMapping(value = "/order/generate/{id}", produces = "application/pdf")
    public ResponseEntity<?> generatePdf(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ByteArrayInputStream byteArrayInputStream = ordersService.generatePdf(httpServletResponse, httpServletRequest, id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "inline; filename=order.pdf");
        return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(byteArrayInputStream));
    }

    @GetMapping(value = "/order/worker/generate/{orderId}&{username}", produces = "application/pdf")
    public ResponseEntity<?> generatePdfForWorker(HttpServletResponse httpServletResponse, @PathVariable Long orderId, @PathVariable String username) {
        ByteArrayInputStream byteArrayInputStream = ordersService.generatePdf(httpServletResponse, orderId, username);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "inline; filename=order.pdf");
        return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(byteArrayInputStream));
    }
}
