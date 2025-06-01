import React from 'react';
import { Link } from 'react-router-dom';
import './Sidebar.css'; // Tùy chỉnh CSS theo ý bạn

const Sidebar = () => {
  return (
    <div className="sidebar">
      <h2>Dashboard</h2>
      <ul>
        <li><Link to="/admin/user">User</Link></li>
        <li><Link to="/admin/resident">Resident</Link></li>
        <li><Link to="/admin/service">Service</Link></li>
      </ul>
    </div>
  );
};

export default Sidebar;
