import React, {useState} from 'react';
import './AuthorizationPage.scss';
import {ReactComponent as Icon} from './SVG/user_icon.svg';
import {ReactComponent as Key} from './SVG/key.svg';
import {ax} from "./Server/ax";
import {Link, useNavigate} from "react-router-dom";

const RegisterPage = ({hey}) => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState('');
    const [error, setError] = useState('');
    const [hasError, setHasError] = useState(false);
    const nav = useNavigate();
    const Authentication = () => {
        if (password === repeatPassword) {
            ax.post('/api/auth/signup', {
                email: email,
                username: username,
                password: password,
                roles: ['ROLE_USER'],
            }).then(
                r => {
                    if (r.status === 200) {
                        nav('/');
                    }
                }
            );
        } else {
            alert('Пароли не совпадают')
        }
    }

    return (
        <div className="main">
            <div className="wrapper">
                <div className="header">
                    <h3 className="sign-in">Sign up</h3>
                    <div className="button">
                        <Link to='/'>Authorization</Link>
                    </div>
                </div>
                <div className="clear"></div>
                <form action="#">
                    <div>
                        <label className="user" htmlFor="text">
                            <Icon/>
                        </label>
                        <input value={username}
                               onChange={e => setUsername(e.target.value)} className="user-input" type="text"
                               name="name" id="name" placeholder="My name is"/>
                    </div>
                    <div>
                        <label className="user" htmlFor="text">
                            <Icon/>
                        </label>
                        <input value={email}
                               onChange={e => setEmail(e.target.value)}
                               className="user-input" type="email" name="name" id="name" placeholder="email"/>
                    </div>
                    {hasError && <div className="error">{error}</div>}
                    <div>
                        <label className="lock" htmlFor="password">
                            <Key/>
                        </label>
                        <input type="password" value={password} onChange={e => setPassword(e.target.value)} name="name"
                               id="name" placeholder=""/>
                    </div>
                    <div>
                        <label className="lock" htmlFor="password">
                            <Key/>
                        </label>
                        <input type="password" value={repeatPassword} onChange={e => setRepeatPassword(e.target.value)}
                               name="name" id="name" placeholder=""/>
                    </div>
                    <div className="button-control">
                        <button className="button2" onClick={Authentication()} disabled={hasError}>
                            Sign up
                        </button>
                    </div>
                    <div className="clear"></div>
                </form>
            </div>
        </div>
    );
};

export default RegisterPage;