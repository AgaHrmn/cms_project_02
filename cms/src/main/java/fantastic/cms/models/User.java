package fantastic.cms.models;
import lombok.Data;
import javax.persistence.*;


@Data

public class User {

    String id;
    String identity;
    String name;
    Role role;
}
