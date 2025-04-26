package fantastic.cms.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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

    @ManyToOne
    @JoinColumn(name = "author_id")
    public User author;
}
