package fantastic.cms.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Data
@Entity
@Table(name = "review")
@NoArgsConstructor
@AllArgsConstructor
public class Review  implements Serializable {
    @Id
    @UuidGenerator
    String userId;

    @Enumerated(EnumType.STRING)
    Status status;
}
