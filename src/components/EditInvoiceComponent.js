import React, {useEffect} from 'react';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import { Grid, TextField, OutlinedInput, Button, makeStyles,InputLabel } from '@material-ui/core';
import axios from 'axios';

export default function EditInvoiceComponent(props) {

    const source = {
        docId : props.selectedData[0],
        totalOpenAmount:'',
        notes:''

    }
     
    const setSource = (value) =>
    {
        Object.assign(source,value);
    }
   

    const handleClose = () => {
        props.handleEditClose();
    }
    const handleSubmit = (event) =>
    {
        event.preventDefault();

        const data = new FormData(event.target);

        setSource({...source, totalOpenAmount:data.get("totalOpenAmount"), notes : data.get("notes")})

        axios.put('http://localhost:8080/1805456/editInvoiceRecord', JSON.stringify(source)
        ).then((response) => {
            alert("Record Succesfully Edited");
            handleClose();
        }, (error) => {
            console.log(error);
        });

        window.location.reload(true);
    }
    const handleReset = () =>
    {
        Array.from(document.querySelectorAll("input")).forEach(
            input => (input.value = "")
        );
    }
 
    const classes = useStyles();
    return (

        <div>
            <Dialog PaperProps={{
                classes: {
                    root: classes.paper
                }
            }} open={true} onClose={handleClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Edit Invoice</DialogTitle>
                <DialogContent>
                    <DialogContentText style={{ color: '#C0C6CA' }}>
                        <form id="addInvoiceForm" onSubmit={handleSubmit} className={classes.root}>
                            <Grid container direction = "column"  spacing = {2}>
                            <Grid item>
                                <Grid container direction = "row" alignItems = "center">
                                        <Grid item xs={4}>
                                            <InputLabel className={classes.labelRoot} required>Invoice Amount </InputLabel>
                                    </Grid>
                                    <Grid item xs={8}>
                                        <TextField
                                            variant="outlined"
                                            type="number"
                                            name="totalOpenAmount"
                                            id = "totalOpenAmount"
                                        />
                                    </Grid>
                                </Grid>
                                </Grid>
                                <Grid item>
                                <Grid container direction = "row" alignItems = "center">
                                    <Grid item xs={4}>
                                            <InputLabel className={classes.labelRoot}>Notes</InputLabel>
                                </Grid>
                                <Grid item xs={8}>
                                    <TextField
                                        variant="outlined"
                                        type="text"
                                        name="notes"
                                        id = "notes"
                                    />
                                </Grid>
                                    </Grid>
                                </Grid>
                            </Grid>
                        </form>
          </DialogContentText>
                </DialogContent>
                <DialogActions>
                <Button onClick = {handleClose} color = "primary">
                    Cancel
                </Button>
                    <Button onClick={handleReset} color="primary">
                        Reset
          </Button>
                    <Button  type="submit" form="addInvoiceForm" className={classes.submitButton} >
                        Save
          </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

const useStyles = makeStyles(
    {
        paper:
        {
            backgroundColor: '#2A3E4C',
            background: 'transparent 0% 0 % no - repeat padding- box',
            boxShadow: '0px 8px 24px #00000029',
            color: 'white',
            borderRadius: '4px',
            opacity: '1',
            minWidth: '35vw',
            minHeight: '25vh'
        },

        submitButton:
        {
            backgroundColor: '#14AFF1',
            background: 'transparent 0% 0 % no - repeat padding- box',
            borderRadius: '10px',
            opacity: '1'
        }
    ,

    root:
    {
        '& .MuiFormControl-root': { // & refers to parent and .MuiFormControl-root will apply to all field 
            width: '80%',
            color: 'white',
        },
        '& .MuiInputLabel-root':
        {
            textAlign: 'left',
            color: '#97A1A9',
            opacity: '1',
        }
        ,
        '& .MuiInputLabel-asterisk':
        {
            color: 'red'
        }
    }
}
    
);