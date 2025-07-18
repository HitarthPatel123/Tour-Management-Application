package com.tours.backend.Service;

import com.tours.backend.Entities.Transport;
import com.tours.backend.Repository.TransportRepo;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class TransportService {

    private static final Logger logger = Logger.getLogger(TransportService.class.getName());

    private TransportRepo transportRepository;

    @CachePut(value = "transports", key = "#transport.id")
    public Transport addTransport(Transport transport) {
        logger.info("Adding Transport with details: " + transport);
        Transport savedTransport = transportRepository.save(transport);
        logger.info("Transport saved: " + savedTransport.getId());
        return savedTransport;
    }

    public Transport getTransportById(Long id) {
        logger.info("Getting Transport with id: " + id);
        Transport transport = transportRepository.findById(id)
                .orElseThrow(() -> new TransportNotFoundException("Transport not found with id " + id));
        logger.info("Transport fetched successfully: " + transport);
        return transport;
    }
}
