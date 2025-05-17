import React from 'react';
import { Link } from 'react-router-dom';
import styles from '../assets/css/Home.module.css';

const Home = () => {
  return (
    <div style={{ overflow: 'hidden', margin: 0, padding: 0, width: '100vw' }}>
      <nav className={`navbar navbar-expand-lg navbar-light ${styles.navbar}`}>
        <div className="container">
          <Link className="navbar-brand fw-bold" to="/">Chung cư Blue Moon</Link>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse justify-content-end" id="navbarNav">
            <ul className="navbar-nav">
              <li className="nav-item">
                <a className={`nav-link ${styles.navLink}`} href="#gioithieu">Giới thiệu</a>
              </li>
              <li className="nav-item">
                <Link className={`nav-link ${styles.navLink}`} to="/login">Đăng nhập</Link>
              </li>
            </ul>
          </div>
        </div>
      </nav>

      <section className={`${styles.hero} text-white`} id="gioithieu">
        <div className={styles.overlay}></div>
        <div className={`container text-center ${styles.container}`}>
          <h1 className="display-5 fw-bold">Hệ thống quản lý thu phí chung cư</h1>
          <p className="lead mt-3">
            Hệ thống giúp ban quản lý dễ dàng theo dõi, thống kê và thông báo các khoản thu của cư dân một cách minh bạch và chính xác.
          </p>
        </div>
      </section>

      <section className={styles.stats}>
        <div className={`container ${styles.container}`}>
          <div className="row text-center">
            <div className="col-md-4 mb-4">
              <h4>200+ Căn hộ</h4>
              <p>Quy mô lớn với nhiều tiện ích hiện đại</p>
            </div>
            <div className="col-md-4 mb-4">
              <h4>100% Thanh toán điện tử</h4>
              <p>Thu phí dễ dàng và nhanh chóng qua hệ thống</p>
            </div>
            <div className="col-md-4 mb-4">
              <h4>Hỗ trợ 24/7</h4>
              <p>Đội ngũ hỗ trợ luôn sẵn sàng giúp đỡ cư dân</p>
            </div>
          </div>
        </div>
      </section>

      <footer className={styles.footer}>
        <div className={`container ${styles.container}`}>
          <p className="mb-0">© 2025 Chung cư ABC. Mọi quyền được bảo lưu.</p>
        </div>
      </footer>
    </div>
  );
};

export default Home;
