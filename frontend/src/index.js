import React, {useEffect, useState} from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import {jsonToArray} from './helpers';
import Button from './Button';


class Main extends React.Component {

    constructor() {
        super();
        this.state = {
            data: [],
            curTime: new Date().toLocaleString()
        };
    }

    componentDidMount() {
        setInterval(() => {
            this.setState({
                curTime: new Date().toLocaleString()
            })
        }, 1000)

        /*        fetch('/api/json')
                    .then(res => res.json())
                    .then(json => this.setState({ data: json }));*/
    }

    async getContent() {

        const response = await fetch('/api/json')
            .then((resp) => {
                const json = resp.json() // there's always a body
                if (resp.status >= 200 && resp.status < 300) {
                    return json
                } else {
                    return "";
                    //return json.then(err => {throw err;});
                    // reject([{name: "noName", value: "noValue"}]) todo
                }
            }).catch(err => console.log("error1"))

        const json = await response;

        const array = jsonToArray(json);

        this.setState({
            data: array
        });
    }

    render() {
        this.getContent();
        return (
            <div>
                <ul>
                    {this.state.data.map(el => (
                        <li>
                            {el.name}: {el.value}
                        </li>
                    ))}
                </ul>
                <div>
                    text: {this.state.curTime}
                </div>
                <div>
                    <RealTimeChart/>
                </div>
            </div>
        );
    }

}

/*function Main2() {
    const [message, setMessage] = useState("");

    useEffect(() => {
        fetch('/api/json')
            .then(response => response.text())
            .then(message => {
                setMessage(message);
            });
    }, [])

    return (
        <div className="App">
            <header className="App-header">
                <h1 className="App-title">{message}</h1>
            </header>
            <p className="App-intro">
                To get started, edit <code>src/App.js</code> and save to reload.
            </p>
        </div>
    )

}*/
/*
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
}*/

// ========================================

ReactDOM.render(
    <Main/>,
    document.getElementById('root')
);

