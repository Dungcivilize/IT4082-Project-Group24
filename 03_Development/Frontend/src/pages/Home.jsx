import React, { useState } from 'react'
import Login from '../components/auth/Login'
import '../styles/Home.css'

const Home = () => {
  const [showLoginModal, setShowLoginModal] = useState(false)

  return (
    <div className="home">
      <div className="home-content">
        <div className="home-header">
          <h1>Hệ thống Quản lý Chung cư</h1>
          <p className="subtitle">Chào mừng đến với hệ thống quản lý chung cư thông minh</p>
        </div>

        <div className="features">
          <div className="feature-item">
            <i className="fas fa-building"></i>
            <h3>Quản lý hiệu quả</h3>
            <p>Hệ thống quản lý toàn diện các hoạt động trong chung cư</p>
          </div>
          <div className="feature-item">
            <i className="fas fa-users"></i>
            <h3>Tiện ích cư dân</h3>
            <p>Đầy đủ các tính năng phục vụ nhu cầu của cư dân</p>
          </div>
          <div className="feature-item">
            <i className="fas fa-shield-alt"></i>
            <h3>An toàn & Bảo mật</h3>
            <p>Đảm bảo an toàn thông tin và quyền riêng tư của người dùng</p>
          </div>
        </div>

        <button className="login-trigger" onClick={() => setShowLoginModal(true)}>
          Đăng nhập
        </button>
      </div>

      {showLoginModal && (
        <div className="modal-overlay">
          <div className="modal">
            <button className="close-button" onClick={() => setShowLoginModal(false)}>
              <i className="fas fa-times"></i>
            </button>
            <Login />
          </div>
        </div>
      )}
    </div>
  )
}

export default Home 