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
    String id;
    String title;
    String content;
    User author;


    @ManyToMany
    @JoinTable(name = "news_categories",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    Set<Category> categories;


}
