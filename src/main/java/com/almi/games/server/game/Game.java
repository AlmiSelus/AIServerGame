package com.almi.games.server.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "game_id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_player_1")
    private GamePlayer player1;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_player_2")
    @Setter
    private GamePlayer player2;

    @Column(name = "game_created_timestamp", nullable = false)
    private LocalDateTime createdTimestamp;

    @Column(name = "game_finished_timestamp")
    @Setter
    private LocalDateTime finishedTimestamp;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GameMove> gameMoves;

    @Column(name = "game_status")
    @Setter
    private GameStatus gameState;

    @Column(name = "game_link_hash")
    private String gameLink;
}
