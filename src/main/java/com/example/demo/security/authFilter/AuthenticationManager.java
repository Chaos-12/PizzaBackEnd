package com.example.demo.security.authFilter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.example.demo.core.exceptions.UnauthorizedException;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.infraestructure.redisInfraestructure.RedisRepository;
import com.example.demo.security.UserLogInfo;
import com.example.demo.security.authTokens.JwtReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private static final Map<Role,Collection<? extends GrantedAuthority>> authMap;

    static {
        authMap  = new HashMap<Role,Collection<? extends GrantedAuthority>>();
        for (Role role: Role.values()) {
            authMap.put(role, generateLowAuthorities(role));
        }
    }

    private final RedisRepository<UserLogInfo, UUID> logInfoRepository;
    private final JwtReader jwtReader;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        try {
            String authToken = authentication.getCredentials().toString();
            if(jwtReader.isTokenExpired(authToken)){
                return Mono.error(new UnauthorizedException("Authorization token is expired"));
            }
            String id = jwtReader.getSubjectFromToken(authToken);
            return this.logInfoRepository
                            .getFromID(UUID.fromString(id))
                            .switchIfEmpty(Mono.error(new UnauthorizedException("User is logged out")))
                            .map(logInfo -> new UsernamePasswordAuthenticationToken(
                                    logInfo.getId(), null, authMap.get(logInfo.getRole())
                            ));
        } catch (Exception ex){
            return Mono.error(new UnauthorizedException(ex.getMessage()));
        }
    }

    private static Collection<? extends GrantedAuthority> generateLowAuthorities(Role role){
        return IntStream.range(role.getLevel(), Role.size)
                        .mapToObj(i -> Role.fromLevel(i).toString())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
    }

    /* Method to replace generateLowAuthorities in the case that we desire to specify each role 
        to have a diferent access to each end-point.
    private static Collection<? extends GrantedAuthority> generateSingleAuthority(Role role){
        return AuthorityUtils.createAuthorityList(role.toString());
    }*/
}