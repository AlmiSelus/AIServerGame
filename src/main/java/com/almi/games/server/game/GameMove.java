package com.almi.games.server.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by c309044 on 2017-08-03.
 */
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameMove {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "game_move_id", nullable = false)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Game game;

    @OneToOne
    @JoinColumn(name = "game_move_player", nullable = false)
    private GamePlayer player;

    @Column(name = "game_move_row")
    private int row;

    @Column(name = "game_move_col")
    private int col;

    @Column(name = "game_move_timestamp")
    private LocalDateTime moveTimestamp;
}
