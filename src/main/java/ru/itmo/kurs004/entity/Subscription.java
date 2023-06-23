package ru.itmo.kurs004.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.*;
import ru.itmo.kurs004.entity.Publication;
import ru.itmo.kurs004.entity.Recipient;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Entity
@Table(name = "tb_subscription")
public class Subscription {
    @Column(name = "term", nullable = false)
    private int subscriptionTerm;
    @Column(name = "delivery_start", nullable = false)
    private LocalDate deliveryStart;

    @Id
    @ManyToOne
    @JoinColumn(name = "p_article", referencedColumnName = "code")
    private Publication publication;
    @Id
    @ManyToOne
    @JoinColumn(name = "r_phone", referencedColumnName = "code")
    private Recipient recipient;
    public Subscription(int subscriptionTerm, LocalDate startDate, Recipient recipient, Publication publication) {
        //this.code = new SubscriptionId(recipient, publication);
        this.subscriptionTerm = subscriptionTerm;
        this.deliveryStart = startDate;
        this.recipient = recipient;
        this.publication = publication;
    }
    public Subscription() {}


    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionTerm=" + subscriptionTerm +
                ", deliveryStart=" + deliveryStart +
                ", publication=" + publication.getCode() +
                ", recipient=" + recipient.getCode() +
                '}';
    }

}

