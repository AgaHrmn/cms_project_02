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

    @Column(nullable = false, unique = true, length = 45)
    private String email;
    @Column(nullable = false, length = 64)
    private String password;
    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Enumerated(EnumType.STRING)
    Role role;

    @ManyToMany(mappedBy = "mandatoryReviewers")
    Set<News> news;
}
