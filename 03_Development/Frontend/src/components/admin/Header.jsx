import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './Header.module.css'; // import CSS module

const Header = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('user');
    navigate('/');
  };

  return (
    <header className={styles.header}>
      <h1 className={styles.title}>Admin Dashboard</h1>
      <button onClick={handleLogout} className={styles.logoutBtn}>
        Đăng xuất
      </button>
    </header>
  );
};

export default Header;
