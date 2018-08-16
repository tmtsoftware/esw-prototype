import * as React from 'react';
import {Component} from 'react';
import EditorClient from "../../client/EditorClient";
import jss from "jss";
import {buttonstyles} from "../../jss/butonstyles";
import {textareastyles} from "../../jss/textareastyles";

interface IState {
    showSequenceResponse: string
}

interface IProps {
    client: EditorClient
}

const button = jss.createStyleSheet(buttonstyles).attach().classes.button;
const textarea = jss.createStyleSheet(textareastyles).attach().classes.textArea;

class ShowSequenceComponent extends Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {showSequenceResponse: ""};
    }

    public callBack(res: string) {
        this.setState({showSequenceResponse: res});
    }

    public render() {
        return (
            <div className="card-panel hoverable">
                <h6>Sequence Editor Show Sequence</h6>
                <div>
                    <button className={`${button}`} onClick={this.handleClick}>Show Sequence</button>
                    <div><span><textarea className={`${textarea}`} value={this.state.showSequenceResponse}/></span></div>
                </div>
            </div>
        );
    }

    private handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        this.props.client.showSequence((res) => this.callBack(res))
    };
}

export default ShowSequenceComponent;