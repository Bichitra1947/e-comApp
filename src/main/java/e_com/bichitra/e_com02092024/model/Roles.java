package e_com.bichitra.e_com02092024.model;

import e_com.bichitra.e_com02092024.constant.AppRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "role_name")
    private AppRole roleName;

    public Roles(AppRole roleName) {
        this.roleName = roleName;
    }
}
