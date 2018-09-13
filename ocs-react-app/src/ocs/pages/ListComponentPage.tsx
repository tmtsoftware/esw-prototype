import {ListComponentsClient} from "../client/ListComponentsClient";
import {ListComponent} from "../components/ListComponent";
import * as React from "react";

export const ListComponentPage = () => {
    const listClient = new ListComponentsClient("/locations");
    return <ListComponent client={listClient}/>
};