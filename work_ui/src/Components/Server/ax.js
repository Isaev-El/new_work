import axios from "axios";

export const ax =
    axios.create({
    baseURL: 'http://localhost:9999',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
        }
    });