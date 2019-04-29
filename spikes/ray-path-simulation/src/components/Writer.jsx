import React, {useState, useContext} from "react";

// Create a Context
const CounterContext = React.createContext({count: 0, increment: () => {}, decrement: () => {}});

export const CounterContextProvider = (props) => {
    const [count, setCount] = useState(0)

    const increment = () => setCount(count + 1)
    const decrement = () => setCount(count - 1)

    return <CounterContext.Provider value={{count , increment, decrement}}>
        {props.children}
    </CounterContext.Provider>
}

export function Writer() {
    const {increment, decrement} = useContext(CounterContext)
    return <div>
        <div>
            <button onClick={increment}>
                Increment
            </button>
            <button onClick={decrement}>
                Decrement
            </button>
        </div>
    </div>
}

export const Reader = () => {
    const d = useContext(CounterContext)
    return <h1>
        {d.count}
    </h1>
}