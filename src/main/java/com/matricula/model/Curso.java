package com.matricula.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cursos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Curso {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    @Field
    private String nombre;
    @Field
    private String siglas;
    @Field
    private Boolean estado;
}
