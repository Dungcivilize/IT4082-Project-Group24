import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../assets/css/Dashboard.module.css';

const ResidentDashboard = () => {
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem('user'));

  const handleLogout = () => {
    localStorage.removeItem('user');
    navigate('/login');
  };

  return (
    <div className={styles.dashboard}>
      <nav className={styles.navbar}>
        <div className="container d-flex justify-content-between align-items-center">
          <h1 className={styles.title}>Resident Dashboard</h1>
          <button onClick={handleLogout} className="btn btn-danger">
            Đăng xuất
          </button>
        </div>
      </nav>
      
      <div className="container mt-4">
        <div className="card">
          <div className="card-body">
            <h5 className="card-title">Thông tin người dùng</h5>
            <p className="card-text">
              <strong>Tên người dùng:</strong> {user?.fullname}<br />
              <strong>Email:</strong> {user?.username}<br />
              <strong>Vai trò:</strong> Cư dân<br />
              <strong>ID:</strong> {user?.userId}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ResidentDashboard; 