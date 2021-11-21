package com.example.demo.domain;

import com.example.demo.core.EntityBase;
import org.springframework.data.relational.core.mapping.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Image extends EntityBase {

    @Column
    private byte[] content;

}
