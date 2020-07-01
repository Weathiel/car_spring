package app.services;

import app.dao.EngineRepository;
import app.entity.Engine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EngineService {

    private EngineRepository engineRepository;

    public EngineService(EngineRepository engineRepository) {
        this.engineRepository = engineRepository;
    }

    public ResponseEntity<?> getAllEngines() {
        List<Engine> engines = engineRepository.findAll();
        return new ResponseEntity<>(engines, HttpStatus.OK);
    }

    public ResponseEntity<?> createEngine(Engine engine) {
        engineRepository.save(engine);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteEngine(Long id) {
        engineRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
