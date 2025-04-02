package fantastic.cms.requests;

import java.util.Set;
import lombok.Data;

import fantastic.cms.models.Category;

@Data
public class NewsRequest {
    String title;
    String content;


    Set<Category> categories;

}