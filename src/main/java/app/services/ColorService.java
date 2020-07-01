package app.services;

import app.dao.ColorRepository;
import app.entity.Color;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ColorService {
    private ColorRepository colorRepository;

    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    public ResponseEntity<?> getAllColors() {
        return new ResponseEntity<>(colorRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<?> createColor(Color color) {
        colorRepository.save(color);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteColor(Long id) {
        colorRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
