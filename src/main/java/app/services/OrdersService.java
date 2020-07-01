package app.services;

import app.dao.OrdersRepository;
import app.dao.UserRepository;
import app.entity.Orders;
import app.entity.User;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class OrdersService {
    private OrdersRepository ordersRepository;
    private UserServiceImpl userService;
    private UserRepository userRepository;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository, UserServiceImpl userService, UserRepository userRepository) {
        this.ordersRepository = ordersRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getAllOrders() {
        return new ResponseEntity<>(ordersRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<?> createOrder(Orders orders, HttpServletRequest httpServletRequest) {
        orders.setArchivized(false);
        orders.setUser(userService.getUserByToken(httpServletRequest));
        ordersRepository.save(orders);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> archivizeOrder(Long orderId) {
        Orders orders = ordersRepository.findById(orderId).orElse(null);
        if (orders != null) {
            orders.setArchivized(true);
            ordersRepository.save(orders);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }

    public ResponseEntity<?> getOrdersForUser(Long userId) {
        List<Orders> orders = ordersRepository.findAllByUser(userRepository.getOne(userId));
        for (Orders order :
                orders) {
            if (order.getArchivized()) {
                orders.remove(order);
            }
        }
        return ResponseEntity.ok(orders);
    }

    public ResponseEntity<?> deleteOrder(Long id) {
        ordersRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ByteArrayInputStream generatePdf(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, Long orderId) {
        User user = userService.getUserByToken(httpServletRequest);
        List<Orders> orders = ordersRepository.findAllByUser(userRepository.getOne(user.getId()));
        for (Orders temp :
                orders) {
            if (temp.getId() == orderId) {
                return generatesPdf(temp, user, httpServletResponse);
            }

        }
        return null;
    }

    public ByteArrayInputStream generatePdf(HttpServletResponse httpServletResponse, Long orderId, String username) {
        UserDetails user = userService.loadUserByUsername(username);
        Orders orders = ordersRepository.findById(orderId).orElse(null);
        if (user != null && orders != null) {
            return generatesPdf(orders, user, httpServletResponse);
        }
        return null;
    }

    private ByteArrayInputStream generatesPdf(Orders orders, UserDetails user, HttpServletResponse response) {
        Document pdf = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(pdf, out);
            pdf.open();
            pdf.add(new Paragraph("Order - summary"));
            pdf.add(new Paragraph(Chunk.NEWLINE));
            PdfPTable table = new PdfPTable(2);
            table.addCell("Username");
            table.addCell(user.getUsername());
            table.addCell("Active");
            table.addCell(user.isEnabled() + "");

            pdf.add(table);

            pdf.add(new Paragraph(Chunk.NEWLINE));
            pdf.add(new Paragraph(Chunk.NEWLINE));

            PdfPTable table1 = new PdfPTable(3);
            table1.addCell("Order number");
            table1.addCell(orders.getId() + "");
            table1.addCell("");

            table1.addCell("Engine");
            table1.addCell(orders.getEngine().getEngine() + " " + orders.getEngine().getCapacity() + " " + orders.getEngine().getPower() + "km");
            table1.addCell(orders.getEngine().getPrice() + "");

            table1.addCell("Tires");
            table1.addCell(orders.getTire().getProducent() + " " + orders.getTire().getSize() + "\"");
            table1.addCell(orders.getTire().getPrice() + "");

            table1.addCell("Color");
            table1.addCell(orders.getColor().getColor());
            table1.addCell(orders.getColor().getPrice() + "");

            table1.addCell("Detail");
            table1.addCell(orders.getDetails().getDetail());
            table1.addCell(orders.getDetails().getPrice() + "");

            pdf.add(table1);

            pdf.add(new Paragraph(Chunk.NEWLINE));
            Paragraph s = new Paragraph("Price - " + orders.getPrice());
            s.setAlignment(1);
            pdf.add(s);

            pdf.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
