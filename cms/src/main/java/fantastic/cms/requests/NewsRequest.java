package fantastic.cms.requests;

import lombok.Data;

@Data
public class NewsRequest {
    private String title;
    private String content; // Match `News` entity field
    private String categoryId;
    private String newCategoryName;
    private String authorId;
    private boolean comments;
}
