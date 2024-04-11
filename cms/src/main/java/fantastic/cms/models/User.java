package fantastic.cms.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "`user`")
public class User implements Serializable {
    @Id
    @UuidGenerator
    String id;

    String name;

    @Enumerated(EnumType.STRING)
    Role role;

    @ManyToMany(mappedBy = "mandatoryReviewers")
    Set<News> news;
}
