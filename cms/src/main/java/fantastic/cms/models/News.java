package fantastic.cms.models;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "news")
public class News {
    @Id
    @UuidGenerator
    public String id;

    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    public User author;

}
