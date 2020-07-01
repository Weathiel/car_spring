package app.services;

import app.dao.TireRepository;
import app.entity.Tire;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TireService {
    private TireRepository tireRepository;

    public TireService(TireRepository tireRepository) {
        this.tireRepository = tireRepository;
    }

    public ResponseEntity<?> getAllTires() {
        return new ResponseEntity<>(tireRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<?> createTire(Tire tire) {
        tireRepository.save(tire);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteTire(Long id) {
        tireRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
