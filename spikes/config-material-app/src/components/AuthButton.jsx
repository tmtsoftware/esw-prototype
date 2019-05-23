import React, {useContext} from "react";
import CircularProgress from "@material-ui/core/CircularProgress";
import {AuthContext} from "csw-aas-js";
import Button from "@material-ui/core/Button";

const LoginButton = () => {
  const {login} = useContext(AuthContext);
  return <Button onClick={login} variant={'contained'} color="default">Login</Button>
};

const LogoutButton = () => {
  const {logout} = useContext(AuthContext);
  return <Button onClick={logout} variant={'contained'} color="default">Logout</Button>
};

const AuthButton = () => {

  const { auth } = useContext(AuthContext);

  if(auth == null || typeof auth === "undefined")
    return <CircularProgress color={'inherit'}  />;
  else if(auth.isAuthenticated())
    return <LogoutButton />;
  else
    return <LoginButton />;
};

export default AuthButton
