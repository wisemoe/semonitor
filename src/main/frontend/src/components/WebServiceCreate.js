import React from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

export default class WebServiceCreate extends React.Component {
  state = {
    name: '',
    url: ''
  }

  createNewWebService = event => {
    event.preventDefault();
    const data = {
        name: this.state.name,
        url: this.state.url
      };
    axios.post('http://localhost:8080/api/webServices', data).then(res => {
        window.location.reload()
      }).catch(err => console.log(err));
  }

  handleInputChange = event => {
    const target = event.target;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const name = target.name;
    this.setState({
      [name]: value    });
  }

  render() {
    return (
      <form onSubmit={this.createNewWebService}>
        <label>
          Name:
          <input type="text" name="name" onChange={this.handleInputChange} />
        </label>
        <label>
          URL:
          <input type="text" name="url" onChange={this.handleInputChange} />
        </label>
        <input type="submit" value="Create" />
      </form>
    )
  }
}
