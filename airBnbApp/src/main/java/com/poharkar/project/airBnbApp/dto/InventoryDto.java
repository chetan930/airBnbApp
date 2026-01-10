package com.poharkar.project.airBnbApp.dto;

import com.poharkar.project.airBnbApp.entity.Hotel;
import com.poharkar.project.airBnbApp.entity.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {
    private Long id;

    private LocalDate date;

    private Integer bookedCount;

    private Integer reservedCount;

    private Integer totalCount;

    private BigDecimal surgeFactor;

    private BigDecimal price;

    private Boolean closed;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
