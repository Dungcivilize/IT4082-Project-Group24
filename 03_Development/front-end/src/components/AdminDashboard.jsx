import React, { useEffect } from 'react';
import { useNavigate, Link, Outlet } from 'react-router-dom';
import styles from '../assets/css/Dashboard.module.css'; // Tùy chỉnh CSS tại đây nếu cần

const AdminDashboard = () => {
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem('user'));

  useEffect(() => {
    if (!user) {
      navigate('/login');
    }
  }, [user, navigate]);

  const handleLogout = () => {
    localStorage.removeItem('user');
    navigate('/login');
  };

  return (
    <div className={styles.dashboardContainer} style={{ display: 'flex', height: '100vh' }}>
      
      {/* Sidebar */}
      <div className={styles.sidebar} style={{
        width: '220px',
        backgroundColor: '#343a40',
        color: 'white',
        padding: '20px'
      }}>
        <h3 className="text-white">Admin</h3>
        <ul style={{ listStyleType: 'none', padding: 0 }}>
          <li><Link to="/dashboard/users" className="text-white text-decoration-none d-block py-2">👤 User</Link></li>
          <li><Link to="/dashboard/presidents" className="text-white text-decoration-none d-block py-2">🏠 President</Link></li>
          <li><Link to="/dashboard/services" className="text-white text-decoration-none d-block py-2">🛠 Service</Link></li>
        </ul>
        <div style={{ marginTop: '30px' }}>
          <p>Xin chào, <strong>{user?.fullname}</strong></p>
          <button onClick={handleLogout} className="btn btn-danger w-100">Đăng xuất</button>
        </div>
      </div>

      {/* Nội dung chính */}
      <div className={styles.mainContent} style={{ flex: 1, padding: '20px' }}>
        <Outlet /> {/* Router sẽ hiển thị nội dung tương ứng ở đây */}
      </div>
    </div>
  );
};

export default AdminDashboard;
