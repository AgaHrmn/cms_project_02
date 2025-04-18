package fantastic.cms.models;

import fantastic.cms.constant.UserType;
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

    @Column(unique = true)
    String username;

    @Column
    String password;

    @Column(unique = true)
    String email;

    @Column
    @Enumerated(EnumType.ORDINAL)
    UserType type;

    @OneToMany(mappedBy = "author")
    Set<Category> categories;
    
    private boolean enabled = false;
}
