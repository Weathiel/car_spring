package app.controllers;

import app.entity.Tire;
import app.services.TireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TireController {

    private TireService tireService;

    @Autowired
    public TireController(TireService tireService) {
        this.tireService = tireService;
    }

    @PostMapping(value = "/tires/create", produces = "application/json")
    public ResponseEntity<?> createTire(@RequestBody Tire tire) {
        return tireService.createTire(tire);
    }

    @GetMapping(value = "/tires/getAll", produces = "application/json")
    public ResponseEntity<?> getAllTires() {
        return tireService.getAllTires();
    }

    @DeleteMapping(value = "/tires/delete/{id}", produces = "application/json")
    public ResponseEntity<?> deleteTire(@PathVariable Long id) {
        return tireService.deleteTire(id);
    }
}
