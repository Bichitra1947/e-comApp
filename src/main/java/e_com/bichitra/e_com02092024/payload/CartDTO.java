package e_com.bichitra.e_com02092024.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long cartId;
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
    private Double totalPrice = 0.0;
    private List<ProductDto> products = new ArrayList<>();

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = Math.round(totalPrice * 100.0) / 100.0;
    }

}
