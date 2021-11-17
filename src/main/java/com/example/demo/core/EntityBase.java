package com.example.demo.core;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EntityBase implements Persistable<Integer> {

    @Id
    // @Column(columnDefinition = "binary(16)")
    private Integer id;

    @Transient
    private boolean isThisNew = false;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EntityBase)) {
            return false;
        }
        EntityBase tmpEntity = (EntityBase) obj;
        return this.id.equals(tmpEntity.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
