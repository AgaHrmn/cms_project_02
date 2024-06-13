package fantastic.cms.requests;
import lombok.Data;
import fantastic.cms.models.Role;

@Data
public class UserRequest {
    String name;
    Role role;
    private String password;

}
