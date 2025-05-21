import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from '../assets/css/Login.module.css';

const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      });

      const data = await response.json();
      
      if (!response.ok) {
        throw new Error(data || 'Đăng nhập thất bại');
      }

      console.log('Response data:', data); // Để debug

      // Lưu thông tin người dùng vào localStorage với tên trường chính xác
      localStorage.setItem('user', JSON.stringify({
        userId: data.user_id,
        username: data.user_name,
        roleId: data.role, // Thay đổi từ roleId thành role
        fullname: data.fullname,
      }));

      // Điều hướng dựa vào role
      switch (data.role) { // Thay đổi từ roleId thành role
        case 1: 
          navigate('/resident');
          break;
        case 2: 
          navigate('/manager');
          break;
        case 3: 
          navigate('/admin');
          break;
        default:
          navigate('/');
      }
    } catch (error) {
      console.error('Login error:', error); // Để debug
      setError(error.message || 'Đã có lỗi xảy ra khi đăng nhập');
    }
  };

  return (
    <div className={styles.loginContainer}>
      <div className={styles.overlay}></div>
      
      <div className={styles.formContainer}>
        <div className={styles.formWrapper}>
          <h2 className="mb-4">Đăng nhập</h2>
          {error && (
            <div className="alert alert-danger" role="alert">
              {error}
            </div>
          )}
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <input
                type="text"
                className="form-control"
                placeholder="Email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <input
                type="password"
                className="form-control"
                placeholder="Mật khẩu"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </div>
            <button type="submit" className="btn btn-primary w-100">
              Đăng nhập
            </button>
          </form>
          <div className="mt-3 text-center">
            <Link to="/" className={styles.backLink}>
              Quay về trang chủ
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login; 