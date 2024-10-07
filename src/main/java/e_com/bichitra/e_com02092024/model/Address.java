package e_com.bichitra.e_com02092024.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be at-least 5 characters")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must be at-least 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 4, message = "City name must be at-least 4 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name must be at-least 2 characters")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must be at-least 2 characters")
    private String country;

    @NotBlank
    @Size(min = 6, message = "Pin code must be at-least 6 characters")
    private String pincode;

    @ManyToMany(mappedBy = "addresses",cascade = CascadeType.ALL,
                fetch = FetchType.EAGER)
    private List<Users> users=new ArrayList<>();
}
