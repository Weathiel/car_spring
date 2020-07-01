package app.controllers;

import app.entity.Details;
import app.services.DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DetailsController {

    private DetailsService detailsService;

    @Autowired
    public DetailsController(DetailsService detailsService) {
        this.detailsService = detailsService;
    }

    @PostMapping(value = "/details/create", produces = "application/json")
    public ResponseEntity<?> createDetail(@RequestBody Details details) {
        return detailsService.createDetail(details);
    }

    @GetMapping(value = "/details/getAll", produces = "application/json")
    public ResponseEntity<?> getAllDetails() {
        return detailsService.getAllDetails();
    }

    @DeleteMapping(value = "/details/delete/{id}", produces = "application/json")
    public ResponseEntity<?> deleteDetail(@PathVariable Long id) {
        return detailsService.deleteDetail(id);
    }
}
