import './App.css';
import Navi from './layouts/Navi';
import Dashboard from './layouts/Dashboard';
import { Container } from 'semantic-ui-react';
import React from 'react';
import 'semantic-ui-css/semantic.css';
import axios from 'axios'


function App() {
  axios.defaults.headers.common = {
    'Authorization': 'Bearer ' + localStorage.getItem('token')
};

  return (
    <div className="App">
      <Navi></Navi>
      <Container className = "main">
        <Dashboard></Dashboard>
      </Container>
    </div>
  );
}
export default App;
