import React from 'react';
import Sidebar from './Sidebar';
import Header from './Header';
import styles from './Layout.module.css';
import { Outlet } from 'react-router-dom';

const Layout = () => {
  return (
    <div className={styles.layoutContainer}>
      <Sidebar />
      <div className={styles.mainArea}>
        <Header />
        <main className={styles.mainContent}>
          <Outlet />  {/* Đây sẽ render các route con */}
        </main>
      </div>
    </div>
  );
};

export default Layout;
