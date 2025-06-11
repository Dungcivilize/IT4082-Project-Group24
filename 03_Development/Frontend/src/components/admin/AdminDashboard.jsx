import React, { useEffect, useState } from 'react';
import './AdminDashboard.css';

const AdminDashboard = () => {
  const [widgets, setWidgets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchWidgetsData();
  }, []);

  const fetchWidgetsData = async () => {
    try {
      setLoading(true);
      setError(null);

      const results = await Promise.allSettled([
        fetch('http://localhost:8080/api/statistics/residents'),
        fetch('http://localhost:8080/api/requests/temp'),
        fetch('http://localhost:8080/api/statistics/vehicles')
      ]);

      const widgetList = [];

      // Dân cư
      if (results[0].status === 'fulfilled' && results[0].value.ok) {
        const data = await results[0].value.json();
        const total = Object.values(data.genderStats || {}).reduce((a, b) => a + b, 0);
        widgetList.push({
          icon: '👥',
          title: 'Tổng số người dùng',
          value: `${total}`
        });
      }

      // Yêu cầu tạm trú/tạm vắng
      if (results[1].status === 'fulfilled' && results[1].value.ok) {
        const data = await results[1].value.json();
        widgetList.push({
          icon: '📬',
          title: 'Tạm trú / Tạm vắng',
          value: `${data.total || 0} yêu cầu mới`
        });
      }

      // Phương tiện
      if (results[2].status === 'fulfilled' && results[2].value.ok) {
        const data = await results[2].value.json();
        const total = Object.values(data || {}).reduce((a, b) => a + b, 0);
        widgetList.push({
          icon: '🚗',
          title: 'Phương tiện đăng ký',
          value: `${total} phương tiện`
        });
      }

      setWidgets(widgetList);
    } catch (err) {
      setError(err.message || 'Lỗi không xác định');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="admin-dashboard">
      <h2>Xin chào, Quản trị viên 👋</h2>
      <p>Chào mừng bạn đến với trang quản trị của hệ thống.</p>

      {loading ? (
        <p>Đang tải dữ liệu...</p>
      ) : error ? (
        <div className="dashboard-error">
          <p>{error}</p>
          <button onClick={fetchWidgetsData}>Thử lại</button>
        </div>
      ) : (
        <div className="dashboard-widgets">
          {widgets.map((item, idx) => (
            <div className="widget" key={idx}>
              <h4>{item.icon} {item.title}</h4>
              <p>{item.value}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
