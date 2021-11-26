package com.example.demo.core;

import java.util.Set;
import java.util.UUID;

import javax.persistence.MappedSuperclass;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.example.demo.core.exceptions.BadRequestException;
import com.example.demo.core.functionalInterfaces.ExistsByField;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@MappedSuperclass
@Getter
@Setter
public abstract class EntityBase implements Persistable<UUID> {
    @Id
    private UUID id;
    @Transient
    private boolean isThisNew = false;

    @Override
    public boolean isNew() {
        return this.isThisNew();
    }

    public void validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EntityBase>> violations = validator.validate(this);
        if (!violations.isEmpty()) {
            BadRequestException badRequestException = new BadRequestException();
            for (ConstraintViolation<EntityBase> violation : violations) {
                badRequestException.addException(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw badRequestException;
        }
    }

    public Mono<Void> validate(String key, String value, ExistsByField existsByField) {
        this.validate();
        return existsByField.exists(value).flatMap(exists -> {
            if (exists) {
                BadRequestException badRequestException = new BadRequestException();
                badRequestException.addException(key, String.format("value '%s' is duplicated.", value));
                return Mono.error(badRequestException);
            } else {
                return Mono.empty();
            }
        });
    }

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