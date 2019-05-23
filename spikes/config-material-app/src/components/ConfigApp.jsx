import React, {useContext, useEffect} from "react";
import TMTTitleBar from "./TMTTitleBar";
import ConfigCollection from "./ConfigCollection";
import ConfigsContext from "./context/ConfigsContext";
import AddButton from "./AddButton";
import {AppSnackbar} from "./AppSnackbar";

export const ConfigApp = (props) => {

  const {items, setItems} = useContext(ConfigsContext);


  const fetchConfigs = async () => {
    const response = await window.fetch(`http://localhost:5000/list`);
    if (response.status === 200) {
      setItems(await response.json())
    } else {
      console.error(response.status)
    }
  };

  useEffect(() => {
    fetchConfigs()
  }, []);

  return <div>
    <TMTTitleBar/>
    <ConfigCollection configs={items}/>
    <AppSnackbar />
    <AddButton/>
  </div>
};
