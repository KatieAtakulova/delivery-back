package ua.nure.delivery.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

import static ua.nure.delivery.util.Constants.SCHEME_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "id")
@Table(name = "comment", schema = SCHEME_NAME)
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    private String text;

    private Integer rating;

    public Comment(String text, Integer rating) {
        this.text = text;
        this.rating = rating;
    }
}
