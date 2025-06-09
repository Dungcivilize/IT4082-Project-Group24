package KTPM.Backend.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resident")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resident {
    public enum Gender {
        male, female, other
    }

    public enum ResidentType {
        owner, member
    }

    public enum ResidentStatus {
        living, moved_out, temporary_absent
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resident_id")
    private Integer residentId;

    @ManyToOne
    @JoinColumn(name = "ownership_id", nullable = false)
    private ApartmentOwnership ownership;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "identity_card", length = 20, unique = true)
    private String identityCard;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "occupation", length = 100)
    private String occupation;

    @Enumerated(EnumType.STRING)
    @Column(name = "resident_type", nullable = false)
    private ResidentType residentType;

    @Column(name = "relationship", length = 50)
    private String relationship;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ResidentStatus status = ResidentStatus.living;
} 