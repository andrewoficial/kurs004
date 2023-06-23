package ru.itmo.kurs004.entity;

import java.util.ArrayList;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

/*
@NamedNativeQueries({
        @NamedNativeQuery(name = "all", query = "SELECT * FROM tb_users", resultClass = LibraryUser.class),
})
@NamedQueries(
        @NamedQuery(name = "byPhone", query = "SELECT lu FROM LibraryUser lu WHERE lu.phone = :phone")
)
*/

@Embeddable
@Entity
@Table(name = "tb_recipient")
public class Recipient extends Unique{
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String email;


    @ManyToMany
    @JoinTable(
            name = "recipients_subscriptions",
            joinColumns =  @JoinColumn(name = "r_cod", referencedColumnName = "code"),
            inverseJoinColumns = {
                    @JoinColumn(name = "s_term", referencedColumnName = "p_article"),
                    @JoinColumn(name = "s_code", referencedColumnName = "r_phone")
            }
    )
    private ArrayList<Subscription> subscribe;


    public Recipient(String fullName, String phone, String email) {
        super(phone);
        this.fullName = fullName;
        this.email = email;
    }

    public Recipient(String phone) {
        super(phone);
    }
    public Recipient() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipient recipient = (Recipient) o;
        return Objects.equals(super.code, recipient.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.code);
    }
}