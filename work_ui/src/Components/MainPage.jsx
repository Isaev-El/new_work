import React, {useEffect, useState} from 'react';
import {ax} from "./Server/ax";
import './MainPage.scss'
import {Button} from "react-bootstrap";
import {
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControl,
    FormControlLabel,
    FormLabel,
    RadioGroup,
    TextField
} from "@mui/material";

const MainPage = () => {
    const [students, setStudents] = useState([]);
    const [isOpen, setIsOpen] = useState(false);
    const [isDeleteOpen, setIsDeleteOpen] = useState(false);
    const [isUpdateOpen, setIsUpdateOpen] = useState(false);
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [load, setLoad] = useState(false);
    const [deleteId, setDeleteId] = useState("");
    const [updateId,setUpdateId] = useState("");

    const logout = () => {
        window.location.reload();
        localStorage.setItem('accessToken', '')
    }


    useEffect(() => {
            ax.get('/api/users/find-all').then(
                r => {
                    let temp = r.data;
                    temp.sort((a, b) => {
                        if (a.id < b.id) return ;
                        if (a.id > b.id) return 1;
                        return 0;
                    });
                    console.log(temp)
                    setStudents(temp);
                }
            );
        }, [load]
    );


    const deleteStudent = () => {
        ax.get(`/api/users/delete/${deleteId}`).then( r => {
            if(r.status === 200) {
                console.log("true")
            }
        });
        load ? setLoad(false) : setLoad(true)
        setDeleteId('');

        setIsDeleteOpen(false);
    }

    const updateStudent=()=>{
        ax.post(`/api/users/update/${updateId}`, {email: email, username: username})

        load ? setLoad(false) : setLoad(true)

        setUpdateId('');
        setEmail('');
        setUsername('');

        setIsUpdateOpen(false);
    }

    const handleRegister = () => {
        ax.post('/api/auth/signup', {email: email, username: username, password: password, roles: ['ROLE_USER'], }).then( r => {
                if (r.status === 200) {
                    load ? setLoad(false) : setLoad(true);

                }
            }
        );
        setEmail('');
        setUsername('');
        setPassword('');
        setIsOpen(false);
    };

    return (
        <div className="main-page">


            <button className={"buttonLog"} onClick={logout}><a>Logout</a></button>
            <div className="main-div">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                </tr>
                </thead>
                <tbody>
                {students.map(user => (
                    <tr key={user.id}>
                        <td>{user.id}</td>
                        <td>{user.username}</td>
                        <td>{user.email}</td>
                    </tr>
                ))}
                </tbody>
            </table>
                <button className={"buttonAdd"} onClick={() => setIsOpen(true)}><a>Добавить</a></button>
                <button className={"buttonDelete"} onClick={() => setIsDeleteOpen(true)}>Удалить</button>
                <button className={"buttonUpdate"} onClick={() => setIsUpdateOpen(true)}>Update</button>

            </div>

            <Dialog open={isOpen} onClose={() =>  setIsOpen(false)}>
                <DialogTitle>Заполните данные</DialogTitle>
                <DialogContent>
                    <form onSubmit={() => {console.log('')}}>
                        <TextField
                            label="&nbsp;&nbsp;Имя"
                            variant="standard"
                            fullWidth
                            margin="normal"
                            onChange={e => setUsername(e.target.value) }
                            value={username}
                        />
                        <TextField
                            label="&nbsp;&nbsp;E-mail"
                            variant="standard"
                            fullWidth
                            margin="normal"
                            onChange={e => setEmail(e.target.value) }
                            value={email}
                        />
                        <TextField
                            label='&nbsp;&nbsp;password'
                            variant="standard"
                            fullWidth
                            margin="normal"
                            onChange={e => setPassword((e.target.value))}
                            value={password}
                        />
                    </form>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setIsOpen(false)}>Отмена</Button>
                    <Button variant="contained" onClick={handleRegister}>
                        Сохранить
                    </Button>
                </DialogActions>
            </Dialog>

            <Dialog open={isDeleteOpen} onClose={() => { setIsDeleteOpen(false); setDeleteId()}}>
                <DialogTitle>Заполните данные</DialogTitle>
                <DialogContent>
                    <form onSubmit={() => {console.log('')}}>
                        <TextField
                            label="&nbsp;&nbsp;Введите id студента"
                            variant="standard"
                            fullWidth
                            margin="normal"
                            onChange={e => setDeleteId(e.target.value) }
                            value={deleteId}
                        />
                    </form>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => {
                        setIsDeleteOpen(false)
                        setDeleteId("");
                    }}>Отмена</Button>
                    <Button variant="contained" onClick={deleteStudent}>
                        Удалить
                    </Button>
                </DialogActions>
            </Dialog>


            <Dialog open={isUpdateOpen} onClose={() =>  setIsUpdateOpen(false)}>
                <DialogTitle>Измените данные</DialogTitle>
                <DialogContent>
                    <form onSubmit={() => {console.log('')}}>
                        <TextField
                            label="&nbsp;&nbsp;Введите id студента"
                            variant="standard"
                            fullWidth
                            margin="normal"
                            onChange={e => setUpdateId(e.target.value) }
                            value={updateId}
                        />
                        <TextField
                            label="&nbsp;&nbsp;Имя"
                            variant="standard"
                            fullWidth
                            margin="normal"
                            onChange={e => setUsername(e.target.value) }
                            value={username}
                        />
                        <TextField
                            label="&nbsp;&nbsp;E-mail"
                            variant="standard"
                            fullWidth
                            margin="normal"
                            onChange={e => setEmail(e.target.value) }
                            value={email}
                        />
                    </form>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setIsUpdateOpen(false)}>Отмена</Button>
                    <Button variant="contained" onClick={updateStudent}>
                        Сохранить
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

export default MainPage;