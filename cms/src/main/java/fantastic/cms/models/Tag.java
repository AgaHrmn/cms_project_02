package fantastic.cms.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@Data
@Entity
@Table(name = "tag")
public class Tag implements Serializable {
    @Id
    @UuidGenerator
    String id;

    String value;
}
