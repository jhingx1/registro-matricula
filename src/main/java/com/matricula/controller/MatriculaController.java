package com.matricula.controller;

import com.matricula.dto.CursoDTO;
import com.matricula.dto.MatriculaDTO;
import com.matricula.model.Curso;
import com.matricula.model.Matricula;
import com.matricula.service.IMatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final IMatriculaService service;

    @Qualifier("matriculaMapper")
    private final ModelMapper modelMapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<MatriculaDTO>>> findAll(){
        Flux<MatriculaDTO> fx = service.findAll()
                .map(this::convertToDto);//modelMapper.map(e,MatriculaDTO.class)

        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MatriculaDTO>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(this::convertToDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<MatriculaDTO>> save(@Valid @RequestBody MatriculaDTO dto, final ServerHttpRequest req){
        return service.save(this.convertToDocument(dto))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.created(URI.create(req.getURI().toString().concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public  Mono<ResponseEntity<MatriculaDTO>> update(@Valid @PathVariable("id") String id,@RequestBody MatriculaDTO dto){
        return Mono.just(this.convertToDocument(dto))
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> service.update(id,e))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                ;
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id){
        return service.delete(id)
                .flatMap(r -> {
                    if(r){
                        return Mono.just(ResponseEntity.noContent().build());
                    }else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    private MatriculaDTO convertToDto(Matricula model){
        return modelMapper.map(model, MatriculaDTO.class);
    }

    private Matricula convertToDocument(MatriculaDTO dto){
        return modelMapper.map(dto,Matricula.class);
    }

}
