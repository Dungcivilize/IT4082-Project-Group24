import React from 'react';
import './AdminDashboard.css'; // tạo thêm file CSS nếu muốn

const AdminDashboard = () => {
  return (
    <div className="admin-dashboard">
      <h2>Xin chào, Quản trị viên 👋</h2>
      <p>Chào mừng bạn đến với trang quản trị của hệ thống.</p>

      <div className="dashboard-widgets">
        <div className="widget">
          <h4>👥 Tổng số người dùng</h4>
          <p>123</p>
        </div>
        <div className="widget">
          <h4>📬 Tạm trú / Tạm vắng</h4>
          <p>12 yêu cầu mới</p>
        </div>
        <div className="widget">
          <h4>🚗 Phương tiện đăng ký</h4>
          <p>47 phương tiện</p>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
