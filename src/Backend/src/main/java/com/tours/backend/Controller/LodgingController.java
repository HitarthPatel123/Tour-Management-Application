package com.tours.backend.Controller;

import com.tours.backend.Entities.Lodging;
import com.tours.backend.Service.LodgingService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/lodgings")
@CrossOrigin(origins = "*")
public class LodgingController {

    @Autowired
    private LodgingService lodgingService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Lodging> addLodging(@RequestBody Lodging lodging) {
        return ResponseEntity.ok(lodgingService.addLodging(lodging));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Lodging> getLodgingById(@PathVariable Long id) {
        Optional<Lodging> lodging = Optional.ofNullable(lodgingService.getLodgingById(id));
        return lodging.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Lodging>> getAllLodgings() {return ResponseEntity.ok(lodgingService.getAllLodgings());}

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Lodging> updateLodging(@PathVariable Long id, @RequestBody Lodging lodgingDetails) {
        return ResponseEntity.ok(lodgingService.updateLodging(id, lodgingDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLodging(@PathVariable Long id) {
        lodgingService.deleteLodging(id);
        return ResponseEntity.noContent().build();
    }
}
