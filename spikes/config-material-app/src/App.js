import React from 'react'
import {ConfigsContextProvider} from "./components/context/ConfigsContext";
import {ConfigApp} from "./components/ConfigApp";
import {AppConfig} from "./config/AppConfig";
import {AuthContextProvider} from 'csw-aas-js'
import 'typeface-roboto';
import {UiContextProvider} from "./components/context/UiContext";

const App = () => {
    return (<AuthContextProvider config={AppConfig}>
        <ConfigsContextProvider>
            <UiContextProvider>
                <ConfigApp/>
            </UiContextProvider>
        </ConfigsContextProvider>
    </AuthContextProvider>)
}

export default App