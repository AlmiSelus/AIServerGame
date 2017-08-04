package com.almi.games.server.game;

import lombok.*;

import javax.persistence.*;

/**
 * Created by c309044 on 2017-08-03.
 */
@Entity
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GameMove {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "game_move_id", nullable = false)
    private Long id;

    @ManyToOne
    private Game game;

    @OneToOne
    @JoinColumn(name = "game_move_player", nullable = false)
    private GamePlayer player;

    @Column(name = "game_move_row")
    private int row;

    @Column(name = "game_move_col")
    private int col;
}
