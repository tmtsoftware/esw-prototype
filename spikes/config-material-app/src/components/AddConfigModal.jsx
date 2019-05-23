import React, {useContext, useState} from "react";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import TextField from "@material-ui/core/TextField";
import DialogActions from "@material-ui/core/DialogActions";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import UiContext from "./context/UiContext";
import {addConfig} from "./configServerApi";
import {AuthContext} from 'csw-aas-js';
import ConfigsContext from "./context/ConfigsContext";

export const AddConfigModal = () => {

  const {addModalOpen, closeAddModal} = useContext(UiContext);
  const {auth} = useContext(AuthContext);
  const {setError} = useContext(UiContext);
  const {addItem} = useContext(ConfigsContext);

  const [data, setData] = useState({});

  const clearData = () => {
    setData({})
  };

  const clearDataAndClose = () => {
    clearData();
    closeAddModal();
  };

  const saveConfig = async () => {
    try{
      await addConfig(data.path, data.message, auth.token(), data.text);
      addItem({id: data.path, path: data.path, author: auth && auth.tokenParsed() && auth.tokenParsed().preferred_username});
      clearDataAndClose()
    }
    catch (e) {
      setError(e.toString())
    }
  };

  return <Dialog
    open={addModalOpen}
    onClose={clearDataAndClose}
    aria-labelledby="form-dialog-title"
    fullWidth
  >
    <DialogTitle id="form-dialog-title">Add new configuration</DialogTitle>
    <DialogContent>
      <TextField
        autoFocus
        margin="dense"
        id="path"
        label="Path"
        type="text"
        fullWidth
        value={data.path}
        multiline
        onChange={(e) => setData({...data, path: e.target.value})}
      />
      <TextField
        margin="dense"
        id="commit_message"
        label="Commit Message"
        type="text"
        value={data.message}
        multiline={true}
        fullWidth
        onChange={(e) => setData({...data, message: e.target.value})}
      />
      <TextField
        margin="dense"
        id="config_text"
        label="Configuration Text"
        type="text"
        multiline={true}
        value={data.text}
        fullWidth
        rows={4}
        variant={'outlined'}
        onChange={(e) => setData({...data, text: e.target.value})}
      />
    </DialogContent>
    <DialogActions>
      <Button onClick={clearDataAndClose} color="secondary">
        Cancel
      </Button>
      <Button onClick={async () => await saveConfig()} color="primary">
        Save
      </Button>
    </DialogActions>
  </Dialog>
};
