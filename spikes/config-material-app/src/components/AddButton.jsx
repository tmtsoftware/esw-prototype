import Fab from "@material-ui/core/Fab";
import React, {useContext} from "react";
import {makeStyles} from "@material-ui/styles";
import AddIcon from '@material-ui/icons/Add';
import {ClientRole} from 'csw-aas-js'
import {AddConfigModal} from "./AddConfigModal";
import UiContext from "./context/UiContext";

const useStyles = makeStyles({
  fab: {
    position: 'absolute',
    bottom: '20px',
    right: '20px'
  }
});

const AddButton = () => {
  const classes = useStyles();

  const {openAddModal} = useContext(UiContext);

  return (
    <ClientRole clientRole={'admin'} client='csw-config-server'
                error={<span className={classes.fab}>Please login as admin to add new configurations</span>}>
      <Fab color="primary" aria-label="Add" className={classes.fab}>
        <AddIcon onClick={openAddModal}/>
      </Fab>
      <AddConfigModal />
    </ClientRole>
  );
};

AddButton.propTypes = {};

export default AddButton;
