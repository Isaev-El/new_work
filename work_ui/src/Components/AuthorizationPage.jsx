import React, {useState} from 'react';
import './AuthorizationPage.scss';
import {ReactComponent as Icon} from './SVG/user_icon.svg';
import {ReactComponent as Key} from './SVG/key.svg';
import {ax} from "./Server/ax";
import {Link} from "react-router-dom";

const AuthorizationPage = ({hey}) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const Authentication = (e) => {

        e.preventDefault();
        ax.post('/api/auth/signin', {username, password}).then(
            r => {
                if(r.status === 200){
                    localStorage.setItem('accessToken', r.data.accessToken);
                    window.location.reload();
                }
                else{
                    alert('Bad credentials');
                }
            }
        )

    }
    return (
        <div className="main">

            <div className="wrapper">
                <div className="header">
                    <h3 className="sign-in">Sign in</h3>
                    <div className="button">
                        <Link to='/register'>Register</Link>
                    </div>
                </div>
                <div className="clear"></div>
                <form action="#">
                    <div>
                        <label className="user" htmlFor="text">
                            <Icon/>
                        </label>
                        <input value={username} onChange={e => setUsername(e.target.value)} className="user-input" type="text" name="name" id="name" placeholder="My name is"/>
                    </div>
                    <div>
                        <label className="lock" htmlFor="password">
                            <Key/>
                        </label>
                        <input type="password" value={password} onChange={e => setPassword(e.target.value)} name="name" id="name" placeholder=""/>
                    </div>
                    <div className="button-control">
                        <button className="button2" onClick={e => Authentication(e)}>
                            Sing in
                        </button>
                    </div>

                    <div className="clear"></div>
                </form>
            </div>


        </div>
    );
};

export default AuthorizationPage;