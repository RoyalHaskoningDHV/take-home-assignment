import './App.scss';

import './components/TopNavigation.scss'
import {AlertProvider} from "./providers/AlertProvider";
import Navigation from "./Navigation";

function App() {

  return (
      <AlertProvider>
        <Navigation />
      </AlertProvider>
  );
}

export default App;
