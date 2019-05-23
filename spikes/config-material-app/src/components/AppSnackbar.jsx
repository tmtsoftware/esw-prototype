import React, {useContext} from "react";
import Snackbar from '@material-ui/core/Snackbar';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import UiContext from "./context/UiContext";
import * as PropTypes from 'prop-types';

export const AppSnackbar = (props) => {
  const {removeError, error} = useContext(UiContext);

  return <Snackbar
    anchorOrigin={{
      vertical: 'bottom',
      horizontal: 'left',
    }}
    open={error !== false}
    onClose={removeError}
    ContentProps={{
      'aria-describedby': 'message-id',
    }}
    message={<span id="message-id">{error.toString()}</span>}
    action={[
      <IconButton
        key="close"
        aria-label="Close"
        color="inherit"
        onClick={removeError}
      >
        <CloseIcon />
      </IconButton>,
    ]}
  />
};
