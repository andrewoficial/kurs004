package ru.itmo.kurs004.entity;

import java.util.ArrayList;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Entity
@Table(name = "tb_publication")
public class Publication extends Unique {
    @Column(name = "title", nullable = false)
    private String title;
    @Column(nullable = false)
    private String type;
    @Column(name = "cost")
    private double subscriptionCost;
    @Column(name = "is_active")
    private boolean activeSubscription;

    public Publication(String article, String type, String title, double subscriptionCost, boolean activeSubscription) {
        super(article);
        this.type = type;
        this.title = title;
        this.subscriptionCost = subscriptionCost;
        this.activeSubscription = activeSubscription;
    }
    public Publication() {}

    public String getInfo(){
        StringBuilder sb = new StringBuilder("");
        sb.append("Артикул: [");
        sb.append(this.code);
        sb.append("] Название: [");
        sb.append(this.title);
        sb.append("] Тип: [");
        sb.append(this.type);
        sb.append("] Стоимость: [");
        sb.append(this.subscriptionCost);
        sb.append("]");
        return sb.toString();
    }
}