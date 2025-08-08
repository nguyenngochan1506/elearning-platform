package dev.edu.ngochandev.authservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.*;

@Entity
@Table(name = "tbl_invalidated_tokens")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvalidatedTokenEntity {
    @Id
    private String id;

    private Date expiredTime;
}
