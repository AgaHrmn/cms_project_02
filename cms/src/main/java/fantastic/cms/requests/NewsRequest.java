package fantastic.cms.requests;

import java.util.Set;
import lombok.Data;

import fantastic.cms.models.Category;

@Data
public class NewsRequest {
    private String title;
    private String content;
    private Category category;

}