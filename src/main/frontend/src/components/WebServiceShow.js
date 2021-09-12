import React from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

import StatusFailChart from './StatusFailChart.js'

export default class WebServiceShow extends React.Component {
  state = {
    web_service: {
      name: "",
      url: "",
      statuses:[]
    },
    fail_statuses: []
  };

  componentDidMount() {
    axios.get(`http://localhost:8080/api/webServices/${this.props.match.params.id}`)
      .then(res => {
        const web_service = res.data;
        let fail_statuses = web_service.statuses.filter(k => k.status === 'FAIL');
        fail_statuses.forEach((k) => {
          k.status = 1
          k.time = new Date(k.time).toLocaleString()
        })
        this.setState({ web_service });
        this.setState({ fail_statuses });
      }).catch(err => console.log(err));
  }

  render() {
    return (
      <div>
        <Link to={"/"}>Back</Link>
        <ul>
          <li>Name: {this.state.web_service.name}</li>
          <li>URL: <a href={this.state.web_service.url}>{this.state.web_service.url}</a></li>
          <li>FAILs Chart: <StatusFailChart data={this.state.fail_statuses}></StatusFailChart></li>
        </ul>
      </div>
    )
  }
}
