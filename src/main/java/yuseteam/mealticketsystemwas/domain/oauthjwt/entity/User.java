package yuseteam.mealticketsystemwas.domain.oauthjwt.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yuseteam.mealticketsystemwas.domain.oauthjwt.RoleType;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;

    private String userPW;

    private String name;

    @Column(unique = true)
    private String socialname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private RoleType role;

    @Column(unique = true)
    private String phone;

    @Column(nullable = false)
    private Integer tokenVersion = 0;

}
