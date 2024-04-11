package fantastic.cms.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@Data
@Entity
@Table(name = "category")
public class Category  implements Serializable {
    @Id
    @UuidGenerator
    String id;
    String name;
}
