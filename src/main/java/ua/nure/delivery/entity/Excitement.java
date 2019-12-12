package ua.nure.delivery.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static ua.nure.delivery.util.Constants.SCHEME_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "id")
@Table(name = "excitement", schema = SCHEME_NAME)
public class Excitement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    private String text;

    private String address;

    @ManyToOne
    @JoinColumn(name = "order_fk")
    private Order order;

    @Builder
    public Excitement(String text, String address, Order order) {
        this.text = text;
        this.address = address;
        this.order = order;
    }
}
