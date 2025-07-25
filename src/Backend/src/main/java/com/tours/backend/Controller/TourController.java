package com.tours.backend.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tours.backend.Entities.Tour;
import com.tours.backend.Repository.LocationRepo;
import com.tours.backend.Repository.LodgingRepo;
import com.tours.backend.Repository.TransportRepo;
import com.tours.backend.Service.CloudinaryImageService;
import com.tours.backend.Service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/tours")
@CrossOrigin(origins = "*")
public class TourController {

    @Autowired
    private TourService tourService;

    @Autowired
    private LocationRepo locationRepository;

    @Autowired
    private LodgingRepo lodgingRepository;

    @Autowired
    private TransportRepo transportRepository;

    @Autowired
    private CloudinaryImageService cloudinaryImageService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tour> addTourWithImages(@RequestParam("tour") String tourJson,
                                                  @RequestParam("image1") MultipartFile image1,
                                                  @RequestParam("image2") MultipartFile image2) throws JsonProcessingException {
        Tour tour = new ObjectMapper().readValue(tourJson, Tour.class);

        String image1Url = cloudinaryImageService.uploadImage(image1);
        String image2Url = cloudinaryImageService.uploadImage(image2);

        tour.setTourImages(List.of(image1Url, image2Url));

        Long locationId = locationRepository.findTopByOrderByIdDesc().getId();
        Long lodgingId = lodgingRepository.findTopByOrderByIdDesc().getId();
        Long transportId = transportRepository.findTopByOrderByIdDesc().getId();

        Tour savedTour = tourService.saveTour(tour, locationId, lodgingId, transportId);
        return ResponseEntity.ok(savedTour);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Tour>> getAllTours() {
        List<Tour> tours = tourService.getAllToursWithDetails();
        return ResponseEntity.ok(tours);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tour> getTourById(@PathVariable Long tourId) {
        return tourService.getTourById(tourId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tour> updateTour(@PathVariable Long id,
                                           @RequestParam("tour") String updatedTourJson,
                                           @RequestParam(value = "image1", required = false) MultipartFile image1,
                                           @RequestParam(value = "image2", required = false) MultipartFile image2) {
        try {
            Tour updatedTour = new ObjectMapper().readValue(updatedTourJson, Tour.class);
            List<String> currentImages = updatedTour.getTourImages();

            if (image1 != null && !currentImages.isEmpty()) {
                try {
                    String newImage1 = cloudinaryImageService.updateImage(currentImages.get(0), image1);
                    currentImages.set(0, newImage1);
                } catch (RuntimeException e) {
                }
            }

            if (image2 != null && currentImages.size() > 1) {
                try {
                    String newImage2 = cloudinaryImageService.updateImage(currentImages.get(1), image2);
                    currentImages.set(1, newImage2);
                } catch (RuntimeException e) {
                }
            }

            Tour tour = tourService.updateTourWithAssociations(id, updatedTour);
            return ResponseEntity.ok(tour);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) {
        try {
            // Retrieve the tour to get image URLs before deletion
            Tour tour = tourService.getTourById(id).orElseThrow(() -> new RuntimeException("Tour not found"));

            // Delete associated images
            for (String imageUrl : tour.getTourImages()) {
                cloudinaryImageService.deleteImage(imageUrl);
            }

            // Delete the tour
            tourService.deleteTour(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
