import React from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

import WebServiceCreate from './WebServiceCreate.js'

export default class WebServiceList extends React.Component {
  state = {
    web_services: []
  }

  componentDidMount() {
    axios.get(`http://localhost:8080/api/webServices`)
      .then(res => {
        const web_services = res.data;
        this.setState({ web_services });
      }).catch(err => console.log(err));
  }

  render() {
    return (
      <div>
        <WebServiceCreate></WebServiceCreate>
        <ul>
          { this.state.web_services.map((web_service, i) =>
            <li key={i}>
              <Link to={"./web_service/"+web_service._id}>{web_service.name}</Link><span> </span>
              <a href={web_service.url}>{web_service.url}</a>
            </li>
          )}
        </ul>
      </div>
    )
  }
}
