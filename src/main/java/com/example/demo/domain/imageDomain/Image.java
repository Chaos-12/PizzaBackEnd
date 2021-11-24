package com.example.demo.domain.imageDomain;

import javax.validation.constraints.NotEmpty;

import com.example.demo.core.EntityBase;
import org.springframework.data.relational.core.mapping.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Image extends EntityBase {

    @Column
    @NotEmpty
    private byte[] content;

}
