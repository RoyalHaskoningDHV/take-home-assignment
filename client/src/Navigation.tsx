import Alert from "./components/Alert";
import {BrowserRouter as Router, NavLink, Route, Switch} from "react-router-dom";
import AnnualCosts from "./screens/AnnualCosts";
import AddCar from "./screens/AddCar";
import CarCollection from "./screens/CarCollection";
import {useAlert} from "./providers/AlertProvider";

export default function Navigation() {
    const {alertProps} = useAlert()

    return (
        <>
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
            {alertProps && <Alert {...alertProps} />}
        </>
    )
}