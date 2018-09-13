import AssemblyCommandWebClient from "../client/AssemblyCommandWebClient";
import AssemblyCommandComponent from "../components/assembly/AssemblyCommandComponent";
import * as React from "react";

export const AssemblyPage = (props: any) => {
    const assemblyPath = props.location.pathname;
    const assemblyClient = new AssemblyCommandWebClient(assemblyPath);

    return <div>
        <AssemblyCommandComponent client={assemblyClient}/>
    </div>
};