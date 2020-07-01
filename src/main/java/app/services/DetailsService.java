package app.services;

import app.dao.DetailsRepository;
import app.entity.Details;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DetailsService {

    private DetailsRepository detailsRepository;

    public DetailsService(DetailsRepository detailsRepository) {
        this.detailsRepository = detailsRepository;
    }

    public ResponseEntity<?> getAllDetails() {
        return new ResponseEntity<>(detailsRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<?> createDetail(Details details) {
        detailsRepository.save(details);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteDetail(Long id) {
        detailsRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
