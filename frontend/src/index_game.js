import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';


function calculateWinner(squares) {
    const lines = [
        [0, 1, 2],
        [3, 4, 5],
        [6, 7, 8],
        [0, 3, 6],
        [1, 4, 7],
        [2, 5, 8],
        [0, 4, 8],
        [2, 4, 6],
    ];
    for (let i = 0; i < lines.length; i++) {
        const [a, b, c] = lines[i];
        if (squares[a] && squares[a] === squares[b] && squares[a] === squares[c]) {
            return squares[a];
        }
    }
    return null;
}

function Square(props) {
    return (
        <button className="square" onClick={() => props.onClick()}>
            {props.value}
        </button>
    )
}

class Board extends React.Component {

    renderSquare(value) {
        return (
            <Square
                value={this.props.squares[value]}
                onClick={() => this.props.onClick(value)}
            />
        )
    }

    render() {
        return (
            <div>
                <div className="board-row">
                    {this.renderSquare(0)}
                    {this.renderSquare(1)}
                    {this.renderSquare(2)}
                </div>
                <div>
                    {this.renderSquare(3)}
                    {this.renderSquare(4)}
                    {this.renderSquare(5)}
                </div>
                <div>
                    {this.renderSquare(6)}
                    {this.renderSquare(7)}
                    {this.renderSquare(8)}
                </div>
            </div>
        )
    }
}

class Game extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            historyArray: [{
                squares: Array(9).fill(null),
            }],
            xIsNext: true,
            stepNumber: 0,
        }
    }

    jumpTo(step) {
        this.setState({
            stepNumber: step,
            xIsNext: (step % 2) === 0,
        });
    }

    handleClick(i) {
        const newHistory = this.state.historyArray.slice(0, this.state.stepNumber + 1);
        const currentSquareMap = newHistory[newHistory.length - 1]; //map
        const currentSquare = currentSquareMap.squares.slice();
        if (calculateWinner(currentSquare) || currentSquare[i]) {
            return;
        }
        currentSquare[i] = this.state.xIsNext ? 'X' : 'O'; // fills X or Y at position i of this square entry

        //funzt nicht
        this.setState({
                historyArray: newHistory.concat([{
                    squares: currentSquare
                }]),
                stepNumber: newHistory.length,
                xIsNext: !this.state.xIsNext,
                counter: 0,
            }
        )
    }


    /*  handleClick(i) {
          const history = this.state.historyArray.slice(0, this.state.stepNumber + 1);
          const current = history[history.length - 1];
          const squares = current.squares.slice();
          if (calculateWinner(squares) || squares[i]) {
              return;
          }
          squares[i] = this.state.xIsNext ? 'X' : 'O';
          this.setState({
              historyArray: history.concat([{
                  squares: squares
              }]),
              stepNumber: history.length,
              xIsNext: !this.state.xIsNext,
          });
      }*/

    render() {
        const history = this.state.historyArray;
        const current = history[this.state.stepNumber];
        const winner = calculateWinner(current.squares);
        const displayCounter = new Date().getTime();


        const moves = history.map((step, move) => {
            const desc = move ?
                'Go to move #' + move :
                'Go to game start';
            return (
                <li key={move}>
                    <button onClick={() => this.jumpTo(move)}>{desc}</button>
                </li>
            );
        });

        let status;
        if (winner) {
            status = 'Winner is: ' + winner;
        } else {
            status = 'Next player: ' + (this.state.xIsNext === false ? 'X' : 'O');
        }

        return (
            <div className="game">
                <div className="game-board">
                    <Board
                        squares={current.squares}
                        onClick={(i) => this.handleClick(i)}
                    />
                </div>
                <div className="game-info">
                    <div>{status}</div>
                    <div>{displayCounter}</div>
                    <ol>{moves}</ol>
                </div>
            </div>
        );
    }
}

// ========================================

ReactDOM.render(
    <Game/>,
    document.getElementById('root')
);

