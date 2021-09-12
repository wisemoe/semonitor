import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import WebServiceList from './components/WebServiceList.js'
import WebServiceShow from './components/WebServiceShow.js'
function App() {
     return (
         <div >
             <Router>
                 <div>
                     <Switch>
                         <Route path="/web_service/:id" component={WebServiceShow} />
                         <Route path="/" component={WebServiceList} />
                     </Switch>
                 </div>

             </Router>
         </div>
     );
 };

export default App;
