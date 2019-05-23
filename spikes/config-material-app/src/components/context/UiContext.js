import React, {useState} from "react";

const e = () => {
};

export const UiContext = React.createContext({
  addModalOpen: false,
  error: false,
  openAddModal: e,
  closeAddModal: e,
  setError: e,
  removeError: e
});

export const UiContextProvider = (props) => {
  const [uiState, setUIState] = useState({
    addModalOpen: false,
    error: false
  });

  const openAddModal = () => {
    setUIState({...uiState, addModalOpen: true});
  };

  const closeAddModal = () => {
    setUIState({...uiState, addModalOpen: false});
  };

  const setError = (text) => {
    setUIState({...uiState, error: text? text:"Something went wrong"});
  };

  const removeError = () => {
    setUIState({...uiState, error: false});
  };

  return <UiContext.Provider value={{addModalOpen: uiState.addModalOpen, error : uiState.error,  openAddModal, closeAddModal, setError, removeError}}>
    {props.children}
  </UiContext.Provider>
};

export default UiContext
