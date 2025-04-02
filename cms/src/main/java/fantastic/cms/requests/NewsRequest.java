package fantastic.cms.requests;

import java.util.Set;
import lombok.Data;

import fantastic.cms.models.Category;

@Data
public class NewsRequest {
    private String title;
    private String content;
    private String categoryId;  // ID of the category
    private String newCategory; // Optionally, for new category name if required
    private String authorId;    // ID of the author

}