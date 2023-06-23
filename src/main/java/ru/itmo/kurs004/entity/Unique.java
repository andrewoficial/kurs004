package ru.itmo.kurs004.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter


//Не понимаю зачем от него всем наследоваться
@MappedSuperclass
abstract public class Unique {
    @Id
    protected String code;

    public Unique() {}

    public Unique(String code) {
        this.code = code;
    }
}
