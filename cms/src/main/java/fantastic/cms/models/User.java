package fantastic.cms.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "`user`", schema = "my_schema")
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
    
    private boolean enabled = false;
}
