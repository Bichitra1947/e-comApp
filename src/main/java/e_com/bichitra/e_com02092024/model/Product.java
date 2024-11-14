package e_com.bichitra.e_com02092024.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank
    @Size(min = 3, message = "Product name must contain at least 3 characters")
    private String productName;
    private String image;
    @NotBlank
    @Size(min = 6, message = "Product description must contain at least 6 characters")
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private  Category category;

    @ManyToOne()
    @JoinColumn(name = "users_id")
    private Users users;

    @OneToMany(mappedBy = "product" ,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CartItem> productList=new ArrayList<>();
}
