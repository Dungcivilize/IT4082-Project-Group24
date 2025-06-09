import React, { useEffect } from 'react';
import { Outlet, Link, useNavigate, useLocation } from 'react-router-dom';
import '../../styles/Layout.css';

const AccountantLayout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const user = JSON.parse(localStorage.getItem('user'));

  useEffect(() => {
    if (!user || user.role !== 'accountant') {
      navigate('/');
    }
  }, [user, navigate]);

  const isActive = (path) => {
    return location.pathname === `/accountant${path}` ? 'active' : '';
  };

  const menuItems = [
    {
      path: '/personal-info',
      icon: '👤',
      text: 'Thông tin cá nhân',
    },
    {
      path: '/services',
      icon: '💰',
      text: 'Quản lý dịch vụ',
    },
    {
      path: '/payment-periods',
      icon: '📅',
      text: 'Quản lý kỳ thu phí',
    },
    {
      path: '/service-usage',
      icon: '📊',
      text: 'Nhập số lượng dịch vụ',
    },
    {
      path: '/payment-status',
      icon: '📝',
      text: 'Trạng thái thanh toán',
    },
    {
      path: '/statistics',
      icon: '📈',
      text: 'Thống kê tài chính',
    },
  ];

  const handleLogout = () => {
    localStorage.removeItem('user');
    navigate('/');
  };

  if (!user || user.role !== 'accountant') {
    return null;
  }

  return (
    <div className="layout-container">
      <aside className="sidebar">
        <div className="sidebar-header">
          <h2>🏢 Quản lý chung cư</h2>
          <p>Xin chào, {user.email}</p>
        </div>
        <nav className="sidebar-nav">
          {menuItems.map((item) => (
            <Link
              key={item.path}
              to={`/accountant${item.path}`}
              className={`nav-item ${isActive(item.path)}`}
            >
              <span className="nav-icon">{item.icon}</span>
              {item.text}
            </Link>
          ))}
        </nav>
        <div className="sidebar-footer">
          <button onClick={handleLogout} className="logout-button">
            🚪 Đăng xuất
          </button>
        </div>
      </aside>
      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
};

export default AccountantLayout; 