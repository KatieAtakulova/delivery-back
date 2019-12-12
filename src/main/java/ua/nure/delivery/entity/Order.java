package ua.nure.delivery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ua.nure.delivery.entity.enums.DeliveryType;
import ua.nure.delivery.entity.enums.OrderState;
import ua.nure.delivery.entity.enums.OrderStatus;
import ua.nure.delivery.entity.enums.PaymentType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static ua.nure.delivery.util.Constants.SCHEME_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"orderProducts", "user"})
@Table(name = "order", schema = SCHEME_NAME)
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    private String fullName;

    private String address;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

    private String fullPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    private LocalDateTime date;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_fk")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts;
}
