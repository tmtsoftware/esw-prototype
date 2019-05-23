import React, {useState} from "react";

const e = () => {};

const ConfigsContext = React.createContext({
  items: [],
  addItem: e,
  removeItem: e,
  setItems: e
});

export const ConfigsContextProvider = (props) => {
  const [items, setItems] = useState([]);

  const addItem = (item) => {
    setItems(items.concat(item))
  };

  const removeItem = (path) => setItems(items.filter(item => item.path !== path));

  return <ConfigsContext.Provider value={{items, addItem, removeItem, setItems}}>
    {props.children}
  </ConfigsContext.Provider>
};

export default ConfigsContext
