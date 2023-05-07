import './App.css';
import AuthorizationPage from "./Components/AuthorizationPage";
import {Route, Routes} from "react-router-dom";
import RegisterPage from "./Components/RegisterPage";
import {useEffect, useState} from "react";
import {ax} from "./Components/Server/ax";
import MainPage from "./Components/MainPage";


function App() {
    const [auth, setAuth] = useState(false);
    useEffect(
        () => {
            ax.get('/api/test/all').then(
                r=> {
                    if(r.status === 200){
                        setAuth(true);
                    }
                }
            )
        },[]
    )
    return (
        <div className="App">
            {
                auth ?

                    <Routes>
                        <Route path='/' element={<MainPage/>}/>
                    </Routes>

                    :
                    <Routes>
                        <Route path="/" element={<AuthorizationPage/>} />
                        <Route path='/register' element={<RegisterPage/>}/>
                    </Routes>
            }

        </div>
    );
}

export default App;
