package fantastic.cms.requests;
import lombok.Data;

@Data
public class CategoryRequest {
    String name;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
