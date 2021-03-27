
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import theme, { pxToVh } from '../utils/theme';
import { InputLabel, makeStyles, Paper } from '@material-ui/core';
import {Button } from '@material-ui/core'
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Snackbar from '@material-ui/core/Snackbar'; 
import WarningIcon from '@material-ui/icons/Warning';

export default function AddInvoiceComponent(props){


    // const [nameCustomer, setCustomerName] = useState("");
    // const[custNumber,setCustNumber] = useState(0);
    // const [invoiceId, setInvoiceId] = useState(0);
    // const[dueInDate, setDueInDate] = useState('');
    // const[invoiceAmount,setInvoiceAmount] = useState(0) ;
    // const[notes, setNotes] = useState("");
    const [ openSnackbar, setSnackBarOpen] = useState(false);
    const  handleSubmit = (event) => {

        event.preventDefault();
        const data = new FormData(event.target);
        const value = Object.fromEntries(data.entries());
        var isValidated = validateForm(value);
        if(isValidated)
         sendToServer(value);
        else
        setSnackBarOpen(true);
    }

    function sendToServer(values)
    {
        var data = JSON.parse(JSON.stringify(values));
        axios.post('http://localhost:8080/1805456/insertInvoiceRecord', data
           
        )
            .then((response) => {
                console.log(response);
                window.location.reload(true);
            }, (error) => {
                console.log(error);
            });
        }
   
    const handleClose = () =>
    {
        setSnackBarOpen(false);
    }
    function validateForm(values)
    {
        // var values = JSON.parse(jsonValue);
        console.log(typeof(values.invoiceId))
        if( values.invoiceId === "" || values.nameCustomer==="" || values.custNumber ==="" || values.dueInDate === "" || values.totalOpenAmount === "")
         return false;

         return true;
    }
    const closeDialog = () =>
    {
        props.handleCloseProp();
    }

    const classes = useStyles();
    return (

        <div>

            <Snackbar
                ContentProps={{
                    classes: {
                        root: classes.snackRoot
                    }
                }}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'left',
                }}
                open={openSnackbar}
                autoHideDuration={1500}
                onClose={handleClose} 
                message={<div><WarningIcon style = {{color:'red'}} size="small"/> <span>  Mandatory Field cant be empty</span></div>}
            />
            <Dialog PaperProps={{
                classes: {
                    root: classes.paper
                }
            }}  open = {true} onClose={closeDialog} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title" style={{ color: 'white' }}>Add Invoice</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        <form id = "addInvoiceForm" onSubmit = {handleSubmit} className={classes.root}>
                            <Grid container >

                                <Grid item xs={6} >
                                    <Grid container direction="row" alignItems="center">
                                        <Grid item xs={4}>
                                            <InputLabel className={classes.labelRoot} required> Customer Name </InputLabel>
                                        </Grid>
                                        <Grid item xs={6}>
                                            <TextField
                                                variant="outlined"
                                                type = "text"
                                                name="nameCustomer"
                                            />
                                        </Grid>
                                    </Grid>
                                    <Grid container direction="row" alignItems="center">
                                        <Grid item xs={4}>
                                            <InputLabel className={classes.labelRoot} required> Customer Number</InputLabel>
                                        </Grid>

                                        <Grid item xs={6}>
                                            <TextField
                                                variant="outlined"
                                                type = "text"
                                                name="custNumber"
                                            />
                                        </Grid>
                                    </Grid>

                                    <Grid container direction="row" alignItems="center">
                                        <Grid item xs={4}>
                                            <InputLabel className={classes.labelRoot} required> Invoice Id</InputLabel>
                                        </Grid>

                                        <Grid item xs={6}>
                                            <TextField
                                                variant="outlined"
                                                type = "number"
                                                name="invoiceId"
                                                id = "invoiceId"
                                            />
                                        </Grid>
                                    </Grid>

                                    <Grid container direction="row" alignItems="center">
                                        <Grid item xs={4}>
                                            <InputLabel className={classes.labelRoot} required>Invoice Amount</InputLabel>
                                        </Grid>

                                        <Grid item xs={6}>
                                            <TextField
                                                variant="outlined"
                                                type = "number"
                                                name="totalOpenAmount"
                                            />
                                        </Grid>
                                    </Grid>
                                </Grid>
                                <Grid item xs={6} >
                                    <Grid container direction="column" alignItems="center">
                                        <Grid item>
                                            <Grid container direction="row" alignItems="center" >
                                                <Grid item xs={4}>
                                                    <InputLabel required>Due in Date</InputLabel>
                                                </Grid>

                                                <Grid item xs={8}>
                                                    <TextField
                                                        variant="outlined"
                                                        type="date"
                                                        name="dueInDate"
                                                    />
                                                </Grid>
                                            </Grid>
                                            <Grid container direction="row" alignItems="center">
                                                <Grid item xs={4}>
                                                    <InputLabel className={classes.labelRoot} >Notes</InputLabel>
                                                </Grid>

                                                <Grid item xs={8}>
                                                    <TextField
                                                        variant="outlined"
                                                        name="notes"
                                                        type="text"
                                                        style={{
                                                            opacity: '1'
                                                        }}
                                                    />
                                                </Grid>
                                            </Grid>
                                        </Grid>
                                    </Grid>
                                </Grid>
                            </Grid>
                        </form>
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={closeDialog} color="primary">
                        Cancel
          </Button>
                    <Button disabled={openSnackbar} type = "submit"  form = "addInvoiceForm" className = {classes.submitButton} >
                        Add
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
            borderRadius: '4px',
            opacity: '1',
            minWidth: '72vw',
            minHeight: '75vh'
        },
        root:
        {
            '& .MuiFormControl-root': { // & refers to parent and .MuiFormControl-root will apply to all field 
                width: '80%',
                margin: theme.spacing(1),
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
        ,
        },
            submitButton:
            {
                backgroundColor:'#14AFF1',
                background: 'transparent 0% 0 % no - repeat padding- box',
        borderRadius: '10px',
        opacity: '1'
            },
            snackRoot:
            {
                backgroundColor:'#21303B',
            }
        },
    
);