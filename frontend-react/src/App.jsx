import './App.css';
import React, { useState, useEffect } from 'react';
import Moment from 'moment';
import 'bootstrap/dist/css/bootstrap.min.css';
import {environment} from './environment';
import { Container, Row, Col, Button } from 'react-bootstrap';
import CurrencyChart from "./components/currencyChart";
import { findCurrencyData, findCurrencyExchangeRate, findHistoricalData, updateExchange, getPanelData } from "./service/service.api";

function App() {
  const [serie, setSerie] = useState(null);
  const [serieMax, setSerieMax] = useState(null);
  const [serieMin, setSerieMin] = useState(null);

  const [valueIn, setValueIn] = useState('');
  const [valueOut, setValueOut] = useState('');

  const [valueAddIn, setValueAddIn] = useState('');
  const [valueAddInError, setValueAddInError] = useState('');
  const [valueAddOut, setValueAddOut] = useState('');
  const [valueAddOutError, setValueAddOutError] = useState('');

  const [currencies, setCurrencies] = useState([]);
  const [currencyFrom, setCurrencyFrom] = useState('USD');
  const [currencyTo, setCurrencyTo] = useState('EUR');
  
  const [exchangeRate, setExchangeRate] = useState(null);
  const [exchangeRateDate, setExchangeRateDate] = useState(null);
  const [exchangeRateTime, setExchangeRateTime] = useState('FX_INTRADAY');
  const [exchangeRateRef, setExchangeRateRef] = useState('FIVE_MINUTES');

  const [exchangePoolList, setExchangePoolList] = useState([]);
  const [exchangePoolJobList, setExchangePoolJobList] = useState([]);
  const [exchangePoolJobForcedList, setExchangePoolJobForcedList] = useState([]);
  const [historicalPoolList, setHistoricalPoolList] = useState([]);
  const [historicalPoolJobList, setHistoricalPoolJobList] = useState([]);

  const [warning, setWarning] = useState('');
  const [warningAdd, setWarningAdd] = useState('');
  const [warningAddType, setWarningAddType] = useState('success');
  
  const [refreshTime, setRefreshTime] = useState(10);

  let publicAPI = "Public API >>>>> PUT "+ environment.getApi('rates') +"?from=USD&to=BRL";

  Moment.locale('en');

  function loadCurrencies(){
    findCurrencyData()
    .then(response => {
      if(response !== undefined && response.currencies !== undefined && response.currencies !== null){
        let data = response.currencies;
        setCurrencies([...data]);
      }
    });
  }
  
  function getCurrencyName(key){
    let cur = currencies.find((currency) => { return currency.key === key; });
    
    if(cur !== null && cur !== undefined){
      return cur.currencyName;
    }
    return "";
  }
  
  function loadPanelData(){
    getPanelData()
    .then(response => {
      if(response !== undefined && response !== null){
        setExchangePoolList([...response.exchangePoolList]);
        setExchangePoolJobList([...response.exchangePoolJobList]);
        setExchangePoolJobForcedList([...response.exchangePoolJobForcedList]);
        setHistoricalPoolList([...response.historicalPoolList]);
        setHistoricalPoolJobList([...response.historicalPoolJobList]);
        setWarningAdd('');
      }
    });
  }

  useEffect(() => {
    const intervalId = setInterval(() => {
      let i = refreshTime-1;
      if(i < 0){
        loadPanelData();
        i = 10;
      }
      setRefreshTime(i);
    },1000)

    return () => clearInterval(intervalId); 
  }, [refreshTime]);

  useEffect(() => {
    loadCurrencies();
  }, []);

  useEffect(() => {
    async function loadHistoricalData(){
      let request = {
        functionRate: exchangeRateTime,
        refRate: exchangeRateRef,
        fromSymbol: currencyFrom,
        toSymbol: currencyTo
      }
      findHistoricalData(request)
      .then(response => {
        if(response === undefined){
          response = [];
        }
        if(response !== undefined && response.rates !== null && response.rates !== undefined && response.rates.length > 0){
          let data = [];
  
          response.rates.forEach(rate => {
            data.push(
              {
                name: (exchangeRateRef === "FIVE_MINUTES" ? Moment(new Date(rate.key)).format('DD hh:mm A') : 
                  (exchangeRateRef === "SIXTY_MINUTES" ? Moment(new Date(rate.key)).format('DD hh:mm A') : 
                    (exchangeRateRef === "DAILY" ? Moment(new Date(rate.key)).format('MMM DD') : 
                      (exchangeRateRef === "WEEKLY" ? Moment(new Date(rate.key)).format('MMM DD yyyy') : 
                        (exchangeRateRef === "MONTHLY" ? Moment(new Date(rate.key)).format('MMM yyyy') : 
                          Moment(new Date(rate.key)).format('MMM yyyy')
                        )
                      )
                    )
                  )
                ),
                value: parseFloat(rate.value),
                date: Moment(new Date(rate.key))
              }
            );
          });
  
          setSerie(data);
          setSerieMin(response.min.toString().length > 5 ? parseFloat(response.min).toFixed(4) : response.min);
          setSerieMax(response.max.toString().length > 5 ? parseFloat(response.max).toFixed(4) : response.max);
          setWarning('');
          
        } else {
          if(response.note !== undefined && response.note !== null && response.note !== ""){
            setWarning(response.note);
          } else {
            response = [];
          }
        }
      });
    }

    async function getCurrencyExchangeRate(){
      let request = {
        from: currencyFrom,
        to: currencyTo
      }
      findCurrencyExchangeRate(request)
      .then(response => {
        if(response !== undefined){
          if(response.note !== null && response.note !== undefined || response.note !== ""){
            setWarningAddType("error");
            setWarningAdd(response.note);
          }else if(response.currencyExchange !== undefined){
              setExchangeRate(response.currencyExchange);
              setExchangeRateDate(response.currencyExchange.lastRefreshed);
          }
        }
      });
    }

    async function consume(){
      if(currencyFrom !== undefined && currencyFrom !== null && currencyTo !== undefined && currencyTo !== null){
        getCurrencyExchangeRate();
      }
    }

    if (exchangeRateRef && currencyFrom && currencyTo) {
      loadHistoricalData();
      consume();
    }
  }, [exchangeRateRef, currencyFrom, currencyTo]);

  useEffect(() => {
    async function exchangeIn(){
      if(exchangeRate !== undefined && exchangeRate !== null){
        let result = '';
        let rate = exchangeRate.exchangeRate;
        if(valueIn !== undefined && valueIn !== null && valueIn !== ''){
          result = parseFloat(valueIn * rate);
          if(result.toString().length > 6){
            setValueOut(parseFloat(result.toFixed(4)));
          }else{
            setValueOut(result);
          }
        }else{
          if(valueOut !== ''){
            setValueOut('');
          }
        }
      }
    }
    exchangeIn();
  }, [valueIn, exchangeRate]);
  
  useEffect(() => {
    async function exchangeOut(){
      if(exchangeRate !== undefined && exchangeRate !== null){
        let result = '';
        let rate = exchangeRate.exchangeRate;
        if(valueOut !== undefined && valueOut !== null && valueOut !== ''){
          result = parseFloat(valueOut / rate);
          if(result.toString().length > 6){
            setValueIn(parseFloat(result.toFixed(4)));
          }else{
            setValueIn(result);
          }
        }else{
          if(valueIn !== ''){
            setValueIn('');
          }
        }
      }
    }
    exchangeOut();
  }, [valueOut]);

  useEffect(() => {
    setValueAddInError('');
  }, [valueAddIn]);

  useEffect(() => {
    setValueAddOutError('');
  }, [valueAddOut]);

  function addNewExchange(){
    let valid = true;
    if(valueAddIn === ''){
      valid = false;
      setValueAddInError('Required Field!');
    }
    if(valueAddOut === ''){
      valid = false;
      setValueAddOutError('Required Field!');
    }
    if(valid){
      let request = {
        from: valueAddIn,
        to: valueAddOut
      }
      updateExchange(request)
      .then(response => {
        if(response !== undefined && response.success !== null && response.success !== undefined && response.success !== ''){
          setWarningAddType("success");
          setWarningAdd(response.success);
          setValueAddIn("");
          setValueAddOut("");
        } else {
          if(response !== undefined && response.error !== null && response.error !== undefined && response.error !== ""){
            setWarningAddType("error");
            setWarningAdd(response.error);
          } else {
            response = [];
          }
        }
      });
    }
  }

  let showDate = "";
  if(exchangeRateDate !== null){
    showDate = Moment(new Date(exchangeRateDate)).format('MMM DD, hh:mm A') + ' UTC - Disclaimer';
  }

  let valueConvertedIn = valueIn !== '' ? parseFloat(valueIn).toFixed(2) + " " + getCurrencyName(currencyFrom) + " equals" : "Enter a number!";
  let valueConvertedOut = valueOut !== '' ? parseFloat(valueOut).toFixed(2) + " " + getCurrencyName(currencyTo) : "";


  let exchangePoolListCol = [];
  exchangePoolList.forEach(it => {
    exchangePoolListCol.push(
      <Row key={`exchangePoolList_r`+it} className="contaier-row">
        <Col key={`exchangePoolList_c`+it} className="contaier-col">
          {it}
        </Col>
      </Row>
    )
  });
  
  let exchangePoolJobListCol = [];
  exchangePoolJobList.forEach(it => {
    exchangePoolJobListCol.push(
      <Row key={`historicalPoolList_r`+it} className="contaier-row">
        <Col key={`historicalPoolList_c`+it} className="contaier-col">
          {it}
        </Col>
      </Row>
    )
  });
  
  let exchangePoolJobForcedListCol = [];
  exchangePoolJobForcedList.forEach(it => {
    exchangePoolJobForcedListCol.push(
      <Row key={`historicalPoolList_r`+it} className="contaier-row">
        <Col key={`historicalPoolList_c`+it} className="contaier-col">
          {it}
        </Col>
      </Row>
    )
  });
  
  let historicalPoolListCol = [];
  historicalPoolList.forEach(it => {
    historicalPoolListCol.push(
      <Row key={`historicalPoolList_r`+it} className="contaier-row">
        <Col key={`historicalPoolList_c`+it} className="contaier-col">
          {it}
        </Col>
      </Row>
    )
  });
  
  let historicalPoolJobListCol = [];
  historicalPoolJobList.forEach(it => {
    historicalPoolJobListCol.push(
      <Row key={`historicalPoolJobList_r`+it} className="contaier-row">
        <Col key={`historicalPoolJobList_c`+it} className="contaier-col">
          {it}
        </Col>
      </Row>
    )
  });

  return (
    <Container fluid="lg">
      <Row className="text-header-info"><Col></Col></Row>
      <Row>
        <Col className="text">
          <Row><Col className="text-header">{valueConvertedIn}</Col></Row>
          <Row><Col className="text-result">{valueConvertedOut}</Col></Row>
          <Row><Col className="text-date">{showDate}</Col></Row>
          <Row className="input-row">
            <Col md={4}>
              <input type="number" value={valueIn} onChange={e => setValueIn(e.target.value)} />
            </Col>
            <Col md={8}>
              <select value={currencyFrom} onChange={e => setCurrencyFrom(e.target.value)}>
                {currencies.map((obj, e) => ( 
                    <option key={'in_'+obj.key} value={obj.key}>{obj.key} - {obj.currencyName}</option>
                ))}
              </select>
            </Col>
          </Row>
          <Row>
            <Col md={4}>
              <input type="text" value={valueOut} onChange={e => setValueOut(e.target.value)} />
            </Col>
            <Col md={8}>
              <select value={currencyTo} onChange={e => setCurrencyTo(e.target.value)}>
                {currencies.map((obj, e) => ( 
                    <option key={'out_'+obj.key} value={obj.key}>{obj.key} - {obj.currencyName}</option>
                ))}
              </select>
            </Col>
          </Row>
        </Col>
        <Col>
          <Row>
            <Col>
              <Button variant="outline-light" className={`button ${exchangeRateRef === 'FIVE_MINUTES' ? 'button outline-primary' : 'button outline-light'}`} onClick={e => {setExchangeRateTime('FX_INTRADAY');setExchangeRateRef('FIVE_MINUTES');}}>1 D</Button>
            </Col>
            <Col>
              <Button variant="outline-light" className={`button ${exchangeRateRef === 'SIXTY_MINUTES' ? 'button outline-primary' : 'button outline-light'}`} onClick={e => {setExchangeRateTime('FX_INTRADAY');setExchangeRateRef('SIXTY_MINUTES');}}>5 D</Button>
            </Col>
            <Col>
              <Button variant="outline-light" className={`button ${exchangeRateRef === 'DAILY' ? 'button outline-primary' : 'button outline-light'}`} onClick={e => {setExchangeRateTime('FX_DAILY');setExchangeRateRef('DAILY');}}>1 M</Button>
            </Col>
            <Col>
              <Button variant="outline-light" className={`button ${exchangeRateRef === 'WEEKLY' ? 'button outline-primary' : 'button outline-light'}`} onClick={e => {setExchangeRateTime('FX_WEEKLY');setExchangeRateRef('WEEKLY');}}>1 A</Button>
            </Col>
            <Col>
              <Button variant="outline-light" className={`button ${exchangeRateRef === 'MONTHLY' ? 'button outline-primary' : 'button outline-light'}`} onClick={e => {setExchangeRateTime('FX_MONTHLY');setExchangeRateRef('MONTHLY');}}>5 A</Button>
            </Col>
            <Col>
              <Button variant="outline-light" className={`button ${exchangeRateRef === 'YEARLY' ? 'button outline-primary' : 'button outline-light'}`} onClick={e => {setExchangeRateTime('FX_MONTHLY');setExchangeRateRef('YEARLY');}}>Máx</Button>
            </Col>
          </Row>
          <Row>
            <Col>
              <CurrencyChart data={serie} serieMin={serieMin} serieMax={serieMax} /> 
            </Col>
          </Row>
        </Col>
      </Row>
      <Row className="text-footer-info"><Col>Data provided by 'Alpha Vantage API' and 'The Free Currency Converter API' for Currency Exchange</Col></Row>
      <Row className="text-footer-warning"><Col>{warning}</Col></Row>

      <Row className="panel panel-header">
        <Col md={7}>
          Dashboard Redis
        </Col>
        <Col md={5}>
          Refreshing in {refreshTime < 10 ? "0"+refreshTime : refreshTime} seconds
        </Col>
      </Row>
      <Row className="panel-exchange">
        <Col className="panel-col" title="Every 15 minutes this list is sent to the update pool.">
          Saved Currency Exchange Data 
          <Row className="panel-sub-row">
            <Col className="panel-sub-col">
              {exchangePoolListCol}
            </Col>
          </Row>
        </Col>
        <Col className="panel-col" title="Every 5 minutes, the first item is updated and removed from the list.">
          Currency Exchange Data (Update Pool)
          <Row className="panel-sub-row">
            <Col className="panel-sub-col">
              {exchangePoolJobListCol}
            </Col>
          </Row>
        </Col>
        <Col className="panel-col-last" title="Every minute, the first item is updated and removed from the list.">
          Currency Exchange Data (Insertion Pool)
          <Row className="panel-sub-row-insert">
            <Col className="panel-sub-col-insert">
              {exchangePoolJobForcedListCol}
            </Col>
          </Row>
        </Col>
      </Row>
      <Row className="panel-msg">
        <Col className={warningAddType}>
            {warningAdd}
        </Col>
      </Row>
      <Row className="panel-historical">
        <Col className="panel-col" title="Every hour this list is sent to the update pool.">
          Saved Historical Data - Chart
          <Row className="panel-sub-row">
            <Col className="panel-sub-col">
              {historicalPoolListCol}
            </Col>
          </Row>
        </Col>
        <Col className="panel-col" title="Every 20 minutes, the first item is updated and removed from the list.">
          Historical Data - Chart (Update Pool)
          <Row className="panel-sub-row">
            <Col className="panel-sub-col">
              {historicalPoolJobListCol}
            </Col>
          </Row>
        </Col>
        <Col className="panel-col-last" title="Enter a new event in the insertion pool.">
          Add New Currency Exchange Data
          <Row className="panel-sub-row-add">
            <Col className="panel-sub-col-add" md={2}>
              From
            </Col>
            <Col className="panel-sub-col-add" md={3}>
              <input type="text" className={valueAddInError ? "error-input" : ""} value={valueAddIn} onChange={e => setValueAddIn(e.target.value)} />
            </Col>
            <Col className="panel-sub-col-add error" md={7}>
              {valueAddInError}
            </Col>
          </Row>
          <Row className="panel-sub-row-add">
            <Col className="panel-sub-col-add" md={2}>
              To
            </Col>
            <Col className="panel-sub-col-add" md={3}>
              <input type="text" className={valueAddInError ? "error-input" : ""} value={valueAddOut} onChange={e => setValueAddOut(e.target.value)} />
            </Col>
            <Col className="panel-sub-col-add error" md={7}>
            {valueAddOutError}
            </Col>
          </Row>
          <Row className="panel-sub-row-add">
            <Col className="panel-sub-col-add-last">
              <Button onClick={e => addNewExchange()}>Insert Currency Exchange</Button>
            </Col>
          </Row>
        </Col>
      </Row>
      <Row className="panel-row-api">
        <Col className="panel-col-api">
          {publicAPI}
        </Col>
      </Row>
    </Container>
  );
}

export default App;