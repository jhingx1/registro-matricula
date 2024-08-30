package com.matricula.controller;

import com.matricula.dto.EstudianteDTO;
import com.matricula.model.Estudiante;
import com.matricula.service.IEstudianteService;
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
@RequestMapping("/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final IEstudianteService service;

    @Qualifier("defatulMapper")
    private final ModelMapper modelMapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<EstudianteDTO>>> findAll(){
        Flux<EstudianteDTO> fx =service.findAll()
                .map(e -> this.convertToDto(e));

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<EstudianteDTO>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(this::convertToDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<EstudianteDTO>> save(@RequestBody EstudianteDTO dto, final ServerHttpRequest req){
        return service.save(this.convertToDocument(dto))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.created(URI.create(req.getURI().toString().concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public  Mono<ResponseEntity<EstudianteDTO>> update(@PathVariable("id") String id,@RequestBody EstudianteDTO dto){
        return Mono.just(this.convertToDocument(dto))
                .map(e -> {
                    e.setId(id);
                    return e;
                })//Mono<Dish>
                .flatMap(e -> service.update(id,e))//Mono<Dish>
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

    private EstudianteDTO convertToDto(Estudiante model){
        return modelMapper.map(model,EstudianteDTO.class);
    }

    private Estudiante convertToDocument(EstudianteDTO dto){
        return modelMapper.map(dto,Estudiante.class);
    }
}
