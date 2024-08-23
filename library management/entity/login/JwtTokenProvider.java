package com.projectrestapi.projectrestapi.entity.login;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JwtTokenProvider {

    private String jwtToken;
    private String email;
}
