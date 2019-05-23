import React from 'react';
import * as PropTypes from 'prop-types';
import Grid from '@material-ui/core/Grid';
import ConfigPreview from "./ConfigPreview";
import {makeStyles} from "@material-ui/styles";

const useStyles = makeStyles({
  list: {
    padding: "10px"
  }
});

const ConfigCollection = props => {
  const {configs} = props;
  const classes = useStyles();
  return (
    <Grid className={classes.list} container justify="flex-start" spacing={2} >
      {configs.map(value => (
        <Grid item key={value.id} >
          <ConfigPreview config={value}/>
        </Grid>
      ))}
    </Grid>
  );
};

ConfigCollection.propTypes = {
  configs: PropTypes.array.isRequired
};

export default ConfigCollection
