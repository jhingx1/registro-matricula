package com.matricula.service;

import com.matricula.model.User;
import reactor.core.publisher.Mono;

public interface IUserService extends ICRUD<User,String>{
    Mono<User> saveHash(User user);
    Mono<com.matricula.security.User> searchByUser(String username);
}
