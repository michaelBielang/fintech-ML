import React, {useEffect, useState} from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import {jsonToArray, testData} from './helpers';
import Example from "./view/chart";
/*import {chartFunctions} from "./view/chart";*/

/*const chartFunctionsMap = chartFunctions;*/

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
                    <Example/>
                </div>
            </div>
        );
    }

}

ReactDOM.render(
    <Main/>,
    document.getElementById('root')
);

