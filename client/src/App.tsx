import './App.scss';

import {BrowserRouter as Router, Route, Switch, NavLink} from "react-router-dom";
import CarCollection from "./screens/CarCollection";
import AddCar from "./screens/AddCar";
import AnnualCosts from "./screens/AnnualCosts";

import './components/TopNavigation.scss'

function App() {

  return (
    <div className="App">
      <Router>
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

        <div className="route-content">
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
