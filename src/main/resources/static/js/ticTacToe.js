/**
 * Created by c309044 on 2017-08-23.
 */
$(document).ready(function(){
    var ticTacToeBoard = {
        rows: 3,
        cols: 3,
        initBoard : function(callback) {
            var that = this;
            $.get('/api/game/new', function(data) {
                that.rows = data.board.rows;
                that.cols = data.board.cols;
                callback();
            });
        },

        appendBoardTo : function(boardStr) {
            var board = '';
            for(var row = 0; row < this.rows; ++row) {
                board += '<div class="tictactoe-row">';
                for(var col = 0; col < this.cols; ++col) {
                    board += '<div class="tictactoe-box box-border text-center" data-row="'+row+'" data-col="'+col+'"></div>';
                }
                board += '</div>';
            }

            $(boardStr).append(board);
            this.loadSteps();
        },
        loadSteps : function(currentlyShownStep) {
            var gameID = $('#gameIdHolder').text();
            $.get('/api/game/' + gameID + '/moves', function(gameMovesList) {
                console.log(gameMovesList);
                for(var i = 0; i < gameMovesList.length; ++i) {
                    var gameMove = gameMovesList[i];
                    var col = gameMove.col;
                    var row = gameMove.row;
                    $('div.tictactoe-box[data-row="' + row + '"][data-col="' + col + '"]').text(gameMove.movementCharacter);
                }
            });
        }
    };

    function startUp() {
        ticTacToeBoard.initBoard(function(){
            ticTacToeBoard.appendBoardTo($('div#board'));
        });
    }

    startUp();

});