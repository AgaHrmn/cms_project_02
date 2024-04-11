package fantastic.cms.models;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "news")
public class News {
    @Id
    @UuidGenerator
    String id;
    String title;
    String content;
    User author;

    @ManyToMany
    @JoinTable(name = "news_reviewers",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<User> mandatoryReviewers;

    @OneToMany
    Set<Review> reviews;

    @ManyToMany
    @JoinTable(name = "news_categories",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    Set<Category> categories;

    @ManyToMany
    @JoinTable(name = "news_tags",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    Set<Tag> tags;

    public Review addReview(String userId, Status status) {
        final Review review = new Review(userId, status);
        this.reviews.add(review);
        return review;
    }

    public Boolean revised() {
        return this.mandatoryReviewers.stream()
                .allMatch(reviewer -> this.reviews.stream()
                        .anyMatch(review -> reviewer.id.equals(review.userId) &&
                                Status.APPROVED == review.status));
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void deleteReview(String reviewId) {
        reviews.removeIf(review -> review.getUserId().equals(reviewId));
    }

}
