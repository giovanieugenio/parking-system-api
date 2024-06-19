package com.park.api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_client_have_vacancy")
@EntityListeners(AuditingEntityListener.class)
public class ClientVacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receipt_number", nullable = false, unique = true, length = 15)
    private String receipt;

    @Column(name = "brand", nullable = false, length = 40)
    private String brand;

    @Column(name = "plate", nullable = false, length = 8)
    private String plate;

    @Column(name = "model", nullable = false, length = 40)
    private String model;

    @Column(name = "color", nullable = false, length = 40)
    private String color;

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate;

    @Column(name = "exit_date")
    private LocalDateTime exitDate;

    @Column(name = "price", nullable = false, columnDefinition = "decimal(7,2)")
    private BigDecimal price;

    @Column(name = "discount", nullable = false, columnDefinition = "decimal(7,2)")
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_vacancy", nullable = false)
    private Vacancy vacancy;

    @CreatedDate
    @Column(name = "newDate")
    private LocalDateTime newDate;

    @LastModifiedDate
    @Column(name = "modifyDate")
    private LocalDateTime modifyDate;

    @CreatedBy
    @Column(name = "userCreator")
    private String userCreator;

    @LastModifiedBy
    @Column(name = "userModifier")
    private String userModifier;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientVacancy that = (ClientVacancy) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
