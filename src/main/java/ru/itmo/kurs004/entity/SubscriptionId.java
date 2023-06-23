package ru.itmo.kurs004.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

/*
@Embeddable
public class SubscriptionId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "p_article", referencedColumnName = "code")
    private Publication publication;
    @ManyToOne
    @JoinColumn(name = "r_phone", referencedColumnName = "code")
    private Recipient recipient;
    public SubscriptionId(Recipient recipient, Publication publication) {
        this.recipient = recipient;
        this.publication = publication;
    }
}
*/