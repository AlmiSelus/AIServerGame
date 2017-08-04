package com.almi.games.server.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

/**
 * Created by c309044 on 2017-08-03.
 */
@Entity
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "player_id", nullable = false)
    @JsonIgnore
    private Long id;

    @Column(name = "player_name", nullable = false)
    private String name;

}
