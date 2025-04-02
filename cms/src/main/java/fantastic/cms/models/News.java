package fantastic.cms.models;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "news", schema = "my_schema")
public class News {
    @Id
    @UuidGenerator
    String id;
    String title;
    String content;


    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false) // Foreign key to Category
    private Category category;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

}
