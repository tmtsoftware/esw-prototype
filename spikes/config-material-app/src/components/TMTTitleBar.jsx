import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import {makeStyles} from "@material-ui/styles";
import AuthButton from "./AuthButton";
import {UserInfo} from "./UserInfo";

const useStyles = makeStyles({
  root: {
    flexGrow: 1,
  },
  grow: {
    flexGrow: 1,
  },
  menuButton: {
    marginLeft: -12,
    marginRight: 20,
  },
});

function TMTTitleBar() {
  const classes = useStyles();

  return (
    <div className={classes.root}>
      <AppBar position="static">
        <Toolbar>
          {/*<IconButton className={classes.menuButton} color="inherit" aria-label="Menu">*/}
          {/*  <MenuIcon />*/}
          {/*</IconButton>*/}
          <Typography variant="h6" color="inherit" className={classes.grow}>
            TMT CSW Configurations
          </Typography>
          <UserInfo />
          <AuthButton />
        </Toolbar>
      </AppBar>
    </div>
  );
}

TMTTitleBar.propTypes = {

};

export default TMTTitleBar

