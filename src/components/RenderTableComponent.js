import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableRow from '@material-ui/core/TableRow';
import TableHead from '@material-ui/core/TableHead';
import {Checkbox} from '@material-ui/core';
import {formatter} from '../utils/formatter';



const useStyles = makeStyles((theme) => ({
    tableContainer : 
    {
        maxHeight : 300
    },
    paper: {
        width: '100%',
        marginBottom: theme.spacing(2),

    },
    table: {
        minWidth: 750,
        border:'none',

    },
    tableBody:
    {
        '&:nth-of-type(odd)': {
            backgroundColor: theme.palette.primary.dark,
        },

    },
    checkBox:
    {
        color: '#97A1A9',
    },

    mainCheckBox: {
        padding: '0px 10px',
        color: '#97A1A9',
        margin : '0'
    },

    visuallyHidden: {
        border: 0,
        clip: 'rect(0 0 0 0)',
        height: 1,
        margin: -1,
        overflow: 'hidden',
        padding: 0,
        position: 'absolute',
        top: 20,
        width: 1,
    },
}));

export default function RenderTableComponent(props)
{
const classes = useStyles();
const [selected, setSelected] = React.useState([]);
const [isAllChecked,setIsAllChecked] = React.useState(false);

const handleSelectAllClick = (event) => {
        if (event.target.checked) {
            const newSelecteds = props.responseData.map((n) => n.docId);
            setSelected(newSelecteds);
            setIsAllChecked(true);
            props.getSelectedRecord(newSelecteds);
            return;
        }
        setSelected([]);
       props.getSelectedRecord([]);
        setIsAllChecked(false);
    };

    const handleClick = (event, name) => {
        const selectedIndex = selected.indexOf(name);
        let newSelected = [];
        if (selectedIndex === -1) {
            newSelected = newSelected.concat(selected, name);
        } else if (selectedIndex === 0) {
            newSelected = newSelected.concat(selected.slice(1));
        } else if (selectedIndex === selected.length - 1) {
            newSelected = newSelected.concat(selected.slice(0, -1));
        } else if (selectedIndex > 0) {
            newSelected = newSelected.concat(
                selected.slice(0, selectedIndex),
                selected.slice(selectedIndex + 1),
            );
        }

        setSelected(newSelected);
        props.getSelectedRecord(newSelected);
        
    };

const isSelected = (name) => selected.indexOf(name) !== -1;
return(<div>
    <TableContainer className = {classes.tableContainer} id="scrollableId">
        <Table
            className={classes.table}
        >
            <TableHead >
                <TableRow >
                    <TableCell>
                        <Checkbox   color="secondary" className={classes.mainCheckBox}
                            type="checkbox"
                            id="selectAll"
                            checked={isAllChecked}
                            onChange={handleSelectAllClick}
                        />
                    </TableCell>
                    <TableCell>Customer Name</TableCell>
                    <TableCell align="right" className={classes.cell_id}>Customer#</TableCell>
                    <TableCell align="right">Bill Number</TableCell>
                    <TableCell align="right">Bill Amount</TableCell>
                    <TableCell align="right">Due Date</TableCell>
                    <TableCell align="right">Predicted Payment Date</TableCell>
                    <TableCell align="right">Delayed</TableCell>
                    <TableCell align="right">Notes</TableCell>
                </TableRow>
            </TableHead>
            <TableBody>
                {
                    props.responseData.map((data, index) => {
                        const isItemSelected = selected.length == data.length || isSelected(data.docId);
                        return (
                            <TableRow
                                className={classes.tableBody}
                                hover
                                role="checkbox"
                                tabIndex={-1}
                                key={index}
                            >
                                <TableCell padding="checkbox" >
                                    <Checkbox
                                        className={classes.checkBox}
                                        id={data.docId}
                                        onChange={(event) => handleClick(event, data.docId)}
                                        checked={isItemSelected}
                                    />
                                </TableCell>
                                <TableCell component="th" scope="row" padding="none" >
                                    {data.nameCustomer}
                                </TableCell>
                                <TableCell align="right">{data.custNumber}</TableCell>
                                <TableCell align="right">{data.invoiceId}</TableCell>
                                <TableCell align="right">{formatter(data.totalOpenAmount)}</TableCell>
                                <TableCell align="right">{data.dueInDate}</TableCell>
                                <TableCell align="right">--</TableCell>
                                <TableCell align="right">--</TableCell>
                                <TableCell align="right">{!data.notes ? "Lorem Ipsum Ipdor.." : data.notes}</TableCell>
                            </TableRow>
                        );
                    })}
            </TableBody>
        </Table>
       
    </TableContainer>
</div>);
}