package app.controllers;

import app.entity.Engine;
import app.services.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EngineController {

    private EngineService engineService;

    @Autowired
    public EngineController(EngineService engineService) {
        this.engineService = engineService;
    }

    @PostMapping(value = "/engines/create", produces = "application/json")
    public ResponseEntity<?> createEngine(@RequestBody Engine engine) {
        return engineService.createEngine(engine);
    }

    @GetMapping(value = "/engines/getAll", produces = "application/json")
    public ResponseEntity<?> getAllEngines() {
        return engineService.getAllEngines();
    }

    @DeleteMapping(value = "/engines/delete/{id}", produces = "application/json")
    public ResponseEntity<?> deleteEngine(@PathVariable Long id) {
        return engineService.deleteEngine(id);
    }
}
