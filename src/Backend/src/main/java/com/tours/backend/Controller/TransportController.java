package com.tours.backend.Controller;

import com.tours.backend.Entities.Transport;
import com.tours.backend.Service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/transports")
@CrossOrigin(origins = "*")
public class TransportController {

    @Autowired
    private TransportService transportService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Transport> addTransport(@RequestBody Transport transport) {
        return ResponseEntity.ok(transportService.addTransport(transport));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Transport> getTransportById(@PathVariable Long id) {
        Optional<Transport> transport = Optional.ofNullable(transportService.getTransportById(id));
        return transport.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Transport>> getAllTransports() {
        return ResponseEntity.ok(transportService.getAllTransports());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Transport> updateTransport(@PathVariable Long id, @RequestBody Transport transport) {
        return ResponseEntity.ok(transportService.updateTransport(id, transport));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Transport> deleteTransport(@PathVariable Long id) {
        transportService.deleteTransport(id);
        return ResponseEntity.noContent().build();
    }
}
