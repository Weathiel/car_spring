package app.controllers;

import app.entity.Color;
import app.services.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ColorController {

    private ColorService colorService;

    @Autowired
    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @PostMapping(value = "/colors/create", produces = "application/json")
    public ResponseEntity<?> createColor(@RequestBody Color color) {
        return colorService.createColor(color);
    }

    @GetMapping(value = "/colors/getAll", produces = "application/json")
    public ResponseEntity<?> getAllColors() {
        return colorService.getAllColors();
    }

    @DeleteMapping(value = "/colors/delete/{id}", produces = "application/json")
    public ResponseEntity<?> deleteColor(@PathVariable Long id) {
        return colorService.deleteColor(id);
    }
}
