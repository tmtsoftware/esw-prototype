import FeederClient from "../client/FeederClient";
import EditorClient from "../client/EditorClient";
import ResultEventClient from "../client/ResultEventClient";
import FeederComponent from "../components/feeder/FeederComponent";
import PauseComponent from "../components/editor/PauseComponent";
import ResumeComponent from "../components/editor/ResumeComponent";
import ShowSequenceComponent from "../components/editor/ShowSequenceComponent";
import ResultEventComponent from "../components/resultevent/ResultEventComponent";
import * as React from "react";

export const SequencerPage = (props: any) => {
    const sequencerPath = `${props.location.pathname}`;
    const feederClient = new FeederClient(sequencerPath);
    const editorClient = new EditorClient(sequencerPath);
    const resultClient = new ResultEventClient(sequencerPath);

    return <div className="row">
        <div className="col s6">
            <FeederComponent client={feederClient} />
            <PauseComponent client={editorClient} />
            <ResumeComponent client={editorClient} />
            <ShowSequenceComponent client={editorClient} />
        </div>
        <div className="col s6">
            <ResultEventComponent client={resultClient}/>
        </div>
    </div>
};