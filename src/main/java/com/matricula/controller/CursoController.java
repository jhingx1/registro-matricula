package com.matricula.controller;

import com.matricula.dto.CursoDTO;
import com.matricula.model.Curso;
import com.matricula.service.ICursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.server.reactive.ServerHttpRequest;
import java.net.URI;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final ICursoService service;
    @Qualifier("cursoMapper")
    private final ModelMapper modelMapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<CursoDTO>>> findAll(){
        Flux<CursoDTO> fx = service.findAll()
                .map(this::convertToDto);//modelMapper.map(e,CursoDTO.class)

        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CursoDTO>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(this::convertToDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<CursoDTO>> save(@Valid @RequestBody CursoDTO dto, final ServerHttpRequest req){
        return service.save(this.convertToDocument(dto))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.created(URI.create(req.getURI().toString().concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public  Mono<ResponseEntity<CursoDTO>> update(@Valid @PathVariable("id") String id,@RequestBody CursoDTO dto){
        return Mono.just(this.convertToDocument(dto))
                .map(e -> {
                    e.setId(id);
                    return e;
                })//Mono<Curso>
                .flatMap(e -> service.update(id,e))//Mono<Curso>
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

    private CursoDTO convertToDto(Curso model){
        return modelMapper.map(model,CursoDTO.class);
    }

    private Curso convertToDocument(CursoDTO dto){
        return modelMapper.map(dto,Curso.class);
    }

}
