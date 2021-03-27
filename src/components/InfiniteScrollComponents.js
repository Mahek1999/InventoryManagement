import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import axios from "axios";
import clsx from 'clsx';
import { CircularProgress } from "@material-ui/core";
import InfiniteScroll from "react-infinite-scroll-component";
import { lighten, makeStyles } from '@material-ui/core/styles';
import RenderTableComponent from './RenderTableComponent';

const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
    }
}));

export default function InfiniteScrollComponents(props) {

    const classes = useStyles();
    const [responseData, setResponseData] = useState([]);
    const [isNext, setIsNext] = useState(false);
    const [pageCount, setPageCount] = useState(0);

    useEffect(() => {
        console.log("InfiniteScroll has rendered with pageCount : "+pageCount);
        const getData = async () => {
            console.log(responseData);
            const response = await axios.get(`http://localhost:8080/1805456/getAllRecordsByPage?pageCount=${pageCount}&limit=5`)
            setResponseData([...responseData, ...response.data]);
            setIsNext(true);
        }
        getData();
    }, [pageCount]
    );

    function fetchMoreData() {
            setPageCount(pageCount + 1);
      
    }

    return (
        <div className={classes.root}>
            <InfiniteScroll
                dataLength={responseData.length} 
                next={fetchMoreData}
                hasMore={isNext}
                scrollableTarget="scrollableDiv"
                loader = 
                {
                    <div style={{ paddingLeft: "50%", overflow: "hidden", paddingTop: '10px' }}>
            <CircularProgress color="primary" className={classes.circularprogess} />
            <h6 style={{ color: '#C0C6CA' }}>Loading</h6>
           </div>
        
                }
            >
                <RenderTableComponent responseData={responseData} getSelectedRecord={props.getSelectedRecord}/>
           
            </InfiniteScroll>
        </div>
    );
}

