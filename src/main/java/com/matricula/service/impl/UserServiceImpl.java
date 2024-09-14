package com.matricula.service.impl;

import com.matricula.model.User;
import com.matricula.repo.IGenericRepo;
import com.matricula.repo.IRoleRepo;
import com.matricula.repo.IUserRepo;
import com.matricula.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor//inyeccion por constructor
public class UserServiceImpl extends CRUDImpl<User,String> implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    //inyeccion por constructor, el @Autowired, brinda mucho acomplamiento

    private final IUserRepo userRepo;
    private final IRoleRepo roleRepo;
    private final BCryptPasswordEncoder bcrypt;

    @Override
    protected IGenericRepo<User, String> getRepo() {
        return userRepo;
    }

    @Override
    public Mono<User> saveHash(User user) {
        user.setPassword(bcrypt.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Mono<com.matricula.security.User> searchByUser(String username) {
        return userRepo.findOneByUsername(username)
                .switchIfEmpty(Mono.error(new RuntimeException("No user found with username: " + username))) // Manejar si no se encuentra un usuario
                .doOnNext(user -> log.info("User found: {}", user))  // Imprime el usuario encontrado
                .flatMap(user ->
                        Flux.fromIterable(user.getRoles())
                                .flatMap(userRole -> roleRepo.findById(userRole.getId())
                                        .map(role -> {
                                            log.info("Role found: {}", role.getName());
                                            return role.getName();
                                        }))
                                .collectList()
                                .map(roles -> {
                                    com.matricula.security.User secureUser = new com.matricula.security.User(
                                            user.getUsername(), user.getPassword(), user.isStatus(), roles);
                                    log.info("Final User Object: {}", secureUser);
                                    return secureUser;
                                })
                );
    }
}
