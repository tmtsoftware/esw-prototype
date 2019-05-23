import React, {useContext} from "react";
import {AuthContext, CheckLogin} from "csw-aas-js";
import {makeStyles} from "@material-ui/styles";

const useStyles = makeStyles({
  userInfo: {
    marginRight: '10px'
  }
});

export const UserInfo = () => {
  const {auth} = useContext(AuthContext);
  const classes = useStyles();
  return <CheckLogin error={null}>
    <span className={classes.userInfo}>
      Hello, {auth && auth.tokenParsed() && auth.tokenParsed().preferred_username}
    </span>
    </CheckLogin>
};
