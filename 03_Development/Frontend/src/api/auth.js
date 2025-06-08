import axios from 'axios';
import { API_URL } from '../constants/api';


export const login = async (email, password) => {
  try {
    const response = await axios.post(`${API_URL}/auth/login`, {
      email,
      password
    });
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data);
    }
    throw new Error('Đã có lỗi xảy ra khi đăng nhập');
  }
}; 