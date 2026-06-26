package com.jashawn.ap_tracker.vendor;

import com.jashawn.ap_tracker.exception.BadRequestException;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "vendors")
public class Vendor {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

//  A vendor may not always have an email to provide
    @Nullable
    private String email;

//   A vendor may not always have a phone number to provide
    @Nullable
    private String phoneNumber;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static Vendor create(
            String name,
            String email,
            String phoneNumber
    ) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Vendor must have a name.");
        }

        String validatedEmail = email != null && !email.isBlank() ? email.trim() : null;
        String validatedPhoneNumber = phoneNumber != null && !phoneNumber.isBlank() ? phoneNumber.trim() : null;

        return Vendor.builder()
                .name(name)
                .email(validatedEmail)
                .phoneNumber(validatedPhoneNumber)
                .build();
    }

    public void updateName(String name) {
        if (!this.active) {
            throw new BadRequestException("Cannot update a deactivated vendor.");
        }

        this.name = name;
    }


    public void updateEmail(String email) {
        if (!this.active) {
            throw new BadRequestException("Cannot update a deactivated vendor.");
        }

//        OWASP validation regex
        if (email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new BadRequestException("Invalid email.");
        }

        this.email = email;
    }

    public void updatePhoneNumber(String phoneNumber) {
    /*
        The phone number regex matches the formats:
            - optional international prefix
            - optional numbers with parenthesis
            - optional whitespace, dots, or hyphens
     */
        if (!phoneNumber.matches("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$")) {
            throw new BadRequestException("Please enter a valid phone number.");
        }

        this.phoneNumber = phoneNumber;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    @PrePersist
    private void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    @PreUpdate
    private void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
