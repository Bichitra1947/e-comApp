package e_com.bichitra.e_com02092024.apiResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class APICategoryResponse {
    private String message;
    private String status;
}
