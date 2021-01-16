import './App.scss';

import {BrowserRouter as Router, Link, Route, Switch, NavLink} from "react-router-dom";
import CarCollection from "./screens/CarCollection";
import AddCar from "./screens/AddCar";
import AnnualCosts from "./screens/AnnualCosts";

import './components/TopNavigation.scss'

function App() {

  return (
    <div className="App">
      <Router>
        <div>
          <nav className="top-navigation">
            <ul>
              <li>
                <NavLink activeClassName="active" exact={true} to="/">Catalogue</NavLink>
              </li>
              <li>
                <NavLink activeClassName="active" exact={true} to="/add-car">Add car</NavLink>
              </li>
              <li>
                <NavLink activeClassName="active" exact={true} to="/annual-costs">Annual costs</NavLink>
              </li>
            </ul>
          </nav>

          <Switch>
            <Route path="/annual-costs">
              <AnnualCosts />
            </Route>
            <Route path="/add-car">
              <AddCar />
            </Route>
            <Route path="/">
              <CarCollection />
            </Route>
          </Switch>
        </div>
      </Router>
    </div>
  );
}

export default App;
