import './App.css';
import React, { useState, useEffect } from 'react';
import Moment from 'moment';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Container, Row, Col } from 'react-bootstrap';
import CurrencyChart from "./components/currencyChart";
import { findHistoricalData } from "./service/service.api";

function App() {
  const [serie, setSerie] = useState(null);
  const [serieMax, setSerieMax] = useState(null);
  const [serieMin, setSerieMin] = useState(null);
  // const [metaData, setMetaData] = useState(null);

  Moment.locale('en');

  useEffect(() => {
    findHistoricalData()
    .then(response => {
      if(response === undefined){
        response = [];
      }
      let data = [];
      let dataMax = -1;
      let dataMin = 9999;
      if(response !== undefined && response.error === undefined){
        // setMetaData(response["Meta Data"]);

        Object.keys(response["Time Series FX (Daily)"]).forEach((key) => {
          if((response["Time Series FX (Daily)"][key])["4. close"] > dataMax){
            dataMax = (response["Time Series FX (Daily)"][key])["4. close"];
          }
          if((response["Time Series FX (Daily)"][key])["4. close"] < dataMin){
            dataMin = (response["Time Series FX (Daily)"][key])["4. close"];
          }

          data.push(
            {
              name: Moment(new Date(key)).format('MMM-DD'),
              value: (response["Time Series FX (Daily)"][key])["4. close"],
              date: Moment(new Date(key))
            }
          );
        });

        // console.log(data);
        data.sort(function (a, b) {
          return a.date < b.date ? -1 : 1;
        });

        setSerie(data);
        setSerieMax(dataMax);
        setSerieMin(dataMin);
        
      } else {
        if(response.errors === undefined){ 
          response = [];
        } else {
          response = [];
        }
      }
    });
  }, []);

  // console.log("Meta Data:");
  // console.log(metaData);
  // console.log("Time Series FX (Daily):");
  // if(serie != null){
  //   console.log(serie);
  // }

  return (
    <Container fluid="lg">
      <Row>
        <Col className="text">
          <Row><Col className="text-header">1 United States Dollar equals</Col></Row>
          <Row><Col className="text-result">0.84 Euro</Col></Row>
          <Row><Col className="text-date">Nov 9, 11:37 AM UTC - Disclaimer</Col></Row>
          <Row><Col>text field</Col>select<Col></Col></Row>
          <Row><Col>text field</Col>select<Col></Col></Row>
        </Col>
        <Col>
          <CurrencyChart data={serie} serieMax={serieMax} serieMin={serieMin} /> 
        </Col>
      </Row>
      <Row><Col className="text-footer">Data provided by Morningstar for Currency and Coinbase for Criptocurrency</Col></Row>
    </Container>
    
  );
}

export default App;