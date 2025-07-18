package com.tours.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lodgings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lodging {

    private Long id;

    private String lodgingName;
    private String lodgingType;
    private String lodgingDescription;
    private String address;
    private Double rating;
}
