package fantastic.cms.models;

import fantastic.cms.constant.UserType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    Set<Category> categories;

    @OneToMany(mappedBy = "author")
    Set<News> news;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    VerificationToken verificationToken;
    
    private boolean enabled = false;
}
