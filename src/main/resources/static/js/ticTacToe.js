/**
 * Created by c309044 on 2017-08-23.
 */
$(document).ready(function(){
    var ticTacToeBoard = {
        rows: 3,
        cols: 3,
        movesData: [],
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
            var that = this;
            $.get('/api/game/' + gameID + '/moves', function(gameMovesList) {
                console.log(gameMovesList);
                that.movesData = gameMovesList;
                currentlyShownStep = currentlyShownStep || gameMovesList.length-1;
                that.appendStepsToBoard(that.movesData, currentlyShownStep);
            });
        },
        clearBoard : function () {
            $('div.tictactoe-row > div.tictactoe-box').each(function () {
                $(this).html('');
            });
        },
        appendStepsToBoard : function (gameMovesList, currentlyShownStep) {
            this.clearBoard();
            for(var i = 0; i < gameMovesList.length && i <= currentlyShownStep; ++i) {
                var gameMove = gameMovesList[i];
                var col = gameMove.col;
                var row = gameMove.row;
                var char = gameMove.movementCharacter === '' ? '' :
                    gameMove.movementCharacter === 'X' ?
                        '<i class="fa fa-times" aria-hidden="true"></i>':
                        '<i class="fa fa-circle-o" aria-hidden="true"></i>';

                $('div.tictactoe-box[data-row="' + row + '"][data-col="' + col + '"]').html(char);
            }
        }
    };

    function startUp() {
        ticTacToeBoard.initBoard(function(){
            ticTacToeBoard.appendBoardTo($('div#board'));
            initTimestampButtons();
        });
    }

    function initTimestampButtons() {
        $(".timestampLink").each(function() {
            var btn = $(this);
            btn.click(function () {
                var timestamp = Date.parse(btn.data("timestamp"));
                console.log('dupa' + timestamp);
                var indexToShowUpTo = ticTacToeBoard.movesData.findIndex(function (boardElement) {
                    return Date.parse(boardElement.moveTimestamp) === timestamp;
                });
                ticTacToeBoard.appendStepsToBoard(ticTacToeBoard.movesData, indexToShowUpTo);
            })
        });
    }

    startUp();

});